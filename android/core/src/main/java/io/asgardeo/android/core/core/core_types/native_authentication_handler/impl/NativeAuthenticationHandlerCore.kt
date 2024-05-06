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

package io.asgardeo.android.core.core.core_types.native_authentication_handler.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core.core_types.native_authentication_handler.NativeAuthenticationHandlerCoreDef
import io.asgardeo.android.core.core.di.NativeAuthenticationHandlerCoreContainer
import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native.GoogleNativeAuthenticationHandlerManager
import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native_legacy.GoogleNativeLegacyAuthenticationHandlerManager
import io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.PasskeyAuthenticationHandlerManager
import io.asgardeo.android.core.core.managers.native_authentication_handler.redirect.RedirectAuthenticationHandlerManager
import io.asgardeo.android.core.core_config.providers.authentication_core_config_provider.AuthenticationCoreConfigProvider
import io.asgardeo.android.core.models.auth_params.AuthParams
import io.asgardeo.android.core.models.autheniticator.Authenticator
import java.lang.ref.WeakReference

/**
 * Implementation of the [NativeAuthenticationHandlerCoreDef]
 *
 * @property authenticationCoreConfigProvider Provider to update the [AuthenticationCoreConfig] based on the discovery response [AuthenticationCoreConfigProvider]
 */
class NativeAuthenticationHandlerCore private constructor(
    private val authenticationCoreConfigProvider: AuthenticationCoreConfigProvider
) : NativeAuthenticationHandlerCoreDef {
    /**
     * Instance of the [AuthenticationCoreConfig] that will be used throughout the application
     */
    private val authenticationCoreConfig: AuthenticationCoreConfig by lazy {
        authenticationCoreConfigProvider.getUpdatedAuthenticationCoreConfig()
    }

    /**
     * Instance of the [GoogleNativeAuthenticationHandlerManager] that will be used throughout the application
     */
    private val googleNativeAuthenticationHandlerManager: GoogleNativeAuthenticationHandlerManager by lazy {
        NativeAuthenticationHandlerCoreContainer.getGoogleNativeAuthenticationHandlerManager(
            authenticationCoreConfig
        )
    }

    /**
     * Instance of the [GoogleNativeLegacyAuthenticationHandlerManager] that will be used throughout the application
     */
    private val googleNativeLegacyAuthenticationHandlerManager:
            GoogleNativeLegacyAuthenticationHandlerManager by lazy {
        NativeAuthenticationHandlerCoreContainer.getGoogleNativeLegacyAuthenticationHandlerManager(
            authenticationCoreConfig
        )
    }

    /**
     * Instance of the [RedirectAuthenticationHandlerManager] that will be used throughout the application
     */
    private val redirectAuthenticationHandlerManager: RedirectAuthenticationHandlerManager by lazy {
        NativeAuthenticationHandlerCoreContainer.getRedirectAuthenticationHandlerManager()
    }

    /**
     * Instance of the [PasskeyAuthenticationHandlerManager] that will be used throughout the application
     */
    private val passkeyAuthenticationHandlerManager: PasskeyAuthenticationHandlerManager by lazy {
        NativeAuthenticationHandlerCoreContainer.getPasskeyAuthenticationHandlerManager()
    }

    companion object {
        /**
         * Instance of the [NativeAuthenticationHandlerCore] that will be used throughout the application
         */
        private var nativeAuthenticationCoreInstance =
            WeakReference<NativeAuthenticationHandlerCore?>(null)

        /**
         * Initialize the AuthenticationCore instance and return the instance.
         *
         * @param authenticationCoreConfigProvider Provider to update the [AuthenticationCoreConfig] based on the discovery response [AuthenticationCoreConfigProvider]
         *
         * @return Initialized [NativeAuthenticationHandlerCore] instance
         */
        fun getInstance(
            authenticationCoreConfigProvider: AuthenticationCoreConfigProvider
        ): NativeAuthenticationHandlerCore {
            var nativeAuthenticationCore = nativeAuthenticationCoreInstance.get()
            if (nativeAuthenticationCore == null ||
                nativeAuthenticationCore.authenticationCoreConfigProvider != authenticationCoreConfigProvider
            ) {
                nativeAuthenticationCore = NativeAuthenticationHandlerCore(authenticationCoreConfigProvider)
                nativeAuthenticationCoreInstance = WeakReference(nativeAuthenticationCore)
            }
            return nativeAuthenticationCore
        }
    }

    /**
     * Handle the Google Native Authentication.
     * This method will authenticate the user with the Google Native Authentication.
     * This method is recommended to use for new applications that support Android 14(API 34) and above.
     *
     * @param context Context of the application
     * @param nonce Nonce to be used in the authentication, this is sent by the Identity Server.
     *
     * @return idToken sent by the Google Native Authentication
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun handleGoogleNativeAuthentication(context: Context, nonce: String)
            : AuthParams? =
        googleNativeAuthenticationHandlerManager.authenticateWithGoogleNative(context, nonce)

    /**
     * Handle the Google Native Authentication logout.
     * This method will logout the user from the Google Native Authentication.
     *
     * @param context Context of the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun handleGoogleNativeAuthenticationLogout(context: Context) {
        googleNativeAuthenticationHandlerManager.logout(context)
    }

    /**
     * Handle the Google Native Authentication result using the legacy one tap method.
     * This method will authenticate the user with the Google Native Authentication using the legacy one tap method.
     * This is not recommended to use for new applications that support Android 14(API 34) and above.
     *
     * @param context [Context] of the application
     * @param googleAuthenticateResultLauncher [ActivityResultLauncher] to launch the Google authentication intent
     */
    override suspend fun handleGoogleNativeLegacyAuthentication(
        context: Context,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    ) {
        googleNativeLegacyAuthenticationHandlerManager.authenticateWithGoogleNativeLegacy(
            context,
            googleAuthenticateResultLauncher
        )
    }

    /**
     * Handle the Google Native Authentication logout using the legacy one tap method.
     * This method will logout the user from the Google Native Authentication using the legacy one tap method.
     *
     * @param context [Context] of the application
     */
    override suspend fun handleGoogleNativeLegacyAuthenticationLogout(context: Context) {
        googleNativeLegacyAuthenticationHandlerManager.logout(context)
    }

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
    override suspend fun handleGoogleNativeLegacyAuthenticateResult(resultCode: Int, data: Intent)
            : AuthParams? =
        googleNativeLegacyAuthenticationHandlerManager.handleGoogleNativeLegacyAuthenticateResult(
            resultCode,
            data
        )

    /**
     * Handle the redirect authentication process.
     * This method will redirect the user to the authenticator's authentication page.
     *
     * @param context The context of the application
     * @param authenticator The authenticator to redirect the user
     *
     * @return The authentication parameters extracted from the redirect URI
     */
    override suspend fun handleRedirectAuthentication(
        context: Context,
        authenticator: Authenticator
    ): LinkedHashMap<String, String>? =
        redirectAuthenticationHandlerManager.redirectAuthenticate(context, authenticator)

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
    override suspend fun handlePasskeyAuthentication(
        context: Context,
        challengeString: String?,
        allowCredentials: List<String>?,
        timeout: Long?,
        userVerification: String?
    ): AuthParams =
        passkeyAuthenticationHandlerManager.authenticateWithPasskey(
            context,
            challengeString,
            allowCredentials,
            timeout,
            userVerification
        )

    /**
     * Handle the passkey authentication logout.
     *
     * @param context [Context] of the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun handlePasskeyAuthenticationLogout(context: Context) {
        passkeyAuthenticationHandlerManager.logout(context)
    }
}
