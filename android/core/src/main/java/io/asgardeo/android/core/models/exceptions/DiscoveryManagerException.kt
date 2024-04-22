package io.asgardeo.android.core.models.exceptions

class DiscoveryManagerException (
    override val message: String?
) : Exception(message) {
    companion object {
        /**
         * Discovery manager exception TAG
         */
        const val DISCOVERY_MANAGER_EXCEPTION = "DiscoveryManager Exception"

        /**
         * Message to be shown when cannot discover endpoints
         */
        const val CANNOT_DISCOVER_ENDPOINTS = "Cannot discover endpointS"
    }

    override fun toString(): String {
        return "$DISCOVERY_MANAGER_EXCEPTION: $message"
    }
}
