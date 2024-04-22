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

package io.asgardeo.android.core.core.di

import android.content.Context
import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core.core_types.authentication.impl.AuthenticationCore
import io.asgardeo.android.core.core.managers.app_auth.AppAuthManager
import io.asgardeo.android.core.core.managers.app_auth.impl.AppAuthManagerImpl
import io.asgardeo.android.core.core.managers.authenticator.AuthenticatorManager
import io.asgardeo.android.core.core.managers.authenticator.impl.AuthenticatorManagerImpl
import io.asgardeo.android.core.core.managers.authn.AuthnManager
import io.asgardeo.android.core.core.managers.authn.impl.AuthnManagerImpl
import io.asgardeo.android.core.core.managers.flow.FlowManager
import io.asgardeo.android.core.core.managers.flow.impl.FlowManagerImpl
import io.asgardeo.android.core.core.managers.logout.LogoutManager
import io.asgardeo.android.core.core.managers.logout.impl.LogoutManagerImpl
import io.asgardeo.android.core.core.managers.token.TokenManager
import io.asgardeo.android.core.core.managers.token.TokenManagerFactory
import io.asgardeo.android.core.core.managers.user.UserManager
import io.asgardeo.android.core.core.managers.user.impl.UserManagerImpl

/**
 * Dependency Injection container for the [AuthenticationCore]
 */
internal object AuthenticationCoreContainer {

    /**
     * Returns an instance of the [AuthnManager] object, based on the given parameters.
     *
     * @property authenticationCoreConfig The [AuthenticationCoreConfig] instance.
     *
     * @return [AuthnManager] instance.
     */
    internal fun getAuthMangerInstance(
        authenticationCoreConfig: AuthenticationCoreConfig,
    ): AuthnManager = AuthnManagerImpl.getInstance(
        authenticationCoreConfig,
        AuthnManagerImplContainer.getClient(
            authenticationCoreConfig.getIsDevelopment()
        ),
        AuthnManagerImplContainer.getAuthenticationCoreRequestBuilder(),
        AuthnManagerImplContainer.getFlowManager()
    )

    /**
     * Returns an instance of the [AppAuthManager] object, based on the given parameters.
     *
     * @property authenticationCoreConfig The [AuthenticationCoreConfig] instance.
     *
     * @return [AppAuthManager] instance.
     */
    internal fun getAppAuthManagerInstance(
        authenticationCoreConfig: AuthenticationCoreConfig
    ): AppAuthManager = AppAuthManagerImpl.getInstance(
        AppAuthManagerImplContainer.getCustomTrustClient(
            authenticationCoreConfig.getIsDevelopment()
        ), AppAuthManagerImplContainer.getClientId(
            authenticationCoreConfig.getClientId()
        ), AppAuthManagerImplContainer.getRedirectUri(
            authenticationCoreConfig.getRedirectUri()
        ), AppAuthManagerImplContainer.getServiceConfig(
            authenticationCoreConfig.getAuthorizeUrl(), authenticationCoreConfig.getTokenUrl()
        )
    )

    /**
     * Returns an instance of the [AuthenticatorManager] object, based on the given parameters.
     *
     * @property authenticationCoreConfig The [AuthenticationCoreConfig] instance.
     *
     * @return [AuthenticatorManager] instance.
     */
    internal fun getAuthenticatorManagerInstance(
        authenticationCoreConfig: AuthenticationCoreConfig
    ): AuthenticatorManager = AuthenticatorManagerImpl.getInstance(
        AuthenticatorManagerImplContainer.getClient(
            authenticationCoreConfig.getIsDevelopment()
        ),
        AuthenticatorManagerImplContainer.getAuthenticatorFactory(),
        AuthenticatorManagerImplContainer.getAuthenticatorManagerImplRequestBuilder(),
        AuthenticatorManagerImplContainer.getAuthnUrl(
            authenticationCoreConfig.getAuthnUrl()
        )
    )

    /**
     * Returns an instance of the [FlowManager] object, based on the given parameters.
     *
     * @return [FlowManager] instance.
     */
    internal fun getFlowManagerInstance(): FlowManager = FlowManagerImpl.getInstance()

    /**
     * Returns an instance of the [TokenManager] object, based on the given parameters.
     *
     * @property context The [Context] instance.
     *
     * @return [TokenManager] instance.
     */
    internal fun getTokenManagerInstance(context: Context): TokenManager =
        TokenManagerFactory.getTokenManager(context)

    /**
     * Returns an instance of the [UserManager] object, based on the given parameters.
     *
     * @property authenticationCoreConfig The [AuthenticationCoreConfig] instance.
     *
     * @return [UserManager] instance.
     */
    internal fun getUserManagerInstance(
        authenticationCoreConfig: AuthenticationCoreConfig,
    ): UserManager = UserManagerImpl.getInstance(
        authenticationCoreConfig,
        UserManagerImplContainer.getClient(
            authenticationCoreConfig.getIsDevelopment()
        ),
        UserManagerImplContainer.getUserManagerImplRequestBuilder()
    )

    /**
     * Returns an instance of the [LogoutManager] object, based on the given parameters.
     */
    internal fun getLogoutManagerInstance(
        authenticationCoreConfig: AuthenticationCoreConfig
    ): LogoutManager = LogoutManagerImpl.getInstance(
        authenticationCoreConfig,
        LogoutManagerImplContainer.getClient(authenticationCoreConfig.getIsDevelopment()),
        LogoutManagerImplContainer.getLogoutManagerImplRequestBuilder()
    )
}
