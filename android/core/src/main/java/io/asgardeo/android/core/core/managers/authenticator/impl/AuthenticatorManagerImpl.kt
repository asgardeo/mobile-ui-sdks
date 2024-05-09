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

package io.asgardeo.android.core.core.managers.authenticator.impl

import io.asgardeo.android.core.core.managers.app_auth.impl.AppAuthManagerImpl
import io.asgardeo.android.core.core.managers.authenticator.AuthenticatorManager
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.autheniticator.AuthenticatorTypes
import io.asgardeo.android.core.models.autheniticator.authenticator_factory.AuthenticatorFactory
import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlowNotSuccess
import io.asgardeo.android.core.models.exceptions.AuthenticatorException
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
 * Authenticator manager implementation class
 * This class is responsible for handling the authenticator related operations.
 *
 * @param client The [OkHttpClient] instance.
 * @param authenticatorFactory The [AuthenticatorFactory] instance.
 * @param authenticatorManagerImplRequestBuilder The [AuthenticatorManagerImplRequestBuilder] instance.
 * @param authnUrl The authentication endpoint URL.
 */
internal class AuthenticatorManagerImpl private constructor(
    private val client: OkHttpClient,
    private val authenticatorFactory: AuthenticatorFactory,
    private val authenticatorManagerImplRequestBuilder: AuthenticatorManagerImplRequestBuilder,
    private val authnUrl: String
) : AuthenticatorManager {
    companion object {
        /**
         * Instance of the [AuthenticatorManagerImpl] class.
         */
        private var authenticatorManagerImplInstance =
            WeakReference<AuthenticatorManagerImpl?>(null)

        /**
         * Initialize the [AuthenticatorManagerImpl] class.
         *
         * @param client The [OkHttpClient] instance.
         * @param authenticatorFactory The [AuthenticatorFactory] instance.
         * @param authenticatorManagerImplRequestBuilder The [AuthenticatorManagerImplRequestBuilder] instance.
         * @param authnUrl The authentication endpoint URL.
         *
         * @return The [AppAuthManagerImpl] instance.
         */
        fun getInstance(
            client: OkHttpClient,
            authenticatorFactory: AuthenticatorFactory,
            authenticatorManagerImplRequestBuilder: AuthenticatorManagerImplRequestBuilder,
            authnUrl: String
        ): AuthenticatorManagerImpl {
            var authenticatorManagerImpl = authenticatorManagerImplInstance.get()
            if (authenticatorManagerImpl == null) {
                authenticatorManagerImpl = AuthenticatorManagerImpl(
                    client,
                    authenticatorFactory,
                    authenticatorManagerImplRequestBuilder,
                    authnUrl
                )
                authenticatorManagerImplInstance = WeakReference(authenticatorManagerImpl)
            }
            return authenticatorManagerImpl
        }
    }

    /**
     * Get full details of the authenticator.
     *
     * @param flowId Flow id of the authentication flow
     * @param authenticator Authenticator that is required to get the full details
     *
     * @return Authenticator with full details [Authenticator]
     *
     * @throws AuthenticatorException
     */
    override suspend fun getDetailsOfAuthenticator(
        flowId: String,
        authenticator: Authenticator
    ): Authenticator = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            if (authenticator.authenticator == AuthenticatorTypes.BASIC_AUTHENTICATOR.authenticatorType) {
                val detailedAuthenticator: Authenticator =
                    authenticatorFactory.getAuthenticator(
                        authenticator.authenticatorId,
                        authenticator.authenticator,
                        authenticator.idp,
                        authenticator.metadata,
                        authenticator.requiredParams
                    )

                continuation.resume(detailedAuthenticator)
            } else {
                val request: Request =
                    AuthenticatorManagerImplRequestBuilder.getAuthenticatorRequestBuilder(
                        authnUrl,
                        flowId,
                        authenticator.authenticatorId
                    )

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        val exception =
                            AuthenticatorException(
                                e.message,
                                authenticator.authenticator
                            )
                        continuation.resumeWithException(exception)
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            if (response.code == 200) {
                                val authenticationFlow: AuthenticationFlowNotSuccess =
                                    AuthenticationFlowNotSuccess.fromJson(
                                        response.body!!.string()
                                    )

                                if (authenticationFlow.nextStep.authenticators.size == 1) {
                                    val detailedAuthenticator: Authenticator =
                                        authenticatorFactory.getAuthenticator(
                                            authenticationFlow.nextStep.authenticators[0].authenticatorId,
                                            authenticationFlow.nextStep.authenticators[0].authenticator,
                                            authenticationFlow.nextStep.authenticators[0].idp,
                                            authenticationFlow.nextStep.authenticators[0].metadata,
                                            authenticationFlow.nextStep.authenticators[0].requiredParams
                                        )

                                    continuation.resume(detailedAuthenticator)
                                } else {
                                    val exception =
                                        AuthenticatorException(
                                            AuthenticatorException.AUTHENTICATOR_NOT_FOUND_OR_MORE_THAN_ONE,
                                            authenticator.authenticator
                                        )
                                    continuation.resumeWithException(exception)
                                }
                            } else {
                                // Throw an `AuthenticatorException` if the request does not return 200
                                val exception =
                                    AuthenticatorException(
                                        response.message,
                                        authenticator.authenticator,
                                        response.code.toString()
                                    )
                                continuation.resumeWithException(exception)
                            }
                        } catch (e: IOException) {
                            val exception =
                                AuthenticatorException(
                                    e.message,
                                    authenticator.authenticator
                                )
                            continuation.resumeWithException(exception)
                        }
                    }
                })
            }
        }
    }

    /**
     * Remove the instance of the [AuthenticatorManagerImpl]
     */
    override fun dispose() {
        authenticatorManagerImplInstance.clear()
    }
}
