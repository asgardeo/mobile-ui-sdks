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

package io.asgardeo.android.core.core.core_types.authentication.impl

import android.content.Context
import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.core.di.AuthenticationCoreContainer
import io.asgardeo.android.core.core.managers.app_auth.AppAuthManager
import io.asgardeo.android.core.core.managers.authenticator.AuthenticatorManager
import io.asgardeo.android.core.core.managers.authn.AuthnManager
import io.asgardeo.android.core.core.managers.flow.FlowManager
import io.asgardeo.android.core.core.managers.logout.LogoutManager
import io.asgardeo.android.core.core.managers.token.TokenManager
import io.asgardeo.android.core.core.managers.user.UserManager
import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core_config.providers.authentication_core_config_provider.AuthenticationCoreConfigProvider
import io.asgardeo.android.core.models.auth_params.AuthParams
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlow
import io.asgardeo.android.core.models.exceptions.AppAuthManagerException
import io.asgardeo.android.core.models.exceptions.AuthenticationCoreException
import io.asgardeo.android.core.models.exceptions.LogoutException
import io.asgardeo.android.core.models.state.TokenState
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Authentication core class which has the core functionality of the Authenticator SDK.
 *
 * @property authenticationCoreConfigProvider Provider to get the updated [AuthenticationCoreConfig] based on the discovery response. [AuthenticationCoreConfigProvider]
 */
