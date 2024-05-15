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

package io.asgardeo.android.core_auth_direct.core.managers.native_authentication_handler.redirect.impl

import android.content.Context
import android.net.Uri
import io.asgardeo.android.core_auth_direct.core.managers.native_authentication_handler.redirect.RedirectAuthenticationHandlerManager
import io.asgardeo.android.core_auth_direct.core.ui.RedirectAuthenticationManagementActivity
import io.asgardeo.android.core_auth_direct.models.autheniticator.Authenticator
import io.asgardeo.android.core_auth_direct.models.exceptions.RedirectAuthenticationException
import io.asgardeo.android.core_auth_direct.models.prompt_type.PromptTypes
import kotlinx.coroutines.CompletableDeferred
import java.lang.ref.WeakReference


/**
 * Implementation of [RedirectAuthenticationHandlerManager]
 * This manager is responsible for handling the redirect authentication process
 * using the redirection prompt type
 */
class RedirectAuthenticationHandlerManagerImpl private constructor() :
    RedirectAuthenticationHandlerManager {
    companion object {
        /**
         * Instance of the [RedirectAuthenticationHandlerManagerImpl] that will be used throughout the application
         */
        private var redirectAuthenticationHandlerManagerImplInstance:
                WeakReference<RedirectAuthenticationHandlerManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [RedirectAuthenticationHandlerManagerImpl] instance and return the instance.
         *
         * @return Initialized [RedirectAuthenticationHandlerManagerImpl] instance
         */
        fun getInstance(): RedirectAuthenticationHandlerManagerImpl {
            var redirectAuthenticationHandlerImpl =
                redirectAuthenticationHandlerManagerImplInstance.get()
            if (redirectAuthenticationHandlerImpl == null) {
                redirectAuthenticationHandlerImpl = RedirectAuthenticationHandlerManagerImpl()
                redirectAuthenticationHandlerManagerImplInstance =
                    WeakReference(redirectAuthenticationHandlerImpl)
            }
            return redirectAuthenticationHandlerImpl
        }
    }

    /**
     * The selected authenticator to redirect the user
     */
    private var selectedAuthenticator: Authenticator? = null

    /**
     * The authentication parameters extracted from the redirect URI
     */
    private var authenticatorAuthParamsMap: LinkedHashMap<String, String>? = null

    /**
     * Deferred object to wait for the result of the redirect authentication process.
     */
    private val redirectAuthenticationResultDeferred: CompletableDeferred<Unit> by lazy {
        CompletableDeferred()
    }

    /**
     * Redirect the user to the authenticator's authentication page.
     *
     * @param context The context of the application
     * @param authenticator The authenticator to redirect the user
     */
    override suspend fun redirectAuthenticate(
        context: Context,
        authenticator: Authenticator
    ): LinkedHashMap<String, String>? {
        // Retrieving the prompt type of the authenticator
        val promptType: String? = authenticator.metadata?.promptType

        if (promptType == PromptTypes.REDIRECTION_PROMPT.promptType) {
            // Retrieving the redirect URI of the authenticator
            val redirectUri: String? = authenticator.metadata?.additionalData?.redirectUrl

            if (redirectUri.isNullOrEmpty()) {
                throw (RedirectAuthenticationException(
                    RedirectAuthenticationException
                        .REDIRECT_URI_NOT_FOUND
                ))
            } else {
                // redirect to the RedirectAuthenticationManagementActivity
                // to handle the redirect URI and continue the authentication process
                context.startActivity(
                    RedirectAuthenticationManagementActivity.createStartIntent(
                        context,
                        redirectUri
                    )
                )

                selectedAuthenticator = authenticator

                redirectAuthenticationResultDeferred.await()

                if (redirectAuthenticationResultDeferred.isCompleted) {
                    return authenticatorAuthParamsMap
                } else {
                    throw (RedirectAuthenticationException(
                        RedirectAuthenticationException.REDIRECT_URI_NOT_FOUND
                    ))
                }
            }
        } else {
            throw (RedirectAuthenticationException(
                RedirectAuthenticationException.NOT_REDIRECT_PROMPT
            ))
        }
    }

    /**
     * Handle the redirect URI and authenticate the user with the selected authenticator.
     *
     * @param context The context of the application
     * @param deepLink The deep link URI that is received from the redirect URI
     *
     * @return The authentication parameters extracted from the redirect URI
     */
    override fun handleRedirectUri(context: Context, deepLink: Uri) {
        // Setting up the deferred object to wait for the result
        if (selectedAuthenticator != null) {
            val requiredParams: List<String> = selectedAuthenticator!!.requiredParams!!

            // Extract required parameters from the authenticator
            val authParamsMap: LinkedHashMap<String, String> = LinkedHashMap()

            for (param in requiredParams) {
                val paramValue: String? = deepLink.getQueryParameter(param)

                if (paramValue != null) {
                    authParamsMap[param] = paramValue
                }
            }

            authenticatorAuthParamsMap = authParamsMap
        }

        redirectAuthenticationResultDeferred.complete(Unit)
    }

    /**
     * Handle the cancel event of the redirect authentication process.
     *
     * @throws [RedirectAuthenticationException] with the message [RedirectAuthenticationException.AUTHENTICATION_CANCELLED]
     */
    override fun handleRedirectAuthenticationCancel() {
        redirectAuthenticationResultDeferred.complete(Unit)
    }
}
