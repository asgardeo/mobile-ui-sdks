package io.asgardeo.android.core.models.auth_params

/**
 * Authenticator parameters class - For Basic Authenticator
 */
data class BasicAuthenticatorAuthParams(
    /**
     * Username of the user
     */
    override val username: String,
    /**
     * Password of the user
     */
    override val password: String
) : AuthParams(username = username, password = password) {
    /**
     * Get the parameter body for the authenticator to be sent to the server
     *
     * @return LinkedHashMap<String, String> - Parameter body for the authenticator
     * ```
     * ex: [<"username", username>, <"password", password>]
     * ```
     */
    override fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String> {
        val paramBody = LinkedHashMap<String, String>()
        paramBody[requiredParams[0]] = username
        paramBody[requiredParams[1]] = password

        return paramBody
    }
}
