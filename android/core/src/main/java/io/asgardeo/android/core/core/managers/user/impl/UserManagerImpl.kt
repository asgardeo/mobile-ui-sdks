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

package io.asgardeo.android.core.core.managers.user.impl

import android.util.Log
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core.core.managers.user.UserManager
import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.models.exceptions.UserManagerException
import io.asgardeo.android.core.util.JsonUtil
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
 * UserManagerImpl class is the implementation of the [UserManager] interface.
 * Which is used to handle the user details retrieval from the Identity Server.
 *
 * @param authenticationCoreConfig Configuration of the Identity Server [AuthenticationCoreConfig]
 * @param client OkHttpClient instance to handle network calls [OkHttpClient]
 * @param userManagerImplRequestBuilder Request builder class to build the requests [UserManagerImplRequestBuilder]
 */
class UserManagerImpl private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig,
    private val client: OkHttpClient,
    private val userManagerImplRequestBuilder: UserManagerImplRequestBuilder
) : UserManager {

    companion object {
        private const val TAG = "UserManager"

        /**
         * Instance of the [UserManagerImpl] that will be used throughout the application
         */
        private var userManagerImplInstance: WeakReference<UserManagerImpl> = WeakReference(null)

        /**
         * Initialize the [UserManagerImpl] instance and return the instance.
         *
         * @param authenticationCoreConfig Configuration of the Authenticator [AuthenticationCoreConfig]
         * @param client OkHttpClient instance to handle network calls
         * @param userManagerImplRequestBuilder Request builder class to build the requests
         *
         * @return Initialized [UserManagerImpl] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig,
            client: OkHttpClient,
            userManagerImplRequestBuilder: UserManagerImplRequestBuilder,
        ): UserManagerImpl {
            var userManagerImpl = userManagerImplInstance.get()
            if (userManagerImpl == null) {
                userManagerImpl = UserManagerImpl(
                    authenticationCoreConfig,
                    client,
                    userManagerImplRequestBuilder,
                )
                userManagerImplInstance = WeakReference(userManagerImpl)
            }
            return userManagerImpl
        }
    }

    /**
     * Get the basic user information of the authenticated.
     *
     * @param accessToken Access token to authorize the request
     *
     * @return User details as a [LinkedHashMap]
     */
    override suspend fun getBasicUserInfo(accessToken: String?): LinkedHashMap<String, Any>? =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request: Request = UserManagerImplRequestBuilder.getUserDetailsRequestBuilder(
                    authenticationCoreConfig.getUserinfoEndpoint(),
                    accessToken!!
                )

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(
                            TAG,
                            "${e.message.toString()}, getting user details failed. ${e.stackTraceToString()}",
                        )
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            if (response.code == 200) {
                                // reading the json from the response
                                val responseObject: JsonNode =
                                    JsonUtil.getJsonObject(response.body!!.string())

                                val stepTypeReference =
                                    object : TypeReference<LinkedHashMap<String, Any>>() {}

                                continuation.resume(
                                    JsonUtil.jsonNodeToObject(responseObject, stepTypeReference)
                                )
                            } else {
                                val exception = UserManagerException(
                                    UserManagerException.USER_MANAGER_EXCEPTION
                                )

                                Log.e(
                                    TAG,
                                    "${exception.message.toString()}, getting user details failed. ${exception.stackTraceToString()}",
                                )
                                continuation.resumeWithException(exception)
                            }
                        } catch (e: Exception) {
                            Log.e(
                                TAG,
                                "${e.message.toString()}, getting user details failed. ${e.stackTraceToString()}",
                            )
                            continuation.resumeWithException(e)
                        }
                    }
                })
            }
        }
}
