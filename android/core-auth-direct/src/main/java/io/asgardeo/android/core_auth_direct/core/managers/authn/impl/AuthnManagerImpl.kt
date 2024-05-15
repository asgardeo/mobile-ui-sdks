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

package io.asgardeo.android.core_auth_direct.core.managers.authn.impl

import android.util.Log
import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core_auth_direct.core.managers.authn.AuthnManager
import io.asgardeo.android.core_auth_direct.core.managers.flow.FlowManager
import io.asgardeo.android.core_auth_direct.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core_auth_direct.models.autheniticator.Authenticator
import io.asgardeo.android.core_auth_direct.models.authentication_flow.AuthenticationFlow
import io.asgardeo.android.core_auth_direct.models.exceptions.AuthenticatorException
import io.asgardeo.android.core_auth_direct.models.exceptions.AuthnManagerException
import io.asgardeo.android.core_auth_direct.models.exceptions.FlowManagerException
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
 * AuthnManagerImpl class is the implementation of the [AuthnManager] interface.
 * Which is used to handle the initial authorization and the authentication flow.
 *
 * @param authenticationCoreConfig Configuration of the Identity Server [AuthenticationCoreConfig]
 * @param client OkHttpClient instance to handle network calls [OkHttpClient]
 * @param authenticationCoreRequestBuilder Request builder class to build the requests [AuthnManagerImplRequestBuilder]
 */
internal class AuthnManagerImpl private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig,
    private val client: OkHttpClient,
    private val authenticationCoreRequestBuilder: AuthnManagerImplRequestBuilder,
    private val flowManager: FlowManager
) : AuthnManager {
    companion object {
        private const val TAG = "AuthnManager"

        /**
         * Instance of the [AuthnManagerImpl] that will be used throughout the application
         */
        private var authnManagerImplInstance: WeakReference<AuthnManagerImpl> = WeakReference(null)

        /**
         * Initialize the [AuthnManagerImpl] instance and return the instance.
         *
         * @param authenticationCoreConfig Configuration of the Authenticator [AuthenticationCoreConfig]
         * @param client OkHttpClient instance to handle network calls
         * @param authenticationCoreRequestBuilder Request builder class to build the requests
         * @param flowManager Flow manager instance to manage the state of the authorization flow
         *
         * @return Initialized [AuthnManagerImpl] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig,
            client: OkHttpClient,
            authenticationCoreRequestBuilder: AuthnManagerImplRequestBuilder,
            flowManager: FlowManager
        ): AuthnManagerImpl {
            var authnManagerImpl = authnManagerImplInstance.get()
            if (authnManagerImpl == null) {
                authnManagerImpl = AuthnManagerImpl(
                    authenticationCoreConfig,
                    client,
                    authenticationCoreRequestBuilder,
                    flowManager
                )
                authnManagerImplInstance = WeakReference(authnManagerImpl)
            }
            return authnManagerImpl
        }
    }

    /**
     * Authorize the application.
     * This method will call the authorization endpoint and get the authenticators available for the
     * first step in the authentication flow.
     *
     * @throws [AuthnManagerException] If the authorization fails
     * @throws [IOException] If the request fails due to a network error
     */
    override suspend fun authorize(): AuthenticationFlow? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val request: Request = authenticationCoreRequestBuilder.authorizeRequestBuilder(
                authenticationCoreConfig.getAuthorizeUrl(),
                authenticationCoreConfig.getClientId(),
                authenticationCoreConfig.getRedirectUri(),
                authenticationCoreConfig.getScope(),
                authenticationCoreConfig.getIntegrityToken()
            )

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(
                        TAG,
                        "${e.message.toString()}. ${e.stackTraceToString()}",
                    )
                    continuation.resumeWithException(e)
                }

                @Throws(IOException::class, AuthnManagerException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.code == 200) {
                            // reading the json from the response
                            val responseObject: JsonNode =
                                JsonUtil.getJsonObject(response.body!!.string())

                            // set the flow id of the authorization flow
                            flowManager.setFlowId(responseObject.get("flowId").asText())

                            // manage the state of the authorization flow
                            continuation.resume(
                                flowManager.manageStateOfAuthorizeFlow(responseObject)
                            )
                        } else {
                            // throw an `AuthnManagerException` if the request does not return 200
                            val exception = AuthnManagerException(response.message)

                            Log.e(
                                TAG,
                                "${exception.message.toString()}. ${exception.stackTraceToString()}",
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

    /**
     * Send the authentication parameters to the authentication endpoint and get the next step of the
     * authentication flow. If the authentication flow has only one step, this method will return
     * the success response of the authentication flow if the authentication is successful.
     *
     * @param authenticator Detailed object of the selected authenticator
     * @param authenticatorParameters Authenticator parameters of the selected authenticator
     * as a LinkedHashMap<String, String> with the key as the parameter name and the value as the
     * parameter value
     *
     * @throws [AuthnManagerException] If the authentication fails
     * @throws [AuthenticatorException] If the authenticator is not valid
     * @throws [FlowManagerException] If the flow is incomplete
     * @throws [IOException] If the request fails due to a network error
     *
     * @return [AuthenticationFlow] with the next step of the authentication flow
     *
     * TODO: In the AuthnManager class we can use retrofit to make the network calls.
     */
    override suspend fun authn(
        authenticator: Authenticator,
        authenticatorParameters: LinkedHashMap<String, String>
    ): AuthenticationFlow? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val request: Request = authenticationCoreRequestBuilder.authenticateRequestBuilder(
                authenticationCoreConfig.getAuthnUrl(),
                flowManager.getFlowId(),
                authenticator,
                authenticatorParameters
            )

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(
                        TAG,
                        "${e.message.toString()}. ${e.stackTraceToString()}",
                    )
                    continuation.resumeWithException(e)
                }

                @Throws(
                    IOException::class,
                    AuthnManagerException::class,
                    AuthenticatorException::class
                )
                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.code == 200) {
                            // reading the json from the response
                            val responseObject: JsonNode =
                                JsonUtil.getJsonObject(response.body!!.string())

                            // manage the state of the authorization flow
                            continuation.resume(
                                flowManager.manageStateOfAuthorizeFlow(responseObject)
                            )
                        } else {
                            // Throw an [AuthnManagerException] if the request does not return 200 response.message
                            val exception = AuthnManagerException(response.message)

                            Log.e(
                                TAG,
                                "${exception.message.toString()}. ${exception.stackTraceToString()}",
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
