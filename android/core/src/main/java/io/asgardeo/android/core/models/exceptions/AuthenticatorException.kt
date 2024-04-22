package io.asgardeo.android.core.models.exceptions

import io.asgardeo.android.core.models.autheniticator.Authenticator

/**
 * Exception to be thrown to the exception related to an [Authenticator]
 *
 * @property message Message related to the exception
 * @property authenticator Authenticator type
 * @property code Code of the exception
 */
class AuthenticatorException(
    override val message: String?,
    private val authenticator: String?,
    private val code: String? = null
): Exception(message) {
    companion object {
        /**
         * Authenticator exception TAG
         */
        const val AUTHENTICATOR_EXCEPTION = "Authenticator Exception"

        /**
         * Authenticator not found or more than one authenticator found
         */
        const val AUTHENTICATOR_NOT_FOUND_OR_MORE_THAN_ONE =
            "Authenticator not found or more than one authenticator found"
    }

    override fun toString(): String {
        val codeString: String = if (code != null) "$code " else ""

        return "$AUTHENTICATOR_EXCEPTION: $authenticator $codeString $message"
    }

    /**
     * Print the exception
     */
    fun printException() {
        println(toString())
    }
}
