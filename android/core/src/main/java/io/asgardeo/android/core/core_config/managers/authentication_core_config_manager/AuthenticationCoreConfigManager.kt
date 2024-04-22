package io.asgardeo.android.core.core_config.managers.authentication_core_config_manager

import io.asgardeo.android.core.core_config.AuthenticationCoreConfig

/**
 * Manager to update the [AuthenticationCoreConfig] based on the discovery response
 */
interface AuthenticationCoreConfigManager {
    /**
     * Get the updated [AuthenticationCoreConfig] based on the discovery response
     *
     * @param discoveryEndpoint Discovery endpoint
     * @param authenticationCoreConfig [AuthenticationCoreConfig] to be updated
     *
     * @return Updated [AuthenticationCoreConfig]
     */
    suspend fun getUpdatedAuthenticationCoreConfig(
        discoveryEndpoint: String?,
        authenticationCoreConfig: AuthenticationCoreConfig
    ): AuthenticationCoreConfig
}
