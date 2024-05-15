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

package io.asgardeo.android.core_auth_direct.core.managers.app_auth.impl

import android.net.Uri
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenRequest

/**
 * Request builder functions related to the [AppAuthManagerImpl]
 */
object AppAuthManagerImplRequestBuilder {
    /**
     * Use to get the [TokenRequest.Builder] instance.
     *
     * @return The [TokenRequest.Builder] instance.
     */
    private fun getTokenRequestBuilder(
        serviceConfig: AuthorizationServiceConfiguration,
        clientId: String
    ): TokenRequest.Builder = TokenRequest.Builder(
        serviceConfig,
        clientId
    )

    /**
     * Use to get the [TokenRequest] instance to exchange the authorization code for the access token.
     *
     * @param serviceConfig The [AuthorizationServiceConfiguration] instance.
     * @param clientId The client id.
     * @param authorizationCode The authorization code.
     * @param redirectUri The redirect uri.
     *
     * @return The [TokenRequest] instance.
     */
    internal fun getExchangeAuthorizationCodeRequestBuilder(
        serviceConfig: AuthorizationServiceConfiguration,
        clientId: String,
        authorizationCode: String,
        redirectUri: Uri
    ): TokenRequest =
        getTokenRequestBuilder(serviceConfig, clientId)
            .setAuthorizationCode(authorizationCode)
            .setClientId(clientId)
            .setRedirectUri(redirectUri)
            .build()

    /**
     * Use to get the [TokenRequest] instance to refresh the access token.
     *
     * @param serviceConfig The [AuthorizationServiceConfiguration] instance.
     * @param clientId The client id.
     * @param refreshToken The refresh token.
     * @param redirectUri The redirect uri.
     *
     * @return The [TokenRequest] instance.
     */
    internal fun getRefreshTokenRequestBuilder(
        serviceConfig: AuthorizationServiceConfiguration,
        clientId: String,
        refreshToken: String,
        redirectUri: Uri
    ): TokenRequest = getTokenRequestBuilder(serviceConfig, clientId)
        .setGrantType(GrantTypeValues.REFRESH_TOKEN)
        .setRedirectUri(redirectUri)
        .setRefreshToken(refreshToken)
        .build()

}
