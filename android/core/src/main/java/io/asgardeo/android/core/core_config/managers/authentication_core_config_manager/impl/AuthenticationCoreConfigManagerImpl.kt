package io.asgardeo.android.core.core_config.managers.authentication_core_config_manager.impl

import android.util.Log
import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core_config.AuthenticationCoreConfigFactory
import io.asgardeo.android.core.core_config.managers.authentication_core_config_manager.AuthenticationCoreConfigManager
import io.asgardeo.android.core.core_config.managers.discovery_manager.DiscoveryManager
import java.lang.ref.WeakReference

/**
 * Implementation of [AuthenticationCoreConfigManager]
 *
 * @property authenticationCoreConfigFactory Factory class to update the [AuthenticationCoreConfig]
 * @property discoveryManager Discovery manager to get the discovery response
 */
class AuthenticationCoreConfigManagerImpl private constructor(
    private val authenticationCoreConfigFactory: AuthenticationCoreConfigFactory,
    private val discoveryManager: DiscoveryManager
) : AuthenticationCoreConfigManager {

    /**
     * Updated [AuthenticationCoreConfig] based on the discovery response
     */
    private var updatedAuthenticationCoreConfig: AuthenticationCoreConfig? = null

    companion object {
        /**
         * Instance of the [AuthenticationCoreConfigManagerImpl] that will be used throughout the application
         */
        private var authenticationCoreConfigManagerImplInstance: WeakReference<AuthenticationCoreConfigManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [AuthenticationCoreConfigManagerImpl] instance and return the instance.
         *
         * @param authenticationCoreConfigFactory Factory class to update the [AuthenticationCoreConfig]
         * @param discoveryManager Discovery manager to get the discovery response
         *
         * @return Initialized [AuthenticationCoreConfigManagerImpl] instance
         */
        fun getInstance(
            authenticationCoreConfigFactory: AuthenticationCoreConfigFactory,
            discoveryManager: DiscoveryManager
        ): AuthenticationCoreConfigManagerImpl {
            var authenticationCoreConfigManagerImpl =
                authenticationCoreConfigManagerImplInstance.get()
            if (authenticationCoreConfigManagerImpl == null) {
                authenticationCoreConfigManagerImpl = AuthenticationCoreConfigManagerImpl(
                    authenticationCoreConfigFactory,
                    discoveryManager
                )
                authenticationCoreConfigManagerImplInstance =
                    WeakReference(authenticationCoreConfigManagerImpl)
            }
            return authenticationCoreConfigManagerImpl
        }
    }

    /**
     * Get the updated [AuthenticationCoreConfig] based on the discovery response
     *
     * @param discoveryEndpoint Discovery endpoint
     * @param authenticationCoreConfig [AuthenticationCoreConfig] to be updated
     *
     * @return Updated [AuthenticationCoreConfig]
     */
    override suspend fun getUpdatedAuthenticationCoreConfig(
        discoveryEndpoint: String?,
        authenticationCoreConfig: AuthenticationCoreConfig
    ): AuthenticationCoreConfig {
        if (updatedAuthenticationCoreConfig == null || discoveryEndpoint == null) {
            var discoveryResponse: JsonNode?
            try {
                discoveryResponse = discoveryManager.callDiscoveryEndpoint(discoveryEndpoint!!)
                updatedAuthenticationCoreConfig =
                    authenticationCoreConfigFactory.updateAuthenticationCoreConfig(
                        discoveryResponse,
                        authenticationCoreConfig
                    )
            } catch (e: Exception) {
                Log.println(
                    Log.ERROR,
                    "DiscoveryManager",
                    e.message.toString()
                            + " "
                            + "hence setting the values based on the base url derived from the discovery endpoint"
                            + " "
                            + e.stackTraceToString()
                )
                discoveryResponse = null
            }

            updatedAuthenticationCoreConfig = updateAuthenticationCoreConfig(
                discoveryResponse,
                authenticationCoreConfig
            )
        }

        return updatedAuthenticationCoreConfig!!
    }

    /**
     * Update the [AuthenticationCoreConfig] based on the discovery response
     *
     * @param discoveryResult Discovery response
     * @param authenticationCoreConfig [AuthenticationCoreConfig] to be updated
     *
     * @return Updated [AuthenticationCoreConfig]
     */
    private fun updateAuthenticationCoreConfig(
        discoveryResult: JsonNode?,
        authenticationCoreConfig: AuthenticationCoreConfig
    ): AuthenticationCoreConfig = authenticationCoreConfigFactory.updateAuthenticationCoreConfig(
        discoveryResult,
        authenticationCoreConfig
    )
}
