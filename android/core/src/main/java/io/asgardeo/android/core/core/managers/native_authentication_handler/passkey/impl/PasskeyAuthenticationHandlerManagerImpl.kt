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

package io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.impl

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.PasskeyAuthenticationHandlerManager
import io.asgardeo.android.core.models.auth_params.AuthParams
import io.asgardeo.android.core.models.auth_params.PasskeyAuthenticatorTypeAuthParams
import io.asgardeo.android.core.models.autheniticator.passkey_related_data.ChallengeInfo
import io.asgardeo.android.core.models.autheniticator.passkey_related_data.PasskeyChallenge
import io.asgardeo.android.core.models.autheniticator.passkey_related_data.PasskeyCredentialAuthParams
import io.asgardeo.android.core.models.exceptions.PasskeyAuthenticationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

/**
 * Implementation of the Passkey Authentication Handler Manager
 * This manager is responsible for handling the Passkey Authentication using the
 * Credential Manager API
 *
 * @param passkeyAuthenticationRequestHandlerManagerImplRequestBuilder [PasskeyAuthenticationHandlerManagerImplRequestBuilder] to build the requests
 */
class PasskeyAuthenticationHandlerManagerImpl private constructor(
    private val passkeyAuthenticationRequestHandlerManagerImplRequestBuilder
    : PasskeyAuthenticationHandlerManagerImplRequestBuilder
) : PasskeyAuthenticationHandlerManager {
    companion object {
        /**
         * Instance of the [PasskeyAuthenticationHandlerManagerImpl] that will be used throughout the application
         */
        private var passkeyAuthenticationHandlerManagerImplInstance:
                WeakReference<PasskeyAuthenticationHandlerManagerImpl> = WeakReference(null)

        /**
         * Initialize the [PasskeyAuthenticationHandlerManagerImpl] instance and return the instance.
         *
         * @param passkeyAuthenticationHandlerManagerImplRequestBuilder Request builder class to build the requests
         *
         * @return Initialized [PasskeyAuthenticationHandlerManagerImpl] instance
         */
        fun getInstance(
            passkeyAuthenticationHandlerManagerImplRequestBuilder
            : PasskeyAuthenticationHandlerManagerImplRequestBuilder
        ): PasskeyAuthenticationHandlerManagerImpl {
            var passkeyAuthenticationHandlerImpl =
                passkeyAuthenticationHandlerManagerImplInstance.get()
            if (passkeyAuthenticationHandlerImpl == null) {
                passkeyAuthenticationHandlerImpl =
                    PasskeyAuthenticationHandlerManagerImpl(
                        passkeyAuthenticationHandlerManagerImplRequestBuilder
                    )
                passkeyAuthenticationHandlerManagerImplInstance =
                    WeakReference(passkeyAuthenticationHandlerImpl)
            }
            return passkeyAuthenticationHandlerImpl
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
     * Get the [GetPublicKeyCredentialOption] instance
     *
     * @param requestJson Request JSON string to get the public key credential options
     *
     * @return [GetPublicKeyCredentialOption] instance
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun getPasskeyCredentialOptions(requestJson: String) =
        GetPublicKeyCredentialOption(requestJson = requestJson)

    /**
     * Authenticate the user with Passkey with the Credential Manager API
     *
     * @param context [Context] of the application
     * @param challengeString Challenge string to authenticate the user. This string is received from the Identity Server
     * @param allowCredentials List of allowed credentials. Default is empty array.
     * @param timeout Timeout for the authentication. Default is 300000.
     * @param userVerification User verification method. Default is "required"
     *
     * @return Authentication response JSON
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun authenticateWithPasskey(
        context: Context,
        challengeString: String?,
        allowCredentials: List<String>?,
        timeout: Long?,
        userVerification: String?,
    ): AuthParams {
        if (challengeString == null) {
            throw PasskeyAuthenticationException(
                PasskeyAuthenticationException.PASSKEY_CHALLENGE_STRING_EMPTY
            )
        } else {
            val challengeInfo: ChallengeInfo = ChallengeInfo
                .getChallengeInfoFromChallengeString(challengeString)

            val passkeyChallenge: PasskeyChallenge = challengeInfo.getPasskeyChallenge(
                allowCredentials = allowCredentials,
                timeout = timeout,
                userVerification = userVerification
            )

            val credentialManager: CredentialManager = getCredentialManager(context)
            val passkeyCredentialOptions: GetPublicKeyCredentialOption =
                getPasskeyCredentialOptions(passkeyChallenge.toString())

            val request: GetCredentialRequest =
                PasskeyAuthenticationHandlerManagerImplRequestBuilder.getAuthenticateWithPasskeyRequestBuilder(
                    passkeyCredentialOptions
                )

            return withContext(Dispatchers.IO) {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                when (val credential = result.credential) {
                    is PublicKeyCredential -> {
                        val responseJson: String = credential.authenticationResponseJson

                        val passkeyCredential: PasskeyCredentialAuthParams.PasskeyCredential =
                            PasskeyCredentialAuthParams.PasskeyCredential.fromJsonString(responseJson)

                        val passkeyCredentialAuthParams = PasskeyCredentialAuthParams(
                            requestId = challengeInfo.requestId,
                            credential = passkeyCredential
                        )

                        return@withContext PasskeyAuthenticatorTypeAuthParams(
                            tokenResponse = passkeyCredentialAuthParams.toString()
                        )
                    }

                    else -> {
                        throw PasskeyAuthenticationException(
                            PasskeyAuthenticationException.PASSKEY_AUTHENTICATION_NOT_SUPPORTED
                        )
                    }
                }
            }
        }
    }

    /**
     * Logout the user from the passkey authentication
     *
     * @param context [Context] of the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun logout(context: Context) {
        withContext(Dispatchers.IO) {
            getCredentialManager(context).clearCredentialState(
                PasskeyAuthenticationHandlerManagerImplRequestBuilder.getPasskeyLogoutRequestBuilder()
            )
        }
    }
}
