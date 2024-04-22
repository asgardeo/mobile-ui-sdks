package io.asgardeo.android.core.models.exceptions

import io.asgardeo.android.core.core.managers.logout.LogoutManager

/**
 * Exception to be thrown to the exception related to [LogoutManager]
 *
 * @property message Message related to the exception
 */
class LogoutException (
    override val message: String?
): Exception(message) {
    companion object {
        /**
         * Logout exception TAG
         */
        const val LOGOUT_EXCEPTION = "Logout Exception"
    }

    override fun toString(): String {
        return "$LOGOUT_EXCEPTION: $message"
    }
}
