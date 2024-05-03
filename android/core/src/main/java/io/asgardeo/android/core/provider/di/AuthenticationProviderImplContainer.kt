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

package io.asgardeo.android.core.provider.di

import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.core.core_types.native_authentication_handler.NativeAuthenticationHandlerCoreDef
import io.asgardeo.android.core.provider.provider_managers.authentication.AuthenticationProviderManager
import io.asgardeo.android.core.provider.provider_managers.authentication.impl.AuthenticationProviderManagerImpl
import io.asgardeo.android.core.provider.provider_managers.user.UserProviderManager
import io.asgardeo.android.core.provider.provider_managers.user.impl.UserProviderManagerImpl
import io.asgardeo.android.core.provider.providers.authentication.impl.AuthenticationProviderImpl

/**
 * Dependency Injection container for the [AuthenticationProviderImpl] class.
 */
internal object AuthenticationProviderImplContainer {
    /**
     * Get the instance of the [AuthenticationProviderManager].
     *
     * @param authenticationCore [AuthenticationCoreDef] instance
     * @param nativeAuthenticationHandlerCore [NativeAuthenticationHandlerCoreDef] instance
     *
     * @return [AuthenticationProviderManager] instance
     */
    internal fun getAuthenticationProviderManager(
        authenticationCore: AuthenticationCoreDef,
        nativeAuthenticationHandlerCore: NativeAuthenticationHandlerCoreDef
    ): AuthenticationProviderManager =
        AuthenticationProviderManagerImpl.getInstance(
            authenticationCore,
            nativeAuthenticationHandlerCore,
            AuthenticationProviderManagerImplContainer.getAuthenticationStateProviderManager(
                authenticationCore
            ),
            AuthenticationProviderManagerImplContainer.getAuthenticationHandlerProviderManager(
                authenticationCore,
                nativeAuthenticationHandlerCore
            )
        )

    /**
     * Get the instance of the [UserProviderManager].
     *
     * @param authenticationCore [AuthenticationCoreDef] instance
     *
     * @return [UserProviderManager] instance
     */
    internal fun getUserProviderManager(
        authenticationCore: AuthenticationCoreDef
    ): UserProviderManager =
        UserProviderManagerImpl.getInstance(
            authenticationCore,
            UserProviderManagerImplContainer.getTokenProviderManager(authenticationCore)
        )
}
