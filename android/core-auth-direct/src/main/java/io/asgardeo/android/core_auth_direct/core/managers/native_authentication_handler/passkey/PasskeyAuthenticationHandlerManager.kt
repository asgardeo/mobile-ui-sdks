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

package io.asgardeo.android.core_auth_direct.core.managers.native_authentication_handler.passkey

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.asgardeo.android.core_auth_direct.models.auth_params.AuthParams

interface PasskeyAuthenticationHandlerManager {
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
    suspend fun authenticateWithPasskey(
        context: Context,
        challengeString: String?,
        allowCredentials: List<String>?,
        timeout: Long?,
        userVerification: String?,
    ): AuthParams

    /**
     * Logout the user from the passkey authentication
     *
     * @param context [Context] of the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun logout(context: Context)
}
