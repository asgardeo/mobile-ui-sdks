/*
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.asgardeo.android.core.core.core_types.native_authentication_handler

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import io.asgardeo.android.core.models.auth_params.AuthParams
import io.asgardeo.android.core.models.autheniticator.Authenticator

/**
 * Native Authentication Handler Core class interface which has the core functionality to
 * handle the Native Authentication.
 */
interface NativeAuthenticationHandlerCoreDef {
    /**
     * Handle the Google Native Authentication.
     * This method will authenticate the user with the Google Native Authentication.
     *
     * @param context Context of the application
     * @param nonce Nonce value to authenticate the user, which is sent by the Identity Server.
     *
     * @return idToken sent by the Google Native Authentication
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun handleGoogleNativeAuthentication(context: Context, nonce: String): AuthParams?

    /**
     * Handle the Google Native Authentication logout.
     * This method will logout the user from the Google Native Authentication.
     *
     * @param context Context of the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun handleGoogleNativeAuthenticationLogout(context: Context)

    /**
     * Handle the Google Native Authentication result using the legacy one tap method.
     * This method will authenticate the user with the Google Native Authentication using the legacy one tap method.
     * This is not recommended to use for new applications that support Android 14(API 34) and above.
     *
     * @param context [Context] of the application
     * @param googleAuthenticateResultLauncher [ActivityResultLauncher] to launch the Google authentication intent
     */
    suspend fun handleGoogleNativeLegacyAuthentication(
        context: Context,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    )

    /**
     * Handle the Google Native Authentication logout using the legacy one tap method.
     * This method will logout the user from the Google Native Authentication using the legacy one tap method.
     *
     * @param context [Context] of the application
     */
    suspend fun handleGoogleNativeLegacyAuthenticationLogout(context: Context)

    /**
     * Handle the Google native authentication result using the legacy one tap method.
     * This method will authenticate the user with the Google Native Authentication using the legacy one tap method.
     * This is not recommended to use for new applications that support Android 14(API 34) and above.
     *
     * @param resultCode The result code of the Google authentication process
     * @param data The [Intent] object that contains the result of the Google authentication process
     *
     * @return The Google native authenticator parameters [LinkedHashMap] that contains the ID Token and the Auth Code
     */
    suspend fun handleGoogleNativeLegacyAuthenticateResult(resultCode: Int, data: Intent)
            : AuthParams?

    /**
     * Handle the redirect authentication process.
     * This method will redirect the user to the authenticator's authentication page.
     *
     * @param context The context of the application
     * @param authenticator The authenticator to redirect the user
     *
     * @return The authentication parameters extracted from the redirect URI
     */
    suspend fun handleRedirectAuthentication(
        context: Context,
        authenticator: Authenticator
    ): LinkedHashMap<String, String>?

    /**
     * Handle the passkey authentication process.
     *
     * @param context [Context] of the application
     * @param challengeString Challenge string to authenticate the user. This string is received from the Identity Server
     * @param allowCredentials List of allowed credentials. Default is empty array.
     * @param timeout Timeout for the authentication. Default is 300000.
     * @param userVerification User verification method. Default is "required"
     *
     * @return Authenticator
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun handlePasskeyAuthentication(
        context: Context,
        challengeString: String?,
        allowCredentials: List<String>?,
        timeout: Long?,
        userVerification: String?
    ): AuthParams?

    /**
     * Handle the passkey authentication logout.
     *
     * @param context [Context] of the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun handlePasskeyAuthenticationLogout(context: Context)
}
