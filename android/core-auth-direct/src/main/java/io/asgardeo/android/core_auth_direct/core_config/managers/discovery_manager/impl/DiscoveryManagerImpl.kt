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

import android.util.Log
import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core_auth_direct.core_config.managers.discovery_manager.DiscoveryManager
import io.asgardeo.android.core_auth_direct.models.exceptions.AuthnManagerException
import io.asgardeo.android.core_auth_direct.models.exceptions.DiscoveryManagerException
import io.asgardeo.android.core_auth_direct.util.JsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Implementation of [DiscoveryManager]
 *
 * @param client OkHttpClient instance to handle network calls
 * @param discoveryManagerImplRequestBuilder Request builder class to build the requests
 */
class DiscoveryManagerImpl private constructor(
    private val client: OkHttpClient,
    private val discoveryManagerImplRequestBuilder: DiscoveryManagerImplRequestBuilder
) : DiscoveryManager {

    companion object {
        private const val TAG = "DiscoveryManager"

        /**
         * Instance of the [DiscoveryManagerImpl] that will be used throughout the application
         */
        private var discoveryManagerImplInstance: WeakReference<DiscoveryManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [DiscoveryManagerImpl] instance and return the instance.
         *
         * @param client OkHttpClient instance to handle network calls
         * @param discoveryManagerImplRequestBuilder Request builder class to build the requests
         *
         * @return Initialized [DiscoveryManagerImpl] instance
         */
        fun getInstance(
            client: OkHttpClient,
            discoveryManagerImplRequestBuilder: DiscoveryManagerImplRequestBuilder
        ): DiscoveryManagerImpl {
            var discoveryManagerImpl = discoveryManagerImplInstance.get()
            if (discoveryManagerImpl == null) {
                discoveryManagerImpl = DiscoveryManagerImpl(
                    client,
                    discoveryManagerImplRequestBuilder
                )
                discoveryManagerImplInstance = WeakReference(discoveryManagerImpl)
            }
            return discoveryManagerImpl
        }
    }

    /**
     * Call the discovery endpoint and return the response.
     *
     * @param discoveryEndpoint Discovery endpoint
     *
     * @return Discovery response as a [JsonNode]
     */
    override suspend fun callDiscoveryEndpoint(discoveryEndpoint: String): JsonNode =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request: Request = discoveryManagerImplRequestBuilder.discoveryRequestBuilder(
                    discoveryEndpoint
                )

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(
                            TAG,
                            "${e.message.toString()} hence setting the values based on the base url derived from the discovery endpoint. ${e.stackTraceToString()}",
                        )
                        continuation.resumeWithException(e)
                    }

                    @Throws(IOException::class, AuthnManagerException::class)
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            if (response.code == 200) {
                                continuation.resume(
                                    JsonUtil.getJsonObject(response.body!!.string())
                                )
                            } else {
                                val exception = DiscoveryManagerException(
                                    if (response.message != "") response.message
                                    else DiscoveryManagerException.CANNOT_DISCOVER_ENDPOINTS
                                )

                                Log.e(
                                    TAG,
                                    "${exception.message.toString()} hence setting the values based on the base url derived from the discovery endpoint. ${exception.stackTraceToString()}",
                                )
                                continuation.resumeWithException(exception)
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "${e.message.toString()}. ${e.stackTraceToString()}",
                            )
                            continuation.resumeWithException(e)
                        }
                    }
                })
            }
        }
}
