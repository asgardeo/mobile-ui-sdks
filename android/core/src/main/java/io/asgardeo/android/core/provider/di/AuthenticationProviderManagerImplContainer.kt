/*
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.asgardeo.android.core.provider.di

import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.core.core_types.native_authentication_handler.NativeAuthenticationHandlerCoreDef
import io.asgardeo.android.core.provider.provider_managers.authenticate_handler.AuthenticateHandlerProviderManager
import io.asgardeo.android.core.provider.provider_managers.authenticate_handler.impl.AuthenticateHandlerProviderManagerImpl
import io.asgardeo.android.core.provider.provider_managers.authentication.impl.AuthenticationProviderManagerImpl
import io.asgardeo.android.core.provider.provider_managers.authentication_state.AuthenticationStateProviderManager
import io.asgardeo.android.core.provider.provider_managers.authentication_state.impl.AuthenticationStateProviderManagerImpl

/**
 * Dependency Injection container for the [AuthenticationProviderManagerImpl] class.
 */
internal object AuthenticationProviderManagerImplContainer {

    /**
     * Get the instance of the [AuthenticationStateProviderManager].
     *
     * @param authenticationCore The [AuthenticationCoreDef] instance
     *
     * @return [AuthenticationStateProviderManager] instance
     */
    internal fun getAuthenticationStateProviderManager(authenticationCore: AuthenticationCoreDef)
            : AuthenticationStateProviderManager =
        AuthenticationStateProviderManagerImpl.getInstance(authenticationCore)

    /**
     * Get the instance of the [AuthenticateHandlerProviderManager].
     *
     * @param authenticationCore The [AuthenticationCoreDef] instance
     * @param nativeAuthenticationHandlerCore The [NativeAuthenticationHandlerCoreDef] instance
     *
     * @return [AuthenticateHandlerProviderManager] instance
     */
    internal fun getAuthenticationHandlerProviderManager(
        authenticationCore: AuthenticationCoreDef,
        nativeAuthenticationHandlerCore: NativeAuthenticationHandlerCoreDef
    ): AuthenticateHandlerProviderManager =
        AuthenticateHandlerProviderManagerImpl.getInstance(
            authenticationCore,
            nativeAuthenticationHandlerCore,
            AuthenticateHandlerProviderManagerImplContainer.getAuthenticateStateProviderManager(
                authenticationCore
            )
        )
}
