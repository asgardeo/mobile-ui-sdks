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

package io.asgardeo.android.core.core.managers.native_authentication_handler.google_native

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.asgardeo.android.core.models.auth_params.AuthParams

/**
 * Interface to be implemented by the Google Native Authentication Handler Manager
 * This manager is responsible for handling the Google Native Authentication using the
 * Credential Manager API
 */
interface GoogleNativeAuthenticationHandlerManager {
    /**
     * Authenticate the user with Google using the Credential Manager API
     *
     * @param context [Context] of the application
     * @param nonce Nonce to be used in the authentication, this is sent by the Identity Server.
     *
     * @return Google ID Token of the authenticated user
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun authenticateWithGoogleNative(context: Context, nonce: String): AuthParams?

    /**
     * Logout the user from the google account
     *
     * @param context [Context] of the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun logout(context: Context)
}
