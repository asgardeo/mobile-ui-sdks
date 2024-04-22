package io.asgardeo.android.core.models.auth_params

/**
 * Authenticator parameters class - For TOTP Authenticator.
 *
 * Currently this class is not used for authentication with TOTP as a first factor, only for the
 * second factor and beyond. If in future TOTP we should use this class for the first factor
 * as well.
 */
data class TotpAuthenticatorTypeAuthParams(
    /**
     * Code retrieved from the authenticator application
     */
    override val token: String? = null,
    /**
     * Username of the user - if the authenticator used in the first step
     */
    override val username: String? = null
) : AuthParams(
    token = token
) {
    /**
     * Get the parameter body for the authenticator to be sent to the server
     *
     * @return LinkedHashMap<String, String> - Parameter body for the authenticator
     * ```
     * ex: [<"totp", totp>]
     * ```
     */
    override fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String> {
        val paramBody = LinkedHashMap<String, String>()

        if (token == null || username == null) {
            paramBody[requiredParams[0]] = token ?: username!!
        } else {
            paramBody[requiredParams[0]] = username
            paramBody[requiredParams[1]] = token
        }

        return paramBody
    }
}
