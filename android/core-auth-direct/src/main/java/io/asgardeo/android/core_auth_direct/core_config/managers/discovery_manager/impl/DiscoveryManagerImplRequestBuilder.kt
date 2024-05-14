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

package io.asgardeo.android.core_auth_direct.core_config.managers.discovery_manager.impl

import okhttp3.Request

/**
 * Builder function related to the [DiscoveryManagerImpl]
 */
object DiscoveryManagerImplRequestBuilder {

    /**
     * Build the request to call the discovery endpoint.
     *
     * @param discoveryUri Discovery endpoint
     *
     * @return [okhttp3.Request] to call the discovery endpoint
     */
    internal fun discoveryRequestBuilder(discoveryUri: String): Request {
        val requestBuilder: Request.Builder = Request.Builder().url(discoveryUri)
        requestBuilder.addHeader("Accept", "application/json")

        return requestBuilder.get().build()
    }
}