class AuthenticationCore private constructor(
    private val authenticationCoreConfigProvider: AuthenticationCoreConfigProvider
) : AuthenticationCoreDef {
    /**
     * Instance of the [AuthenticationCoreConfig] that will be used throughout the application
     */
    private val authenticationCoreConfig: AuthenticationCoreConfig by lazy {
        authenticationCoreConfigProvider.getUpdatedAuthenticationCoreConfig()
    }

    /**
     * Instance of the [AuthenticatorManager] that will be used throughout the application
     */
    private val authenticatorManager: AuthenticatorManager by lazy {
        AuthenticationCoreContainer.getAuthenticatorManagerInstance(authenticationCoreConfig)
    }

    /**
     * Instance of the [FlowManager] that will be used throughout the application
     */
    private val flowManager: FlowManager by lazy {
        AuthenticationCoreContainer.getFlowManagerInstance()
    }

    /**
     * Instance of the [AuthnManager] that will be used throughout the application
     */
    private val authnMangerInstance: AuthnManager by lazy {
        AuthenticationCoreContainer.getAuthMangerInstance(authenticationCoreConfig)
    }

    /**
     * Instance of the [AppAuthManager] that will be used throughout the application
     */
    private val appAuthManagerInstance: AppAuthManager by lazy {
        AuthenticationCoreContainer.getAppAuthManagerInstance(authenticationCoreConfig)
    }

    /**
     * Instance of the [UserManager] that will be used throughout the application
     */
    private val userManagerInstance: UserManager by lazy {
        AuthenticationCoreContainer.getUserManagerInstance(authenticationCoreConfig)
    }

    /**
     * Instance of the [LogoutManager] that will be used throughout the application
     */
    private val logoutManagerInstance: LogoutManager by lazy {
        AuthenticationCoreContainer.getLogoutManagerInstance(authenticationCoreConfig)
    }

    companion object {
        /**
         * Instance of the [AuthenticationCore] that will be used throughout the application
         */
        private var authenticationCoreInstance = WeakReference<AuthenticationCore?>(null)

        /**
         * Initialize the AuthenticationCore instance and return the instance.
         *
         * @param authenticationCoreConfigProvider Provider to get the updated [AuthenticationCoreConfig] based on the discovery response. [AuthenticationCoreConfigProvider]
         *
         * @return Initialized [AuthenticationCore] instance
         */
        fun getInstance(
            authenticationCoreConfigProvider: AuthenticationCoreConfigProvider
        ): AuthenticationCore {
            var authenticationCore = authenticationCoreInstance.get()
            if (authenticationCore == null ||
                authenticationCore.authenticationCoreConfigProvider != authenticationCoreConfigProvider
            ) {
                authenticationCore = AuthenticationCore(authenticationCoreConfigProvider)
                authenticationCoreInstance = WeakReference(authenticationCore)
            }
            return authenticationCore
        }
    }

    /**
     * Get the [TokenManager] instance.
     *
     * @param context The [Context] instance.
     *
     * @return [TokenManager] instance.
     */
    private fun getTokenManagerInstance(context: Context): TokenManager {
        return AuthenticationCoreContainer.getTokenManagerInstance(context)
    }

    /**
     * Authorize the application.
     * This method will call the authorization endpoint and get the authenticators available for the
     * first step in the authentication flow.
     *
     * @throws [AuthenticationCoreException] If the authorization fails
     * @throws [IOException] If the request fails due to a network error
     */
    override suspend fun authorize(): AuthenticationFlow? = authnMangerInstance.authorize()

    /**
     * Send the authentication parameters to the authentication endpoint and get the next step of the
     * authentication flow. If the authentication flow has only one step, this method will return
     * the success response of the authentication flow if the authentication is successful.
     *
     * @param authenticator Authenticator object of the selected authenticator
     * @param authenticatorParameters Authenticator parameters of the selected authenticator as a
     * [LinkedHashMap] with the key as the parameter name and the value as the parameter value
     *
     * @throws [AuthenticationCoreException] If the authentication fails
     * @throws [IOException] If the request fails due to a network error
     *
     * @return [AuthenticationFlow] with the next step of the authentication flow
     */
    override suspend fun authn(
        authenticator: Authenticator,
        authenticatorParameters: LinkedHashMap<String, String>
    ): AuthenticationFlow? = authnMangerInstance.authn(authenticator, authenticatorParameters)

    /**
     * Get the authenticator details of the given authenticator.
     * This should call before authenticating with the any authenticator.
     *
     * @param authenticator Authenticator object of the selected authenticator
     *
     * @return Authenticator details [AuthParams]
     */
    override suspend fun getDetailsOfAuthenticator(
        authenticator: Authenticator,
    ): Authenticator {
        val flowId: String = flowManager.getFlowId()

        return authenticatorManager.getDetailsOfAuthenticator(flowId, authenticator)
    }

    /**
     * Exchange the authorization code for the access token.
     *
     * @param authorizationCode Authorization code
     * @param context Context of the application
     *
     * @throws [AppAuthManagerException] If the token request fails.
     *
     * @return token state [TokenState]
     */
    override suspend fun exchangeAuthorizationCode(
        authorizationCode: String,
        context: Context,
    ): TokenState? = appAuthManagerInstance.exchangeAuthorizationCode(
        authorizationCode,
        context
    )

    /**
     * Perform the refresh token grant.
     *
     * @param context Context of the application
     * @param tokenState The [TokenState] instance.
     *
     * @throws [AppAuthManagerException] If the token request fails.
     *
     * @return Updated [TokenState] instance.
     */
    override suspend fun performRefreshTokenGrant(
        context: Context,
        tokenState: TokenState,
    ): TokenState? = appAuthManagerInstance.performRefreshTokenGrant(context, tokenState)

    /**
     * Perform an action with the tokens. If the token is expired, it will perform the refresh the
     * tokens, and then perform the action. This will also update the token in the data store
     * as well.
     *
     * @param context The [Context] instance.
     * @param tokenState The [TokenState] instance.
     * @param action The action to perform.
     *
     * @return Updated [TokenState] instance.
     */
    override suspend fun performAction(
        context: Context,
        tokenState: TokenState,
        action: suspend (String?, String?) -> Unit
    ): TokenState? =
        appAuthManagerInstance.performAction(context, tokenState, action)

    /**
     * Save the [TokenState] to the data store.
     *
     * @param context Context of the application
     * @param tokenState The [TokenState] instance.
     */
    override suspend fun saveTokenState(context: Context, tokenState: TokenState): Unit? =
        getTokenManagerInstance(context).saveTokenState(tokenState)

    /**
     * Get the [TokenState] from the data store.
     *
     * @param context Context of the application
     */
    override suspend fun getTokenState(context: Context): TokenState? =
        getTokenManagerInstance(context).getTokenState()

    /**
     * Get the access token from the token data store.
     *
     * @param context Context of the application
     *
     * @return The access token [String]
     */
    override suspend fun getAccessToken(context: Context): String? =
        getTokenManagerInstance(context).getAccessToken()

    /**
     * Get the refresh token from the token data store.
     *
     * @param context Context of the application
     *
     * @return The refresh token [String]
     */
    override suspend fun getRefreshToken(context: Context): String? =
        getTokenManagerInstance(context).getRefreshToken()

    /**
     * Get the ID token from the token data store.
     *
     * @param context Context of the application
     *
     * @return The ID token [String]
     */
    override suspend fun getIDToken(context: Context): String? =
        getTokenManagerInstance(context).getIDToken()

    /**
     * Get the decoded ID token
     *
     * @param context Context of the application
     * @param idToken The ID token
     *
     * @return The decoded ID token [String]
     */
    override fun getDecodedIDToken(context: Context, idToken: String): LinkedHashMap<String, Any> =
        getTokenManagerInstance(context).getDecodedIDToken(idToken)

    /**
     * Get the access token expiration time from the token data store.
     *
     * @return The access token expiration time [Long]
     */
    override suspend fun getAccessTokenExpirationTime(context: Context): Long? =
        getTokenManagerInstance(context).getAccessTokenExpirationTime()

    /**
     * Get the scope from the token data store.
     *
     * @param context Context of the application
     *
     * @return The scope [String]
     */
    override suspend fun getScope(context: Context): String? =
        getTokenManagerInstance(context).getScope()

    /**
     * Clear the tokens from the token data store.
     *
     * @param context Context of the application
     */
    override suspend fun clearTokens(context: Context): Unit? =
        getTokenManagerInstance(context).clearTokens()

    /**
     * Validate the access token, by checking the expiration time of the access token, and
     * by checking if the access token is null or empty.
     *
     * **Here we are not calling the introspection endpoint to validate the access token!.
     * We are checking the expiration time of the access token and
     * if the access token is null or empty.**
     *
     * @param context Context of the application
     *
     * @return `true` if the access token is valid, `false` otherwise.
     */
    override suspend fun validateAccessToken(context: Context): Boolean? =
        getTokenManagerInstance(context).validateAccessToken()

    /**
     * Get the basic user information of the authenticated.
     *
     * @param accessToken Access token to authorize the request
     *
     * @return User details as a [LinkedHashMap]
     */
    override suspend fun getBasicUserInfo(accessToken: String?): LinkedHashMap<String, Any>? =
        userManagerInstance.getBasicUserInfo(accessToken)

    /**
     * Logout the user from the application.
     *
     * @param idToken Id token of the user
     *
     * @throws [LogoutException] If the logout fails
     * @throws [IOException] If the request fails due to a network error
     */
    override suspend fun logout(idToken: String) {
        logoutManagerInstance.logout(idToken)
    }
}
