package io.asgardeo.android.core.models.flow_status

/**
 * Enum class for flow status
 *
 * @property flowStatus Flow status value
 */
enum class FlowStatus(val flowStatus: String) {
    /**
     * Flow status is fail and incomplete.
     */
    FAIL_INCOMPLETE("FAIL_INCOMPLETE"),

    /**
     * Flow status is incomplete.
     */
    INCOMPLETE("INCOMPLETE"),

    /**
     * Flow status is success.
     */
    SUCCESS("SUCCESS_COMPLETED"),
}