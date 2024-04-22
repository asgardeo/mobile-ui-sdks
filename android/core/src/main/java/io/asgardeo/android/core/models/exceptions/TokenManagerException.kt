package io.asgardeo.android.core.models.exceptions

import io.asgardeo.android.core.core.managers.token.TokenManager

/**
 * Exception to be thrown to the exception related to [TokenManager]
 */
class TokenManagerException(
    override val message: String?
) : Exception(message) {
    companion object {
        /**
         * Token manager exception TAG
         */
        const val TOKEN_MANAGER_EXCEPTION = "TokenManager Exception"

        /**
         * Invalid ID token message
         */
        const val INVALID_ID_TOKEN = "Invalid ID token"
    }

    override fun toString(): String {
        return "$TOKEN_MANAGER_EXCEPTION: $message"
    }
}
