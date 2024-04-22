package io.asgardeo.android.core.models.exceptions

import io.asgardeo.android.core.core.managers.flow.FlowManager

/**
 * Exception to be thrown to the exception related to [FlowManager]
 */
class FlowManagerException(
    override val message: String?
) : Exception(message) {
    companion object {
        /**
         * Flow manager exception TAG
         */
        const val FLOW_MANAGER_EXCEPTION = "FlowManager Exception"

        /**
         * Message to be shown when authentication is not completed
         */
        const val AUTHENTICATION_NOT_COMPLETED =
            "Authentication is not completed. Response returned FAIL_INCOMPLETE"

        /**
         * Message to be shown when authentication is not completed due to an unknown error
         */
        const val AUTHENTICATION_NOT_COMPLETED_UNKNOWN =
            "Authentication is not completed. Unknown error occurred"
    }

    override fun toString(): String {
        return "$FLOW_MANAGER_EXCEPTION: $message"
    }
}
