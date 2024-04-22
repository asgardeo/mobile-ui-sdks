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

import io.asgardeo.android.core.core.managers.user.impl.UserManagerImpl
import io.asgardeo.android.core.core.managers.user.impl.UserManagerImplRequestBuilder
import io.asgardeo.android.core.models.http_client.LessSecureHttpClient
import io.asgardeo.android.core.models.http_client.http_client_builder.HttpClientBuilder
import okhttp3.OkHttpClient

/**
 * Dependency Injection container for the [UserManagerImpl] class.
 */
internal object UserManagerImplContainer {
    /**
     * Returns an instance of the [OkHttpClient] class, based on the given parameters.
     *
     * @property isDevelopment The flag to check whether the app is in development mode or not.
     * If true, the [LessSecureHttpClient] instance will be returned. Otherwise, the default
     * [OkHttpClient] instance will be returned. Default value is `false`. It is not recommended to
     * keep this value as `true` in production environment.
     *
     * @return [OkHttpClient] instance.
     */
    internal fun getClient(isDevelopment: Boolean?): OkHttpClient =
        HttpClientBuilder.getHttpClientInstance(isDevelopment)

    /**
     * Returns an instance of the [UserManagerImplRequestBuilder] class.
     *
     * @return [UserManagerImplRequestBuilder] instance.
     */
    internal fun getUserManagerImplRequestBuilder(): UserManagerImplRequestBuilder =
        UserManagerImplRequestBuilder
}
