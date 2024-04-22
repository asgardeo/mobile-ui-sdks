package io.asgardeo.android.core.models.exceptions

class RedirectAuthenticationException (
    override val message: String?
): Exception(message) {
    companion object {
        /**
         * Redirect Authentication exception TAG
         */
        const val REDIRECT_AUTHENTICATION_EXCEPTION = "Redirect Authentication Exception"

        /**
         * Message to be shown when the redirect URI is not found
         */
        const val REDIRECT_URI_NOT_FOUND = "Redirect URI not found"

        /**
         * Message to be shown when the authenticator is not a redirection prompt
         */
        const val NOT_REDIRECT_PROMPT = "Authenticator is not a redirection prompt"

        /**
         * Message to be shown when the redirect authenticator is not selected
         */
        const val AUTHENTICATOR_NOT_SELECTED = "Redirect authenticator is not selected"

        /**
         * Message to be shown when the authentication parameters are not found
         */
        const val AUTHENTICATION_PARAMS_NOT_FOUND = "Authentication parameters not found"

        /**
         * Message to be shown when the authentication is cancelled by the user
         */
        const val AUTHENTICATION_CANCELLED = "Authentication cancelled by the user"
    }

    override fun toString(): String {
        return "$REDIRECT_AUTHENTICATION_EXCEPTION: $message"
    }
}
