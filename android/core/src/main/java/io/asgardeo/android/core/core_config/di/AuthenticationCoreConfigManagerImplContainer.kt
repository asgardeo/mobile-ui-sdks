package io.asgardeo.android.core.core_config.di

import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core_config.AuthenticationCoreConfigFactory
import io.asgardeo.android.core.core_config.managers.authentication_core_config_manager.impl.AuthenticationCoreConfigManagerImpl
import io.asgardeo.android.core.core_config.managers.discovery_manager.DiscoveryManager
import io.asgardeo.android.core.core_config.managers.discovery_manager.impl.DiscoveryManagerImpl

/**
 * Container for [AuthenticationCoreConfigManagerImpl]
 */
object AuthenticationCoreConfigManagerImplContainer {

    /**
     * Provide [AuthenticationCoreConfigFactory] instance
     *
     * @return [AuthenticationCoreConfigFactory] instance
     */
    internal fun getAuthenticationCoreConfigFactory(): AuthenticationCoreConfigFactory =
        AuthenticationCoreConfigFactory

    /**
     * Provide [DiscoveryManager] instance
     *
     * @param authenticationCoreConfig [AuthenticationCoreConfig] instance
     *
     * @return [DiscoveryManager] instance
     */
    internal fun getDiscoveryManager(
        authenticationCoreConfig: AuthenticationCoreConfig
    ): DiscoveryManager = DiscoveryManagerImpl.getInstance(
        client = DiscoveryManagerImplContainer
            .getClient(authenticationCoreConfig.getIsDevelopment()),
        discoveryManagerImplRequestBuilder = DiscoveryManagerImplContainer
            .getDiscoveryManagerImplRequestBuilder()
    )
}
