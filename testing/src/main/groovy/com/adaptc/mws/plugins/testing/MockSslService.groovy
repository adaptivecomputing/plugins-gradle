package com.adaptc.mws.plugins.testing

import com.adaptc.mws.plugins.ISslService

import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509KeyManager
import java.security.Principal
import java.security.cert.X509Certificate
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.KeyStore
import org.apache.commons.ssl.KeyStoreBuilder
import java.security.cert.Certificate
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.KeyManager
import javax.net.ssl.SSLContext
import java.security.SecureRandom
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import org.apache.commons.logging.LogFactory
import org.apache.commons.logging.Log

/**
 * A fully functional mock implementation of ISslService.  This should be used in testing only and should never
 * be referred to in production code.  The basic difference between this service and the true implementation in MWS
 * is the location from which the certificates are loaded.
 * <p/>
 * For this service, you may define the base path used for
 * certificate files with the constructor.  If no certificates directory is specified, the current directory (./) will
 * be used.
 * <p/>
 * Using the loadAsResources flag in the constructor, certificates may instead be loaded as resources
 * from the class loader.  This means that you can store test resources in src/test/resources/* and refer them by the
 * filename minus src/test/resources (use a leading forward slash).
 * @author bsaville
 */
class MockSslService implements ISslService {
	private static Log log = LogFactory.getLog(this)

	/**
	 * This is the password used to initialize (and potentially store) the keystores generated
	 * and used in-memory during the process of creating socket factories.
	 */
	private final char[] KEYSTORE_PASSWORD = "changeit".toCharArray();
	/**
	 * The absolute path of the root directory.  This is to account for Windows or Linux machines.
	 * For linux, this is the same as {@link File#separator}.  For Windows, this is the equivalent of
	 * C:\ for the device that is being used.
	 */
	private static final String ABSOLUTE_PATH_ROOT = new File("/").absolutePath

	private final File certificatesDir
	private boolean loadAsResources

	public MockSslService(loadAsResources=false) {
		this.certificatesDir = new File(".")
		this.loadAsResources = loadAsResources
	}

