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

package io.asgardeo.android.core_auth_direct.provider.di

import io.asgardeo.android.core_auth_direct.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core_auth_direct.provider.provider_managers.token.TokenProviderManager
import io.asgardeo.android.core_auth_direct.provider.provider_managers.token.impl.TokenProviderManagerImpl
import io.asgardeo.android.core_auth_direct.provider.provider_managers.user.impl.UserProviderManagerImpl

/**
 * Dependency Injection container for the [UserProviderManagerImpl] interface.
 */
internal object UserProviderManagerImplContainer {
    /**
     * Get the instance of the [TokenProviderManager].
     *
     * @param authenticationCore [AuthenticationCoreDef] instance
     *
     * @return [TokenProviderManager] instance
     */
    internal fun getTokenProviderManager(
        authenticationCore: AuthenticationCoreDef
    ): TokenProviderManager =
        TokenProviderManagerImpl.getInstance(authenticationCore)
}
