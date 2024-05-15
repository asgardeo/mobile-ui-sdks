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

package io.asgardeo.android.core_auth_direct.core.managers.native_authentication_handler.google_native_legacy

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import io.asgardeo.android.core_auth_direct.models.auth_params.AuthParams

/**
 * Interface to be implemented by the Google Native Authentication Handler Manager
 * This manager is responsible for handling the Google Native Authentication using the
 * legacy one tap method
 */
interface GoogleNativeLegacyAuthenticationHandlerManager {
    /**
     * Authenticate the user with Google using the legacy one tap method.
     *
     * @param context [Context] of the application
     * @param googleAuthenticateResultLauncher [ActivityResultLauncher] to launch the Google authentication intent
     */
    suspend fun authenticateWithGoogleNativeLegacy(
        context: Context,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    )

    /**
     * Handle the Google native authentication result.
     *
     * @param resultCode The result code of the Google authentication process
     * @param data The [Intent] object that contains the result of the Google authentication process
     *
     * @return The Google native authenticator parameters [LinkedHashMap] that contains the ID Token and the Auth Code
     */
    suspend fun handleGoogleNativeLegacyAuthenticateResult(resultCode: Int, data: Intent): AuthParams?

    /**
     * Logout the user from the Google account
     *
     * @param context [Context] of the application
     */
    fun logout(context: Context)
}
