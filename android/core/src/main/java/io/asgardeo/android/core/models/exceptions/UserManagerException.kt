package io.asgardeo.android.core.models.exceptions

class UserManagerException(
    override val message: String?
) : Exception(message) {
    companion object {
        /**
         * User manager exception TAG
         */
        const val USER_MANAGER_EXCEPTION = "UserManager Exception"

        /**
         * Message to be shown when user is not found
         */
        const val USER_NOT_FOUND = "User not found"
    }

    override fun toString(): String {
        return "$USER_MANAGER_EXCEPTION: $message"
    }
}