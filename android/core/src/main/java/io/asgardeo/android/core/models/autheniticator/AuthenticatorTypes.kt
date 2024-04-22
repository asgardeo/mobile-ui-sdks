package io.asgardeo.android.core.models.autheniticator

/**
 * Enum class for authenticators
 *
 * @property authenticatorType Authenticator type value
 */
enum class AuthenticatorTypes(val authenticatorType: String) {
    /**
     * Basic authenticator type
     */
    BASIC_AUTHENTICATOR("Username & Password"),
    /**
     * Google native authenticator type
     */
    GOOGLE_AUTHENTICATOR("Google"),
    /**
     * TOTP authenticator type
     */
    TOTP_AUTHENTICATOR("TOTP"),
    /**
     * Passkey authenticator type
     */
    PASSKEY_AUTHENTICATOR("Passkey"),
    /**
     * OpenID Connect authenticator type
     */
    OPENID_CONNECT_AUTHENTICATOR("openidconnect"),
    /**
     * Github authenticator type
     */
    GITHUB_REDIRECT_AUTHENTICATOR("Github"),
    /**
     * Microsoft authenticator type
     */
    MICROSOFT_REDIRECT_AUTHENTICATOR("openidconnect"),
    /**
     * Email OTP authenticator type
     */
    EMAIL_OTP_AUTHENTICATOR("Email OTP"),
    /**
     * SMS OTP authenticator type
     */
    SMS_OTP_AUTHENTICATOR("SMS OTP")
}
