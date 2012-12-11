package com.adaptc.mws.plugins;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Used on plugin custom web service methods to signify that the web service should not be
 * secured and can be called externally without any authentication.
 * @author bsaville
 */
@Retention(RetentionPolicy.RUNTIME) // Make this annotation accessible at runtime via reflection.
@Target({ElementType.METHOD})	   // This annotation can only be applied to class methods.
public @interface Unsecured {

}
