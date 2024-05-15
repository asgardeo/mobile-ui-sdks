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

package io.asgardeo.android.core_auth_direct.core.managers.user.impl

import okhttp3.Request

/**
 * UserManagerImplRequestBuilder is responsible for building the requests for the [UserManagerImpl]
 * to get the user details.
 */
object UserManagerImplRequestBuilder {
    /**
     * Build the request to get the user details.
     *
     * @param meUri User details endpoint
     * @param accessToken Access token to authorize the request
     *
     * @return [okhttp3.Request] to get the user details
     */
    internal fun getUserDetailsRequestBuilder(meUri: String, accessToken: String): Request {
        val requestBuilder: Request.Builder = Request.Builder().url(meUri)
        requestBuilder.addHeader("Accept", "application/scim+json")
        requestBuilder.addHeader("Authorization", "Bearer $accessToken")

        return requestBuilder.get().build()
    }
}
