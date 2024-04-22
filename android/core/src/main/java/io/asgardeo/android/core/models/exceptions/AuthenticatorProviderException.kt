package io.asgardeo.android.core.models.exceptions

import io.asgardeo.android.core.models.autheniticator.Authenticator

/**
 * Exception to be thrown to the exception related to [Authenticator]
 *
 * @property message Message related to the exception
 */
class AuthenticatorProviderException(
    override val message: String?
) : Exception(message) {
    companion object {
        /**
         * AuthenticatorProvider Exception
         */
        const val AUTHENTICATOR_PROVIDER_EXCEPTION = "AuthenticatorProvider Exception"

        /**
         * Message for the case where the authenticator is not found
         */
        const val AUTHENTICATOR_NOT_FOUND = "Authenticator not found"

        /**
         * Message for the case where the authenticator is not supported
         */
        const val NOT_REDIRECT_PROMPT = "Authenticator does not support redirect prompt"

        /**
         * Message for the case where the redirect URI is not found
         */
        const val REDIRECT_URI_NOT_FOUND = "Redirect URI not found"
    }

    override fun toString(): String {
        return "$AUTHENTICATOR_PROVIDER_EXCEPTION: $message"
    }
}
