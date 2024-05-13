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

package io.asgardeo.android.core.core.managers.app_auth.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import io.asgardeo.android.core.core.managers.app_auth.AppAuthManager
import io.asgardeo.android.core.models.exceptions.AppAuthManagerException
import io.asgardeo.android.core.models.http_client.CustomHttpURLConnection
import io.asgardeo.android.core.models.state.TokenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenRequest
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Use to manage the AppAuth SDK.
 *
 * @param customTrustClient The [OkHttpClient] instance.
 * @param clientId The client ID.
 * @param redirectUri The redirect URI.
 * @param serviceConfig The [AuthorizationServiceConfiguration] instance.
 */
internal class AppAuthManagerImpl private constructor(
    private val customTrustClient: OkHttpClient,
    private val clientId: String,
    private val redirectUri: Uri,
    private val serviceConfig: AuthorizationServiceConfiguration
) : AppAuthManager {
    companion object {
        private const val TAG = "AppAuthManager"

        /**
         * Instance of the [AppAuthManagerImpl] class.
         */
        private var appAuthManagerImplInstance = WeakReference<AppAuthManagerImpl?>(null)

        /**
         * Initialize the [AppAuthManagerImpl] class.
         *
         * @param httpBuilderClient The [OkHttpClient] instance.
         * @param clientId The client ID.
         * @param redirectUri The redirect URI.
         * @param serviceConfig The [AuthorizationServiceConfiguration] instance.
         *
         * @return The [AppAuthManagerImpl] instance.
         */
        fun getInstance(
            httpBuilderClient: OkHttpClient,
            clientId: String,
            redirectUri: Uri,
            serviceConfig: AuthorizationServiceConfiguration
        ): AppAuthManagerImpl {
            var appAuthManagerImpl = appAuthManagerImplInstance.get()
            if (appAuthManagerImpl == null) {
                appAuthManagerImpl = AppAuthManagerImpl(
                    httpBuilderClient,
                    clientId,
                    redirectUri,
                    serviceConfig
                )
                appAuthManagerImplInstance = WeakReference(appAuthManagerImpl)
            }
            return appAuthManagerImpl
        }
    }

    /**
     * Use to get the [AuthorizationService] instance to perform requests.
     *
     * @param context The [Context] instance.
     *
     * @return The [AuthorizationService] instance.
     */
    private fun getAuthorizationService(context: Context): AuthorizationService =
        AuthorizationService(
            context,
            AppAuthConfiguration.Builder()
                .setConnectionBuilder { url ->
                    CustomHttpURLConnection(
                        url,
                        customTrustClient.x509TrustManager as X509TrustManager,
                        customTrustClient.sslSocketFactory
                    ).getConnection()
                }
                .build()
        )

    /**
     * Use to exchange the authorization code for the access token.
     *
     * @param authorizationCode The authorization code.
     *
     * @throws AppAuthManagerException If the token request fails.
     *
     * @return The [TokenState] instance.
     */
    override suspend fun exchangeAuthorizationCode(
        authorizationCode: String,
        context: Context,
    ): TokenState? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val tokenRequest: TokenRequest =
                AppAuthManagerImplRequestBuilder.getExchangeAuthorizationCodeRequestBuilder(
                    serviceConfig,
                    clientId,
                    authorizationCode,
                    redirectUri
                )

            val authService: AuthorizationService = getAuthorizationService(context)

            try {
                authService.performTokenRequest(tokenRequest) { tokenResponse, exception ->
                    val appAuthState = AuthState(serviceConfig)
                    appAuthState.update(tokenResponse, exception)

                    when {
                        exception != null -> {
                            Log.e(
                                TAG,
                                "${exception.message.toString()}. ${exception.stackTraceToString()}",
                            )
                            continuation.resumeWithException(exception)
                        }

                        tokenResponse == null -> {
                            val e = AppAuthManagerException(
                                AppAuthManagerException.EMPTY_TOKEN_RESPONSE
                            )

                            Log.e(
                                TAG,
                                "${e.message.toString()}. ${e.stackTraceToString()}",
                            )
                            continuation.resumeWithException(e)
                        }

                        else -> {
                            continuation.resume(TokenState(appAuthState))
                        }
                    }
                }
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "${exception.message.toString()}, auth code exchange failed. ${exception.stackTraceToString()}",
                )
                continuation.resumeWithException(exception)
            } finally {
                authService.dispose()
            }
        }
    }

    /**
     * Use to perform the refresh token grant.
     *
     * @param context The [Context] instance.
     * @param tokenState The [TokenState] instance.
     *
     * @throws AppAuthManagerException If the token request fails.
     *
     * @return Updated [TokenState] instance.
     */
    override suspend fun performRefreshTokenGrant(
        context: Context,
        tokenState: TokenState,
    ): TokenState? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->

            val refreshToken: String? = tokenState.getAppAuthState().refreshToken

            // Check we have a refresh token
            if (refreshToken.isNullOrBlank()) {
                val e = AppAuthManagerException(
                    AppAuthManagerException.INVALID_REFRESH_TOKEN
                )

                Log.e(
                    TAG,
                    "${e.message.toString()}. ${e.stackTraceToString()}",
                )
                continuation.resumeWithException(e)
            }

            // Create the refresh token grant request
            val tokenRequest: TokenRequest =
                AppAuthManagerImplRequestBuilder.getRefreshTokenRequestBuilder(
                    serviceConfig,
                    clientId,
                    refreshToken!!,
                    redirectUri
                )
            val authService: AuthorizationService = getAuthorizationService(context)

            // Trigger the request
            try {
                val appAuthState: AuthState = tokenState.getAppAuthState()

                authService.performTokenRequest(tokenRequest) { tokenResponse, exception ->
                    appAuthState.update(tokenResponse, exception)
                    when {
                        // Translate AppAuth errors to the display format
                        exception != null -> {
                            // If we get an invalid_grant error it means the refresh token has expired
                            if (exception.type == AuthorizationException.TYPE_OAUTH_TOKEN_ERROR &&
                                exception.code == AuthorizationException.TokenRequestErrors.INVALID_GRANT.code
                            ) {
                                Log.e(
                                    TAG,
                                    "${exception.message.toString()}. ${exception.stackTraceToString()}",
                                )
                                continuation.resumeWithException(
                                    AppAuthManagerException(
                                        AppAuthManagerException.INVALID_REFRESH_TOKEN,
                                        exception.message
                                    )
                                )

                            } else {
                                Log.e(
                                    TAG,
                                    "${exception.message.toString()}. ${exception.stackTraceToString()}",
                                )
                                continuation.resumeWithException(exception)
                            }
                        }

                        // Sanity check
                        tokenResponse == null -> {
                            val e = AppAuthManagerException(
                                AppAuthManagerException.EMPTY_TOKEN_RESPONSE
                            )

                            Log.e(
                                TAG,
                                "${e.message.toString()}. ${e.stackTraceToString()}",
                            )
                            continuation.resumeWithException(e)
                        }

                        // return the token response
                        else -> {
                            tokenState.updateAppAuthState(appAuthState)
                            continuation.resume(tokenState)
                        }
                    }
                }
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "${exception.message.toString()}, token refresh failed. ${exception.stackTraceToString()}",
                )
                continuation.resumeWithException(exception)
            } finally {
                authService.dispose()
            }
        }
    }

    /**
     * Perform an action with the tokens. If the token is expired, it will perform the refresh the
     * tokens, and then perform the action. This will also update the token in the data store
     * as well.
     *
     * @param context The [Context] instance.
     * @param tokenState The [TokenState] instance.
     * @param action The action to perform.
     *
     * @return Updated [TokenState] instance.
     */
    override suspend fun performAction(
        context: Context,
        tokenState: TokenState,
        action: suspend (String?, String?) -> Unit
    ): TokenState? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val appAuthState: AuthState = tokenState.getAppAuthState()
            val authService: AuthorizationService = getAuthorizationService(context)

            if (appAuthState.isAuthorized) {
                appAuthState.performActionWithFreshTokens(authService)
                { accessToken, idToken, exception ->
                    if (exception != null) {
                        Log.e(
                            TAG,
                            "${exception.message.toString()}. ${exception.stackTraceToString()}",
                        )
                        continuation.resumeWithException(exception)
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            runCatching {
                                action(accessToken, idToken)
                            }.onSuccess {
                                continuation.resume(tokenState)
                            }.onFailure {
                                Log.e(
                                    TAG,
                                    "${it.message.toString()}. ${it.stackTraceToString()}",
                                )
                                continuation.resumeWithException(it)
                            }
                            tokenState.updateAppAuthState(appAuthState)
                        }
                    }
                }
            } else {
                val e = AppAuthManagerException(
                    AppAuthManagerException.INVALID_AUTH_STATE
                )

                Log.e(
                    TAG,
                    "${e.message.toString()}. ${e.stackTraceToString()}",
                )
                continuation.resumeWithException(e)
            }
        }
    }
}
