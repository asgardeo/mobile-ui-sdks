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

package io.asgardeo.android.core_auth_direct.core_config.providers.authentication_core_config_provider.impl

import io.asgardeo.android.core_auth_direct.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core_auth_direct.core_config.managers.authentication_core_config_manager.AuthenticationCoreConfigManager
import io.asgardeo.android.core_auth_direct.core_config.providers.authentication_core_config_provider.AuthenticationCoreConfigProvider
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference

/**
 * Implementation of [AuthenticationCoreConfigProvider]
 *
 * @param authenticationCoreConfigManager Manager to update the [AuthenticationCoreConfig] based on the discovery response
 */
class AuthenticationCoreConfigProviderImpl private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig,
    private val authenticationCoreConfigManager: AuthenticationCoreConfigManager
) : AuthenticationCoreConfigProvider {

    companion object {
        /**
         * Instance of the [AuthenticationCoreConfigProviderImpl] that will be used throughout the application
         */
        private var authenticationCoreConfigProviderImplInstance: WeakReference<AuthenticationCoreConfigProviderImpl> =
            WeakReference(null)

        /**
         * Initialize the [AuthenticationCoreConfigProviderImpl] instance and return the instance.
         *
         * @param authenticationCoreConfigManager Manager to update the [AuthenticationCoreConfig] based on the discovery response
         *
         * @return The [AuthenticationCoreConfigProviderImpl] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig,
            authenticationCoreConfigManager: AuthenticationCoreConfigManager
        ): AuthenticationCoreConfigProviderImpl {
            var authenticationCoreConfigProviderImpl =
                authenticationCoreConfigProviderImplInstance.get()
            if (authenticationCoreConfigProviderImpl == null) {
                authenticationCoreConfigProviderImpl = AuthenticationCoreConfigProviderImpl(
                    authenticationCoreConfig,
                    authenticationCoreConfigManager
                )
                authenticationCoreConfigProviderImplInstance =
                    WeakReference(authenticationCoreConfigProviderImpl)
            }
            return authenticationCoreConfigProviderImpl
        }
    }

    /**
     * Get the updated [AuthenticationCoreConfig] based on the discovery response
     *
     * @return Updated [AuthenticationCoreConfig]
     */
    override fun getUpdatedAuthenticationCoreConfig(): AuthenticationCoreConfig {
        return runBlocking {
            authenticationCoreConfigManager
                .getUpdatedAuthenticationCoreConfig(
                    authenticationCoreConfig.getDiscoveryEndpoint(),
                    authenticationCoreConfig
                )
        }
    }
}
