package io.asgardeo.android.core.models.auth_params

/**
 * Authenticator parameters class - For TOTP Authenticator
 *
 * Currently this class is not used for authentication with Email OTP as a first factor, only for the
 * second factor and beyond. If in future Email OTP we should use this class for the first factor
 */
data class EmailOTPAuthenticatorTypeAuthParams(
    /**
     * Email OTP code received to the user
     */
    override val otpCode: String? = null,
    /**
     * Username of the user - if the authenticator used in the first step
     */
    override val username: String? = null
) : AuthParams(otpCode = otpCode) {
    /**
     * Get the parameter body for the authenticator to be sent to the server
     *
     * @return LinkedHashMap<String, String> - Parameter body for the authenticator
     * ```
     * ex: [<"OTPcode", otpCode>]
     * ```
     */
    override fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String> {
        val paramBody = LinkedHashMap<String, String>()

        if (otpCode == null || username == null) {
            paramBody[requiredParams[0]] = otpCode ?: username!!
        } else {
            paramBody[requiredParams[0]] = username
            paramBody[requiredParams[1]] = otpCode
        }

        return paramBody
    }
}
