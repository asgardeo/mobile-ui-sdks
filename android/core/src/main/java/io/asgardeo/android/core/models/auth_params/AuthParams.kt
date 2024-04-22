package io.asgardeo.android.core.models.auth_params

import io.asgardeo.android.core.util.JsonUtil

/**
 * Authenticator parameters class
 */
abstract class AuthParams(
    /**
     * Username of the user - For Basic Authenticator, TOTP Authenticator, Email OTP Authenticator, SMS OTP Authenticator
     */
    open val username: String? = null,
    /**
     * Password of the user - For Basic Authenticator
     */
    open val password: String? = null,
    /**
     * access token retrieved from the Google authenticator - For Google Native Authenticator
     */
    open val accessToken: String? = null,
    /**
     * id token retrieved from the Google authenticator - For Google Native Authenticator
     */
    open val idToken: String? = null,
    /**
     * Code retrieved from the authenticator application - For TOTP Authenticator
     */
    open val token: String? = null,
    /**
     * Token response retrieved from the passkey authenticator - For Passkey Authenticator
     */
    open val tokenResponse: String? = null,
    /**
     * OTP code retrieved from the authenticator application - For SMS OTP Authenticator, Email OTP Authenticator
     */
    open val otpCode: String? = null
) {
    fun toJsonString(): String {
        return JsonUtil.getJsonString(this)
    }

    /**
     * Get the parameter body for the authenticator to be sent to the server
     */
    abstract fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String>
}
