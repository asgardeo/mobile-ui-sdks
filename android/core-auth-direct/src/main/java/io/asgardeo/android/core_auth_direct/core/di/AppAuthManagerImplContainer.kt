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

package io.asgardeo.android.core_auth_direct.core.di

import android.net.Uri
import io.asgardeo.android.core_auth_direct.core.managers.app_auth.impl.AppAuthManagerImpl
import io.asgardeo.android.core_auth_direct.models.http_client.LessSecureHttpClient
import io.asgardeo.android.core_auth_direct.models.http_client.http_client_builder.HttpClientBuilder
import net.openid.appauth.AuthorizationServiceConfiguration
import okhttp3.OkHttpClient

/**
 * Dependency Injection container for the [AppAuthManagerImpl] class.
 */
internal object AppAuthManagerImplContainer {

    /**
     * Returns an instance of the [OkHttpClient] class, based on the given parameters.
     *
     * @param isDevelopment The flag to check whether the app is in development mode or not.
     * If true, the [LessSecureHttpClient] instance will be returned. Otherwise, the default
     * [OkHttpClient] instance will be returned. Default value is `false`. It is not recommended to
     * keep this value as `true` in production environment.
     *
     * @return [OkHttpClient] instance.
     */
    internal fun getCustomTrustClient(isDevelopment: Boolean?): OkHttpClient =
        HttpClientBuilder.getHttpClientInstance(isDevelopment)

    /**
     * Returns the client ID passed as a parameter.
     *
     * @param clientId The client ID.
     *
     * @return The client ID.
     */
    internal fun getClientId(clientId: String): String = clientId

    /**
     * Returns the redirect URI passed as a parameter.
     *
     * @param redirectUri The redirect URI.
     *
     * @return The redirect URI.
     */
    internal fun getRedirectUri(redirectUri: String): Uri = Uri.parse(redirectUri)

    /**
     * Returns the [AuthorizationServiceConfiguration] instance, based on the given parameters.
     *
     * @param authorizeEndpoint The authorize endpoint.
     * @param tokenEndpoint The token endpoint.
     *
     * @return [AuthorizationServiceConfiguration] instance.
     */
    internal fun getServiceConfig(
        authorizeEndpoint: String,
        tokenEndpoint: String
    ): AuthorizationServiceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(authorizeEndpoint),
        Uri.parse(tokenEndpoint)
    )
}
