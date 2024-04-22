package io.asgardeo.android.core.models.auth_params

/**
 * Authenticator parameters class - For Passkey Authenticator
 */
data class PasskeyAuthenticatorTypeAuthParams(
    /**
     * Token response retrieved from the passkey authenticator
     */
    override val tokenResponse: String
) : AuthParams(tokenResponse = tokenResponse) {
    /**
     * Get the parameter body for the authenticator to be sent to the server
     *
     * @return LinkedHashMap<String, String> - Parameter body for the authenticator
     * ```
     * ex: [<"tokenResponse", tokenResponse>]
     * ```
     */
    override fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String> {
        val paramBody = LinkedHashMap<String, String>()
        paramBody[requiredParams[0]] = tokenResponse

        return paramBody
    }
}
