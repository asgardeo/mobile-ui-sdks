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

package io.asgardeo.android.core.provider.provider_managers.authentication.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.core.core_types.native_authentication_handler.NativeAuthenticationHandlerCoreDef
import io.asgardeo.android.core.models.auth_params.BasicAuthenticatorAuthParams
import io.asgardeo.android.core.models.auth_params.EmailOTPAuthenticatorTypeAuthParams
import io.asgardeo.android.core.models.auth_params.SMSOTPAuthenticatorTypeAuthParams
import io.asgardeo.android.core.models.auth_params.TotpAuthenticatorTypeAuthParams
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.autheniticator.AuthenticatorTypes
import io.asgardeo.android.core.models.state.AuthenticationState
import io.asgardeo.android.core.provider.provider_managers.authenticate_handler.AuthenticateHandlerProviderManager
import io.asgardeo.android.core.provider.provider_managers.authentication.AuthenticationProviderManager
import io.asgardeo.android.core.provider.provider_managers.authentication_state.AuthenticationStateProviderManager
import kotlinx.coroutines.flow.SharedFlow
import java.lang.ref.WeakReference

/**
 * Authentication provider manager that is used to manage the authentication process.
 *
 * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
 *
 * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
 *
 * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
 *
 * emit: [AuthenticationState.Error] - An error occurred during the authentication process
 *
 * @property authenticationCore The [AuthenticationCoreDef] instance
 * @property nativeAuthenticationHandlerCore The [NativeAuthenticationHandlerCoreDef] instance
 * @property authenticationStateProviderManager The [AuthenticationStateProviderManager] instance
 * @property authenticateHandlerProviderManager The [AuthenticateHandlerProviderManager] instance
 */
