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

package io.asgardeo.android.core_auth_direct.core.core_types.authentication

import android.content.Context
import io.asgardeo.android.core_auth_direct.models.auth_params.AuthParams
import io.asgardeo.android.core_auth_direct.models.autheniticator.Authenticator
import io.asgardeo.android.core_auth_direct.models.authentication_flow.AuthenticationFlow
import io.asgardeo.android.core_auth_direct.models.exceptions.AuthnManagerException
import io.asgardeo.android.core_auth_direct.models.state.TokenState
import java.io.IOException

/**
 * Authentication core class interface which has the core functionality of the Authenticator SDK.
 */
interface AuthenticationCoreDef {
    /**
     * Authorize the application.
     * This method will call the authorization endpoint and get the authenticators available for the
     * first step in the authentication flow.
     */
    suspend fun authorize(): AuthenticationFlow?

    /**
     * Send the authentication parameters to the authentication endpoint and get the next step of the
     * authentication flow. If the authentication flow has only one step, this method will return
     * the success response of the authentication flow if the authentication is successful.
     *
     * @param authenticator Authenticator of the selected authenticator
     * @param authenticatorParameters Authenticator parameters of the selected authenticator
     * as a LinkedHashMap<String, String> with the key as the parameter name and the value as the
     * parameter value
     *
     * @return [AuthenticationFlow] with the next step of the authentication flow
     */
    suspend fun authn(
        authenticator: Authenticator,
        authenticatorParameters: LinkedHashMap<String, String>
    ): AuthenticationFlow?

    /**
     * Get the authenticator details of the given authenticator.
     * This should call before authenticating with the any authenticator.
     *
     * @param authenticator Authenticator
     *
     * @return Authenticator details [AuthParams]
     */
    suspend fun getDetailsOfAuthenticator(authenticator: Authenticator): Authenticator?

    /**
     * Exchange the authorization code for the access token.
     *
     * @param authorizationCode Authorization code
     * @param context Context of the application
     *
     * @return token state [TokenState]
     */
    suspend fun exchangeAuthorizationCode(
        authorizationCode: String,
        context: Context,
    ): TokenState?

    /**
     * Perform the refresh token grant.
     *
     * @param context Context of the application
     * @param tokenState The [TokenState] instance.
     *
     *
     * @return updated [TokenState] instance.
     */
    suspend fun performRefreshTokenGrant(
        context: Context,
        tokenState: TokenState,
    ): TokenState?

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
    suspend fun performAction(
        context: Context,
        tokenState: TokenState,
        action: suspend (String?, String?) -> Unit
    ): TokenState?

    /**
     * Save the [TokenState] to the data store.
     *
     * @param context Context of the application
     * @param tokenState The [TokenState] instance.
     */
    suspend fun saveTokenState(context: Context, tokenState: TokenState): Unit?

    /**
     * Get the [TokenState] from the data store.
     *
     * @param context Context of the application
     *
     * @param context Context of the application
     */
    suspend fun getTokenState(context: Context): TokenState?

    /**
     * Get the access token from the token data store.
     *
     * @param context Context of the application
     *
     * @return The access token [String]
     */
    suspend fun getAccessToken(context: Context): String?

    /**
     * Get the refresh token from the token data store.
     *
     * @param context Context of the application
     *
     * @return The refresh token [String]
     */
    suspend fun getRefreshToken(context: Context): String?

    /**
     * Get the ID token from the token data store.
     *
     * @return The ID token [String]
     */
    suspend fun getIDToken(context: Context): String?

    /**
     * Get the decoded ID token
     *
     * @param context Context of the application
     * @param idToken The ID token
     *
     * @return The decoded ID token [String]
     */
    fun getDecodedIDToken(context: Context, idToken: String): LinkedHashMap<String, Any>

    /**
     * Get the access token expiration time from the token data store.
     *
     * @param context Context of the application
     *
     * @return The access token expiration time [Long]
     */
    suspend fun getAccessTokenExpirationTime(context: Context): Long?

    /**
     * Get the scope from the token data store.
     *
     * @param context Context of the application
     *
     * @return The scope [String]
     */
    suspend fun getScope(context: Context): String?

    /**
     * Clear the tokens from the token data store.
     *
     * @param context Context of the application
     */
    suspend fun clearTokens(context: Context): Unit?

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
    suspend fun validateAccessToken(context: Context): Boolean?

    /**
     * Get the basic user information of the authenticated.
     *
     * @param accessToken Access token to authorize the request
     *
     * @return User details as a [LinkedHashMap]
     */
    suspend fun getBasicUserInfo(accessToken: String?): LinkedHashMap<String, Any>?

    /**
     * Logout the user from the application.
     *
     * @param idToken Id token of the user
     *
     * @throws [AuthnManagerException] If the logout fails
     * @throws [IOException] If the request fails due to a network error
     */
    suspend fun logout(idToken: String)
}
