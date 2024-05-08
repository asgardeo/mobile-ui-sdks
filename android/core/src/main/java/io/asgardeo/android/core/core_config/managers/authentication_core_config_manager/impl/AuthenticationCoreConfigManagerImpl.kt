/*
 *  Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

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
 * @param authenticationCoreConfigFactory Factory class to update the [AuthenticationCoreConfig]
 * @param discoveryManager Discovery manager to get the discovery response
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
        if (updatedAuthenticationCoreConfig == null || discoveryEndpoint != null) {
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
