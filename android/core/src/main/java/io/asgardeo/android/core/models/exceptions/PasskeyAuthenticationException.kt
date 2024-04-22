package io.asgardeo.android.core.models.exceptions

/**
 * Exception to be thrown to the exception related to the Passkey Authentication
 */
class PasskeyAuthenticationException (
    override val message: String?
): Exception(message) {
    companion object {
        /**
         * Passkey Authentication Exception TAG
         */
        private const val PASSKEY_AUTHENTICATION_EXCEPTION = "PasskeyAuthenticationException"

        /**
         * Passkey Authentication is not supported message
         */
        const val PASSKEY_AUTHENTICATION_NOT_SUPPORTED = "Passkey Authentication is not supported"

        /**
         * Passkey Authentication failed message
         */
        const val PASSKEY_AUTHENTICATION_FAILED = "Passkey Authentication failed"

        /**
         * Passkey Challenge String is empty message
         */
        const val PASSKEY_CHALLENGE_STRING_EMPTY = "Passkey Challenge String is empty"
    }

    override fun toString(): String {
        return "$PASSKEY_AUTHENTICATION_EXCEPTION: $message"
    }
}
