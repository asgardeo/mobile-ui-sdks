package io.asgardeo.android.core.core_config.providers.authentication_core_config_provider

import io.asgardeo.android.core.core_config.AuthenticationCoreConfig

/**
 * Provider to update the [AuthenticationCoreConfig] based on the discovery response
 */
interface AuthenticationCoreConfigProvider {
    /**
     * Get the updated [AuthenticationCoreConfig] based on the discovery response
     *
     * @return Updated [AuthenticationCoreConfig]
     */
     fun getUpdatedAuthenticationCoreConfig(): AuthenticationCoreConfig
}
