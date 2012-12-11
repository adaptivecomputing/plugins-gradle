package com.adaptc.mws.plugins;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;

/**
 * This service is used to aid in loading certificates for SSL communication with other APIs.
 * Each method generates a {@link SSLSocketFactory} instance that uses the specified
 * client and/or server certificate(s).  This socket factory may be used to generate a SSL
 * based Socket or may be used in combination with other communication libraries (such as the
 * {@link HttpsURLConnection}).
 * <p/>
 * Methods are provided for loading and using a client certificate file with an optional password
 * as well as overriding the trust store to use specified server or chain certificates.  Certificate
 * files may be in the PEM file format and do not need to be converted in DER as is typical of Java.
 * <p/>
 * It should be noted that this service is not needed when performing SSL communications with
 * known and trusted certificates, such as when communicating with HTTPS enabled websites that
 * do not have a self-signed certificate.
 * @author bsaville
 */
public interface ISslService {
	/**
	 * Generates a socket factory that automatically trusts all server certificates.<br/>
	 * <b>WARNING</b>: If this socket factory is used, it may present a large security risk.  Use only
	 * during development and only when the risks are understood.
	 * @return An SSLSocketFactory instance that may be used in the communication library of choice
	 * @throws Exception On error initializing the SSL context
	 */
	public SSLSocketFactory getLenientSocketFactory() throws Exception;

	/**
	 * Generates a hostname verifier that automatically trusts all host names.<br/>
	 * <b>WARNING</b>: If this hostname verifier is used, it may present a large security risk.  Use only
	 * during development and only when the risks are understood.
	 * @return A HostnameVerifier instance that may be used in the communication library of choice
	 */
	public HostnameVerifier getLenientHostnameVerifier();

	/**
	 * Generates a socket factory for the specified options.
	 * <p/>
	 * If the certificate file is a relative path (no leading '/'), it will be loaded
	 * from the MWS certificates directory as documented.
	 * @param clientCertificate The client certificate file to use
	 * @param clientCertAlias The alias of the client certificate to use for socket communication, may be null to use the given certificate's alias
	 * @return An SSLSocketFactory instance that may be used in the communication library of choice
	 * @throws Exception On error initializing the SSL context or if the certificate(s) are invalid
	 */
	public SSLSocketFactory getSocketFactory(String clientCertificate, String clientCertAlias) throws Exception;

	/**
	 * Generates a socket factory for the specified options.
	 * <p/>
	 * If the certificate file or private key is a relative path (no leading '/'), it will be loaded
	 * from the MWS certificates directory as documented.
	 * @param clientCertificate The client certificate file to use
	 * @param clientCertAlias The alias of the client certificate to use for socket communication, may be null to use the given certificate's alias
	 * @param clientPrivateKey The client private key file to use
	 * @param clientKeyPassword The password to decrypt the client certificate private key, may be null to use an unencrypted certificate
	 * @return An SSLSocketFactory instance that may be used in the communication library of choice
	 * @throws Exception On error initializing the SSL context or if the certificate(s) are invalid
	 */
	public SSLSocketFactory getSocketFactory(String clientCertificate, String clientCertAlias,
									String clientPrivateKey, String clientKeyPassword) throws Exception;

	/**
	 * Generates a socket factory for the specified options.
	 * <p/>
	 * If the client, private key, or server certificate file is a relative path (no leading '/'), it will be loaded
	 * from the MWS certificates directory as documented.
	 * @param serverCertificate The server certificate(s) or chain certificate to use (multiple certificates may be present in this file)
	 * @return An SSLSocketFactory instance that may be used in the communication library of choice
	 * @throws Exception On error initializing the SSL context or if the certificate(s) are invalid
	 */
	public SSLSocketFactory getSocketFactory(String serverCertificate) throws Exception;

	/**
	 * Generates a socket factory for the specified options.
	 * <p/>
	 * If the client or server certificate file is a relative path (no leading '/'), it will be loaded
	 * from the MWS certificates directory as documented.
	 * @param clientCertificate The client certificate file to use
	 * @param clientCertAlias The alias of the client certificate to use for socket communication, may be null to use the given certificate's alias
	 * @param serverCertificate The server certificate(s) or chain certificate to use (multiple certificates may be present in this file)
	 * @return An SSLSocketFactory instance that may be used in the communication library of choice
	 * @throws Exception On error initializing the SSL context or if the certificate(s) are invalid
	 */
	public SSLSocketFactory getSocketFactory(String clientCertificate, String clientCertAlias,
											 String serverCertificate) throws Exception;

	/**
	 * Generates a socket factory for the specified options.
	 * <p/>
	 * If the client, private key, or server certificate file is a relative path (no leading '/'), it will be loaded
	 * from the MWS certificates directory as documented.
	 * @param clientCertificate The client certificate file to use
	 * @param clientCertAlias The alias of the client certificate to use for socket communication, may be null to use the given certificate's alias
	 * @param clientPrivateKey The client private key file to use
	 * @param clientKeyPassword The password to decrypt the client certificate private key, may be null to use an unencrypted certificate
	 * @param serverCertificate The server certificate(s) or chain certificate to use (multiple certificates may be present in this file)
	 * @return An SSLSocketFactory instance that may be used in the communication library of choice
	 * @throws Exception On error initializing the SSL context or if the certificate(s) are invalid
	 */
	public SSLSocketFactory getSocketFactory(String clientCertificate, String clientCertAlias,
									String clientPrivateKey, String clientKeyPassword,
									String serverCertificate) throws Exception;

	/**
	 * Returns the file representing the certificate for the given filename.  This includes accounting for relative
	 * and absolute file paths and handling of the MWS certificates location.
	 * @param filename The filename of the certificate, either absolute or relative.
	 * @return The resulting file or null if filename is null
	 */
	public File getCertificateFile(String filename);
}
