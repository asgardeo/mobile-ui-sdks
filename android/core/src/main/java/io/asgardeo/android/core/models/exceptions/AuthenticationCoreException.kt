package io.asgardeo.android.core.models.exceptions

/**
 * Exception to be thrown to the exception related to the Authenticator
 *
 * @property message Message related to the exception
 */
class AuthenticationCoreException(
    override val message: String?
): Exception(message) {
    companion object {
        /**
         * Authenticator exception TAG
         */
        const val AUTHORIZATION_SERVICE_EXCEPTION = "Authorization Service Exception"

        /**
         * Message to be shown when authenticator is not initialized
         */
        const val AUTHORIZATION_SERVICE_NOT_INITIALIZED = "Authorization Service is not initialized"
    }

    override fun toString(): String {
        return "$AUTHORIZATION_SERVICE_EXCEPTION: $message"
    }

    /**
     * Print the exception
     */
    fun printException() {
        println(toString())
    }
}