internal class AuthenticationProviderManagerImpl private constructor(
    private val authenticationCore: AuthenticationCoreDef,
    private val nativeAuthenticationHandlerCore: NativeAuthenticationHandlerCoreDef,
    private val authenticationStateProviderManager: AuthenticationStateProviderManager,
    private val authenticateHandlerProviderManager: AuthenticateHandlerProviderManager
) : AuthenticationProviderManager {
    companion object {
        /**
         * Instance of the [AuthenticationProviderManagerImpl] that will be used throughout the
         * application
         */
        private var authenticationProviderManagerInstance:
                WeakReference<AuthenticationProviderManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [AuthenticationProviderManagerImpl] instance and return the instance.
         *
         * @param authenticationCore The [AuthenticationCoreDef] instance
         * @param nativeAuthenticationHandlerCore The [NativeAuthenticationHandlerCoreDef] instance
         * @param authenticationStateProviderManager The [AuthenticationStateProviderManager] instance
         * @param authenticateHandlerProviderManager The [AuthenticateHandlerProviderManager] instance
         *
         * @return The [AuthenticationProviderManagerImpl] instance
         */
        fun getInstance(
            authenticationCore: AuthenticationCoreDef,
            nativeAuthenticationHandlerCore: NativeAuthenticationHandlerCoreDef,
            authenticationStateProviderManager: AuthenticationStateProviderManager,
            authenticateHandlerProviderManager: AuthenticateHandlerProviderManager
        ): AuthenticationProviderManagerImpl {
            var authenticationProviderManager = authenticationProviderManagerInstance.get()
            if (authenticationProviderManager == null) {
                authenticationProviderManager = AuthenticationProviderManagerImpl(
                    authenticationCore,
                    nativeAuthenticationHandlerCore,
                    authenticationStateProviderManager,
                    authenticateHandlerProviderManager
                )
                authenticationProviderManagerInstance = WeakReference(authenticationProviderManager)
            }
            return authenticationProviderManager
        }

        /**
         * Get the [AuthenticationProviderManagerImpl] instance.
         *
         * @return The [AuthenticationProviderManagerImpl] instance
         */
        fun getInstance(): AuthenticationProviderManagerImpl? =
            authenticationProviderManagerInstance.get()
    }

    /**
     * Get authentication state flow of the authentication state which is exposed to the outside.
     *
     * @return authentication state flow [SharedFlow<AuthenticationState>]
     */
    override fun getAuthenticationStateFlow(): SharedFlow<AuthenticationState> =
        authenticationStateProviderManager.getAuthenticationStateFlow()

    /**
     * Check whether the user is logged in or not.
     *
     * @param context The context of the application
     *
     * @return `true` if the user is logged in, `false` otherwise
     */
    override suspend fun isLoggedIn(context: Context): Boolean =
        authenticationCore.validateAccessToken(context) ?: false

    /**
     * Handle the authentication flow initially to check whether the user is authenticated or not.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     *
     * emit: [AuthenticationState.Initial] - The user is not authenticated to access the application
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     *
     * @param context The context of the application
     */
    override suspend fun isLoggedInStateFlow(context: Context) {
        authenticationStateProviderManager.emitAuthenticationState(AuthenticationState.Loading)

        runCatching {
            authenticationCore.validateAccessToken(context)
        }.onSuccess { isAccessTokenValid ->
            if (isAccessTokenValid == true) {
                authenticationStateProviderManager.emitAuthenticationState(
                    AuthenticationState.Authenticated
                )
            } else {
                authenticationStateProviderManager.emitAuthenticationState(AuthenticationState.Initial)
            }
        }.onFailure {
            authenticationStateProviderManager.emitAuthenticationState(AuthenticationState.Initial)
        }
    }

    /**
     * Initialize the authentication process.
     * This method will initialize the authentication process and emit the state of the authentication process.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     *
     * @param context The context of the application
     */
    override suspend fun initializeAuthentication(context: Context) {
        authenticationStateProviderManager.emitAuthenticationState(AuthenticationState.Loading)

        runCatching {
            authenticationCore.authorize()
        }.onSuccess {
            authenticateHandlerProviderManager.setAuthenticatorsInThisStep(
                authenticationStateProviderManager.handleAuthenticationFlowResult(
                    it!!,
                    context
                )
            )
        }.onFailure {
            authenticationStateProviderManager.emitAuthenticationState(AuthenticationState.Error(it))
        }
    }

    /**
     * Authenticate the user with the username and password.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param username The username of the user
     * @param password The password of the user
     */
    override suspend fun authenticateWithUsernameAndPassword(
        context: Context,
        authenticatorId: String,
        username: String,
        password: String
    ) {
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticatorId,
            authenticatorTypeString = AuthenticatorTypes.BASIC_AUTHENTICATOR.authenticatorType
        ) {
            authenticateHandlerProviderManager.commonAuthenticate(
                context,
                userSelectedAuthenticator = it,
                authParams = BasicAuthenticatorAuthParams(username, password)
            )
        }
    }

    /**
     * Authenticate the user with the TOTP token.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param token The TOTP token of the user
     */
    override suspend fun authenticateWithTotp(
        context: Context,
        authenticatorId: String,
        token: String
    ) {
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticatorId,
            authenticatorTypeString = AuthenticatorTypes.TOTP_AUTHENTICATOR.authenticatorType
        ) {
            authenticateHandlerProviderManager.commonAuthenticate(
                context,
                userSelectedAuthenticator = it,
                authParams = TotpAuthenticatorTypeAuthParams(token = token)
            )
        }
    }

    /**
     * Authenticate the user with the Email OTP authenticator.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param otpCode The OTP code received to the user
     */
    override suspend fun authenticateWithEmailOTP(
        context: Context,
        authenticatorId: String,
        otpCode: String
    ) {
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticatorId,
            authenticatorTypeString = AuthenticatorTypes.EMAIL_OTP_AUTHENTICATOR.authenticatorType
        ) {
            authenticateHandlerProviderManager.commonAuthenticate(
                context,
                userSelectedAuthenticator = it,
                authParams = EmailOTPAuthenticatorTypeAuthParams(otpCode = otpCode)
            )
        }
    }

    /**
     * Authenticate the user with the SMS OTP authenticator.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param otpCode The OTP code received to the user
     */
    override suspend fun authenticateWithSMSOTP(
        context: Context,
        authenticatorId: String,
        otpCode: String
    ) {
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticatorId,
            authenticatorTypeString = AuthenticatorTypes.SMS_OTP_AUTHENTICATOR.authenticatorType
        ) {
            authenticateHandlerProviderManager.commonAuthenticate(
                context,
                userSelectedAuthenticator = it,
                authParams = SMSOTPAuthenticatorTypeAuthParams(otpCode = otpCode)
            )
        }
    }

    /**
     * Authenticate the user with the selected authenticator which requires a redirect URI.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param authenticatorTypeString The authenticator type of the selected authenticator
     */
    private suspend fun authenticateWithRedirectUri(
        context: Context,
        authenticatorId: String,
        authenticatorTypeString: String
    ) {
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticatorId,
            authenticatorTypeString = authenticatorTypeString
        ) {
            authenticateHandlerProviderManager.redirectAuthenticate(context, it)
        }
    }

    /**
     * Authenticate the user with the OpenID Connect authenticator.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     */
    override suspend fun authenticateWithOpenIdConnect(context: Context, authenticatorId: String) {
        authenticateWithRedirectUri(
            context,
            authenticatorId = authenticatorId,
            authenticatorTypeString = AuthenticatorTypes.OPENID_CONNECT_AUTHENTICATOR.authenticatorType
        )
    }

    /**
     * Authenticate the user with the Github authenticator (Redirect).
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     */
    override suspend fun authenticateWithGithub(context: Context, authenticatorId: String) {
        authenticateWithRedirectUri(
            context,
            authenticatorId = authenticatorId,
            authenticatorTypeString =
            AuthenticatorTypes.GITHUB_REDIRECT_AUTHENTICATOR.authenticatorType
        )
    }

    /**
     * Authenticate the user with the Microsoft authenticator (Redirect).
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     */
    override suspend fun authenticateWithMicrosoft(
        context: Context,
        authenticatorId: String
    ) {
        authenticateWithRedirectUri(
            context,
            authenticatorId = authenticatorId,
            authenticatorTypeString =
            AuthenticatorTypes.MICROSOFT_REDIRECT_AUTHENTICATOR.authenticatorType
        )
    }

    /**
     * Authenticate the user with the Google authenticator using the Credential Manager API.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun authenticateWithGoogleNative(context: Context, authenticatorId: String) {
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticatorId,
            authenticatorTypeString = AuthenticatorTypes.GOOGLE_AUTHENTICATOR.authenticatorType
        ) {
            authenticateHandlerProviderManager.googleAuthenticate(
                context,
                it.metadata?.additionalData?.nonce!!
            )
        }
    }

    /**
     * Authenticate the user with the Google authenticator using the legacy one tap method.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param googleAuthenticateResultLauncher The result launcher for the Google authentication process
     */
    override suspend fun authenticateWithGoogleNativeLegacy(
        context: Context,
        authenticatorId: String,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    ) {
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticatorId,
            authenticatorTypeString = AuthenticatorTypes.GOOGLE_AUTHENTICATOR.authenticatorType
        ) {
            authenticateHandlerProviderManager.googleLegacyAuthenticate(
                context,
                googleAuthenticateResultLauncher
            )
        }
    }

    /**
     * Handle the Google authentication result.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * @param context The context of the application
     * @param resultCode The result code of the Google authentication process
     * @param data The [Intent] object that contains the result of the Google authentication process
     */
    override suspend fun handleGoogleNativeLegacyAuthenticateResult(
        context: Context,
        resultCode: Int,
        data: Intent
    ) {
        authenticateHandlerProviderManager.handleGoogleNativeLegacyAuthenticateResult(
            context,
            resultCode,
            data
        )
    }

    /**
     * Authenticate the user with the Passkey authenticator.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application.
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param allowCredentials The list of allowed credentials. Default is empty array.
     * @param timeout Timeout for the authentication. Default is 300000.
     * @param userVerification User verification method. Default is "required"
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun authenticateWithPasskey(
        context: Context,
        authenticatorId: String,
        allowCredentials: List<String>?,
        timeout: Long?,
        userVerification: String?
    ) {
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticatorId,
            authenticatorTypeString = AuthenticatorTypes.PASSKEY_AUTHENTICATOR.authenticatorType
        ) {
            authenticateHandlerProviderManager.passkeyAuthenticate(
                context,
                it,
                allowCredentials,
                timeout,
                userVerification
            )
        }
    }

    /**
     * Select the authenticator to authenticate the user. This method will select the authenticator
     * to authenticate the user and get the details of the selected authenticator.
     *
     * This method will only select the authenticator and will not authenticate the user.
     * This method should be called before calling the [authenticate] method, and does not require
     * to call before other specific authenticate methods.
     *
     * Example:
     * Assume you want to authenticate the user with the Email OTP authenticator as a first factor
     * authenticator.
     * ```
     * val detailedAuthenticator = authenticationProvider.selectAuthenticator(authenticator = authenticator)
     * ...
     * // for username
     * authenticationProvider.authenticate(
     * context = context,
     *        authenticator: detailedAuthenticator,
     *        authParams = <Parameters as a LinkedHashMap>
     * )
     *
     * ...
     *
     * // for email otp
     * authenticationProvider.authenticate(
     * context = context,
     *        authenticator: detailedAuthenticator,
     *        authParams = <Parameters as a LinkedHashMap>
     * )
     * ```
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     *
     * @param authenticator The selected authenticator
     *
     * @return The selected authenticator with the details as a [Authenticator] object
     */
    override suspend fun selectAuthenticator(authenticator: Authenticator): Authenticator? =
        authenticateHandlerProviderManager.selectAuthenticator(
            authenticatorId = authenticator.authenticatorId,
            authenticatorTypeString = authenticator.authenticator!!,
            afterSelectingAuthenticator = {}
        )

    /**
     * Authenticate the user with the selected authenticator. This method will `only authenticate`
     * the user with the selected authenticator and the authentication parameters.
     *
     * Before calling this method, the [selectAuthenticator] method should be called to select the
     * authenticator to authenticate the user.
     *
     * Example:
     * Assume you want to authenticate the user with the Email OTP authenticator as a first factor
     * authenticator.
     * ```
     * val detailedAuthenticator = authenticationProvider.selectAuthenticator(authenticator = authenticator)
     * ...
     * // for username
     * authenticationProvider.authenticate(
     * context = context,
     *        authenticator: detailedAuthenticator,
     *        authParams = <Parameters as a LinkedHashMap>
     * )
     *
     * ...
     *
     * // for email otp
     * authenticationProvider.authenticate(
     * context = context,
     *        authenticator: detailedAuthenticator,
     *        authParams = <Parameters as a LinkedHashMap>
     * )
     * ```
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     *
     * @param context The context of the application
     * @param detailedAuthenticator The detailed authenticator of the selected authenticator
     * @param authParams The authentication parameters of the selected authenticator
     * as a LinkedHashMap<String, String> with the key as the parameter name and the value as the
     * parameter value
     */
    override suspend fun authenticate(
        context: Context,
        detailedAuthenticator: Authenticator?,
        authParams: LinkedHashMap<String, String>
    ) {
        authenticateHandlerProviderManager.commonAuthenticate(
            context,
            userSelectedAuthenticator = detailedAuthenticator,
            authParamsAsMap = authParams
        )
    }

    /**
     * Logout the user from the application.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     *
     * emit: [AuthenticationState.Initial] - The user is not authenticated to access the application
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     *
     * @param context The context of the application
     */
    override suspend fun logout(context: Context) {
        authenticationStateProviderManager.emitAuthenticationState(AuthenticationState.Loading)

        runCatching {
            val idToken: String? = authenticationCore.getIDToken(context)
            // Call the logout endpoint
            authenticationCore.logout(idToken!!)

            // Sign out from google legacy if the user is signed in from google legacy
            nativeAuthenticationHandlerCore.handleGoogleNativeLegacyAuthenticationLogout(context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                // Sign out from google if the user is signed in from google
                nativeAuthenticationHandlerCore.handleGoogleNativeAuthenticationLogout(context)
                // Sign out from passkey if the user is signed in from passkey
                nativeAuthenticationHandlerCore.handlePasskeyAuthenticationLogout(context)
            }

            // clear the tokens
            authenticationCore.clearTokens(context)
        }.onSuccess {
            authenticationStateProviderManager.emitAuthenticationState(AuthenticationState.Initial)
        }.onFailure {
            authenticationStateProviderManager.emitAuthenticationState(
                AuthenticationState.Error(it)
            )
        }
    }
}