	public MockSslService(File certificatesDir) {
		this.loadAsResources = false
		if (certificatesDir)
			this.certificatesDir = certificatesDir
		else
			this.certificatesDir = new File(".")
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	SSLSocketFactory getLenientSocketFactory() {
		def trustManagers = getLenientTrustManagers()
		return getSocketFactoryInternal(null, trustManagers)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	HostnameVerifier getLenientHostnameVerifier() {
		return ([
				verify:{ String string, SSLSession sslSession ->
					return true
				}
		] as HostnameVerifier)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSLSocketFactory getSocketFactory(String clientCertificate, String clientCertAlias) throws Exception {
		def keyManagers = getKeyManagers(clientCertificate, clientCertAlias, null, null)
		return getSocketFactoryInternal(keyManagers, null)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSLSocketFactory getSocketFactory(String clientCertificate, String clientCertAlias,
											 String clientPrivateKey, String clientKeyPassword) throws Exception {
		def keyManagers = getKeyManagers(clientCertificate, clientCertAlias, clientPrivateKey, clientKeyPassword)
		return getSocketFactoryInternal(keyManagers, null)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSLSocketFactory getSocketFactory(String serverCertificate) throws Exception {
		def trustManagers = getTrustManagers(serverCertificate)
		return getSocketFactoryInternal(null, trustManagers)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSLSocketFactory getSocketFactory(String clientCertificate, String clientCertAlias,
											 String serverCertificate) throws Exception {
		def keyManagers = getKeyManagers(clientCertificate, clientCertAlias, null, null)
		def trustManagers = getTrustManagers(serverCertificate)
		return getSocketFactoryInternal(keyManagers, trustManagers)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSLSocketFactory getSocketFactory(String clientCertificate, String clientCertAlias, String clientPrivateKey,
											 String clientKeyPassword, String serverCertificate) throws Exception {
		def keyManagers = getKeyManagers(clientCertificate, clientCertAlias, clientPrivateKey, clientKeyPassword)
		def trustManagers = getTrustManagers(serverCertificate)
		return getSocketFactoryInternal(keyManagers, trustManagers)
	}

	private SSLSocketFactory getSocketFactoryInternal(KeyManager[] keyManagers,
													  TrustManager[] trustManagers) throws Exception {
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(keyManagers, trustManagers, new SecureRandom());
		return context.getSocketFactory();
	}

	/**
	 * Replace the original KeyManagers with the AliasForcingKeyManager
	 * @param keyManagerFactory
	 * @param clientCertAlias
	 * @return
	 * @throws Exception
	 */
	private KeyManager[] getKeyManagers(String clientCertificate, String clientCertAlias,
										String clientPrivateKey, String clientKeyPassword) throws Exception {
		// Build the client certificate key store using the KeyStoreBuilder
		def clientCertificateFile = getCertificateFile(clientCertificate)
		log.debug("Using client certificate file ${clientCertificateFile}")
		def clientBytes = clientCertificateFile.bytes
		KeyStore keyStore
		if (!clientPrivateKey)
			keyStore = KeyStoreBuilder.build(clientBytes, KEYSTORE_PASSWORD)
		else {
			log.debug("Utilizing ${clientKeyPassword==null?'':'encrypted '}private key ${clientPrivateKey}")

			// The KeyStoreBuilder is relatively undocumented, but this override of the build method
			// makes it appear as if the keystore password should be the 3rd parameter, but it is actually
			// the 4th.  Not sure why they named them as they did, but this works.
			def clientKeyBytes = getCertificateFile(clientPrivateKey).bytes
			if (clientKeyPassword == null)
				keyStore = KeyStoreBuilder.build(clientBytes, clientKeyBytes, null, KEYSTORE_PASSWORD)
			else {
				keyStore = KeyStoreBuilder.build(clientBytes, clientKeyBytes, clientKeyPassword.toCharArray(), KEYSTORE_PASSWORD)
			}
		}

		// Can use this line for debugging the store
		//keyStore.store(new FileOutputStream("/opt/mws/security/keystore.jks"), KEYSTORE_PASSWORD)

		// Init key manager factory with key store
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
		keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD);

		if (!clientCertAlias) {
			log.debug("Setting client certificate alias from loaded client certificate")
			clientCertAlias = keyStore.aliases().nextElement()
		}

		log.debug("Forcing the socket factory to use the client certificate alias '${clientCertAlias}'")
		KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
		for (int i = 0; i < keyManagers.length; i++) {
			if (keyManagers[i] instanceof X509KeyManager) {
				keyManagers[i] = new AliasForcingKeyManager((X509KeyManager) keyManagers[i], clientCertAlias);
			}
		}
		return keyManagers
	}

	private TrustManager[] getTrustManagers(String serverCertificate) throws Exception {
		// Build trust key store with CA certificate
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509")
		KeyStore trustStore = KeyStore.getInstance("JKS")
		trustStore.load(null, KEYSTORE_PASSWORD)
		certificateFactory.generateCertificates(new FileInputStream(getCertificateFile(serverCertificate))).eachWithIndex { Certificate cert, int i ->
			trustStore.setCertificateEntry("ca${i}", cert)
		}
		// Can use this line for debugging the store
		//trustStore.store(new FileOutputStream("/opt/mws/security/truststore.jks"), KEYSTORE_PASSWORD)

		// Init trust manager factory with trust store
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
		trustManagerFactory.init(trustStore)
		return trustManagerFactory.getTrustManagers()
	}

	private TrustManager[] getLenientTrustManagers() throws Exception {
		return [
				[
						getAcceptedIssuers:{ -> return null },
						checkClientTrusted:{ X509Certificate[] certs, String authType -> },
						checkServerTrusted:{ X509Certificate[] certs, String authType -> }
				] as X509TrustManager
		] as X509TrustManager[]
	}

	public File getCertificateFile(String fileName) {
		if (loadAsResources) {
			def resource = this.class.classLoader.getResource(fileName)
			if (!resource)
				return new File(fileName)
			return new File(resource.toURI())
		}
		if (!fileName.startsWith(ABSOLUTE_PATH_ROOT) && !fileName.startsWith(File.separator))
			return new File(certificatesDir, fileName)
		return new File(fileName)
	}
}


/*
 * This wrapper class overwrites the default behavior of a X509KeyManager and
 * always render a specific certificate whose clientAlias matches that provided in the constructor
 */
class AliasForcingKeyManager implements X509KeyManager {
	X509KeyManager baseKM = null;
	String clientAlias

	public AliasForcingKeyManager(X509KeyManager keyManager, String clientAlias) {
		baseKM = keyManager
		this.clientAlias = clientAlias
	}

	/*
	 * Always render the specific clientAlias provided in the constructor
	 */
	public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
		return clientAlias;
	}

	/**
	 * Always render the specific serverAlias provided in the constructor
	 */
	public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
		return baseKM.chooseServerAlias(keyType, issuers, socket)
	}

	public X509Certificate[] getCertificateChain(String alias) {
		return baseKM.getCertificateChain(alias);
	}

	public String[] getClientAliases(String keyType, Principal[] issuers) {
		return baseKM.getClientAliases(keyType, issuers);
	}

	public PrivateKey getPrivateKey(String alias) {
		return baseKM.getPrivateKey(alias);
	}

	public String[] getServerAliases(String keyType, Principal[] issuers) {
		return baseKM.getServerAliases(keyType, issuers);
	}
}