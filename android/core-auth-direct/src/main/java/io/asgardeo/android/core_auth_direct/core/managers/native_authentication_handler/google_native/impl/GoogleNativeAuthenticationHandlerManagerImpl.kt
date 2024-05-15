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

package io.asgardeo.android.core_auth_direct.core.managers.native_authentication_handler.google_native.impl

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.asgardeo.android.core_auth_direct.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core_auth_direct.core.managers.native_authentication_handler.google_native.GoogleNativeAuthenticationHandlerManager
import io.asgardeo.android.core_auth_direct.models.auth_params.AuthParams
import io.asgardeo.android.core_auth_direct.models.auth_params.GoogleNativeAuthenticatorTypeAuthParams
import io.asgardeo.android.core_auth_direct.models.exceptions.GoogleNativeAuthenticationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

/**
 * Implementation of [GoogleNativeAuthenticationHandlerManager]
 * This manager is responsible for handling the Google Native Authentication using the
 * Credential Manager API
 *
 * @param authenticationCoreConfig [AuthenticationCoreConfig] to get the Google Web Client ID
 * @param googleNativeAuthenticationHandlerManagerImplRequestBuilder [GoogleNativeAuthenticationHandlerManagerImplRequestBuilder] to build the requests
 */
class GoogleNativeAuthenticationHandlerManagerImpl private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig,
    private val googleNativeAuthenticationHandlerManagerImplRequestBuilder
    : GoogleNativeAuthenticationHandlerManagerImplRequestBuilder
) : GoogleNativeAuthenticationHandlerManager {
    companion object {
        /**
         * Instance of the [GoogleNativeAuthenticationHandlerManagerImpl] that will be used throughout the application
         */
        private var googleNativeAuthenticationHandlerManagerImplInstance:
                WeakReference<GoogleNativeAuthenticationHandlerManagerImpl> = WeakReference(null)

        /**
         * Initialize the [GoogleNativeAuthenticationHandlerManagerImpl] instance and return the instance.
         *
         * @param authenticationCoreConfig Configuration of the Authenticator [AuthenticationCoreConfig]
         * @param googleNativeAuthenticationHandlerManagerImplRequestBuilder Request builder class to build the requests
         *
         * @return Initialized [GoogleNativeAuthenticationHandlerManagerImpl] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig,
            googleNativeAuthenticationHandlerManagerImplRequestBuilder: GoogleNativeAuthenticationHandlerManagerImplRequestBuilder
        ): GoogleNativeAuthenticationHandlerManagerImpl {
            var googleNativeAuthenticationHandlerImpl =
                googleNativeAuthenticationHandlerManagerImplInstance.get()
            if (googleNativeAuthenticationHandlerImpl == null) {
                googleNativeAuthenticationHandlerImpl =
                    GoogleNativeAuthenticationHandlerManagerImpl(
                        authenticationCoreConfig,
                        googleNativeAuthenticationHandlerManagerImplRequestBuilder
                    )
                googleNativeAuthenticationHandlerManagerImplInstance =
                    WeakReference(googleNativeAuthenticationHandlerImpl)
            }
            return googleNativeAuthenticationHandlerImpl
        }
    }

    /**
     * Get the [CredentialManager] instance
     *
     * @param context [Context] of the application
     *
     * @return [CredentialManager] instance
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun getCredentialManager(context: Context) = CredentialManager.create(context)

    /**
     * Get the [GetGoogleIdOption] instance
     *
     * @param googleWebClientId Google Web Client ID
     * @param nonce Nonce to be used in the authentication, this is sent by the Identity Server.
     *
     * @return [GetGoogleIdOption] instance
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun getGoogleIdOptions(googleWebClientId: String, nonce: String): GetGoogleIdOption =
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(googleWebClientId)
            .setNonce(nonce)
            .build()

    /**
     * Authenticate the user with Google using the Credential Manager API
     *
     * @param context [Context] of the application
     * @param nonce Nonce to be used in the authentication, this is sent by the Identity Server.
     *
     * @return Google ID Token of the authenticated user
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun authenticateWithGoogleNative(context: Context, nonce: String): AuthParams {
        val googleWebClientId: String? = authenticationCoreConfig.getGoogleWebClientId()

        if (googleWebClientId.isNullOrEmpty()) {
            throw GoogleNativeAuthenticationException(
                GoogleNativeAuthenticationException.GOOGLE_WEB_CLIENT_ID_NOT_SET
            )
        } else {
            val credentialManager: CredentialManager = getCredentialManager(context)
            val googleIdOptions: GetGoogleIdOption = getGoogleIdOptions(googleWebClientId, nonce)

            val request: GetCredentialRequest =
                googleNativeAuthenticationHandlerManagerImplRequestBuilder
                    .getAuthenticateWithGoogleNativeRequestBuilder(googleIdOptions)

            return withContext(Dispatchers.IO) {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val googleIdTokenCredential: GoogleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)

                return@withContext GoogleNativeAuthenticatorTypeAuthParams(
                    idToken = googleIdTokenCredential.idToken,
                    accessToken = googleIdTokenCredential.id
                )
            }
        }
    }

    /**
     * Logout the user from the google account
     *
     * @param context [Context] of the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun logout(context: Context) {
        withContext(Dispatchers.IO) {
            getCredentialManager(context).clearCredentialState(
                googleNativeAuthenticationHandlerManagerImplRequestBuilder
                    .getGoogleLogoutRequestBuilder()
            )
        }
    }
}
