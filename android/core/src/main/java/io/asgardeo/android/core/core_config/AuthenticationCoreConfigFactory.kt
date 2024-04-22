package io.asgardeo.android.core.core_config

import com.fasterxml.jackson.databind.JsonNode

/**
 * Factory class to update the [AuthenticationCoreConfig]
 */
object AuthenticationCoreConfigFactory {
    /**
     * Update the [AuthenticationCoreConfig] based on the discovery response
     *
     * @param discoveryResult Discovery response
     * @param authenticationCoreConfig [AuthenticationCoreConfig] to be updated
     *
     * @return Updated [AuthenticationCoreConfig]
     */
    fun updateAuthenticationCoreConfig(
        discoveryResult: JsonNode?,
        authenticationCoreConfig: AuthenticationCoreConfig
    ): AuthenticationCoreConfig =
        authenticationCoreConfig.updateBasedOnDiscoveryResponse(discoveryResult)
}
