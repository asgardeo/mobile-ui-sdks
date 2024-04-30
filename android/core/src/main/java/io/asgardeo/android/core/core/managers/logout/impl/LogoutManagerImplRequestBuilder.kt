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

package io.asgardeo.android.core.core.managers.logout.impl

import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody

/**
 * Request builder for the [LogoutManagerImpl]
 *
 * This class is responsible for building the request for the [LogoutManagerImpl]
 */
object LogoutManagerImplRequestBuilder {
    /**
     * Build the request to logout the user.
     * This request will be used to logout the user from the application.
     *
     * @param logoutUri Logout endpoint
     * @param clientId Client id of the application
     * @param idToken Id token of the user
     *
     * @return [okhttp3.Request] to logout the user
     */
    internal fun logoutRequestBuilder(
        logoutUri: String,
        clientId: String,
        idToken: String
    ): Request {
        val formBody: RequestBody = FormBody.Builder()
            .add("response_mode", "direct")
            .add("client_id", clientId)
            .add("id_token_hint", idToken)
            .build()

        val requestBuilder: Request.Builder = Request.Builder().url(logoutUri)
        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded")

        return requestBuilder.post(formBody).build()
    }
}
