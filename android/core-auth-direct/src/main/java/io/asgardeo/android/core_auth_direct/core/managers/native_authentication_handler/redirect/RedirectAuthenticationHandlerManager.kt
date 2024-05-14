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

package io.asgardeo.android.core_auth_direct.core.managers.native_authentication_handler.redirect

import android.content.Context
import android.net.Uri
import io.asgardeo.android.core_auth_direct.models.autheniticator.Authenticator
import io.asgardeo.android.core_auth_direct.models.exceptions.RedirectAuthenticationException

/**
 * This manager is responsible for handling the redirect authentication process
 * using the redirection prompt type
 */
interface RedirectAuthenticationHandlerManager {
    /**
     * Redirect the user to the authenticator's authentication page.
     *
     * @param context The context of the application
     * @param authenticator The authenticator to redirect the user
     */
    suspend fun redirectAuthenticate(
        context: Context,
        authenticator: Authenticator
    ): LinkedHashMap<String, String>?

    /**
     * Handle the redirect URI and authenticate the user with the selected authenticator.
     *
     * @param context The context of the application
     * @param deepLink The deep link URI that is received from the redirect URI
     *
     * @return The authentication parameters extracted from the redirect URI
     */
    fun handleRedirectUri(context: Context, deepLink: Uri)

    /**
     * Handle the cancel event of the redirect authentication process.
     *
     * @throws [RedirectAuthenticationException] with the message [RedirectAuthenticationException.AUTHENTICATION_CANCELLED]
     */
    fun handleRedirectAuthenticationCancel()
}
