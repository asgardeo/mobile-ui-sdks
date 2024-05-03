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

package io.asgardeo.android.core.provider.providers.authentication.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.state.AuthenticationState
import io.asgardeo.android.core.provider.provider_managers.authentication.AuthenticationProviderManager
import io.asgardeo.android.core.provider.provider_managers.user.UserProviderManager
import io.asgardeo.android.core.provider.providers.authentication.AuthenticationProvider
import kotlinx.coroutines.flow.SharedFlow
import java.lang.ref.WeakReference

/**
 * Authentication provider class that is used to manage the authentication process.
 *
 * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
 *
 * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
 *
 * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
 *
 * emit: [AuthenticationState.Error] - An error occurred during the authentication process
 *
 * @param authenticationProviderManager The [AuthenticationProviderManager] instance
 */
internal class AuthenticationProviderImpl private constructor(
    private val authenticationProviderManager: AuthenticationProviderManager,
    private val userProviderManager: UserProviderManager
) : AuthenticationProvider {

    companion object {
        /**
         * Instance of the [AuthenticationProviderImpl] that will be used throughout the application
         */
        private var authenticationProviderImplInstance: WeakReference<AuthenticationProviderImpl> =
            WeakReference(null)

        /**
         * Initialize the [AuthenticationProviderImpl] instance and return the instance.
         *
         * @param authenticationProviderManager The [AuthenticationProviderManager] instance
         *
         * @return The [AuthenticationProviderImpl] instance
         */
        fun getInstance(
            authenticationProviderManager: AuthenticationProviderManager,
            userProviderManager: UserProviderManager
        ): AuthenticationProviderImpl {
            var authenticatorProvider = authenticationProviderImplInstance.get()
            if (authenticatorProvider == null) {
                authenticatorProvider = AuthenticationProviderImpl(
                    authenticationProviderManager,
                    userProviderManager
                )
                authenticationProviderImplInstance = WeakReference(authenticatorProvider)
            }
            return authenticatorProvider
        }

        /**
         * Get the [AuthenticationProviderImpl] instance.
         *
         * @return The [AuthenticationProviderImpl] instance
         */
        fun getInstance(): AuthenticationProviderImpl? = authenticationProviderImplInstance.get()
    }

    /**
     * Get authentication state flow
     *
     * @return authentication state flow [SharedFlow<AuthenticationState>]
     */
    override fun getAuthenticationStateFlow(): SharedFlow<AuthenticationState> =
        authenticationProviderManager.getAuthenticationStateFlow()

    /**
     * Check whether the user is logged in or not.
     *
     * @return `true` if the user is logged in, `false` otherwise
     */
    override suspend fun isLoggedIn(context: Context): Boolean =
        authenticationProviderManager.isLoggedIn(context)

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
    override suspend fun isLoggedInStateFlow(context: Context) =
        authenticationProviderManager.isLoggedInStateFlow(context)

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
    override suspend fun initializeAuthentication(context: Context) =
        authenticationProviderManager.initializeAuthentication(context)

    /**
     * Authenticate the user with the username and password.
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
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param username The username of the user
     * @param password The password of the user
     */
    override suspend fun authenticateWithUsernameAndPassword(
        context: Context,
        authenticatorId: String,
        username: String,
        password: String
    ) = authenticationProviderManager.authenticateWithUsernameAndPassword(
        context,
        authenticatorId,
        username,
        password
    )

    /**
     * Authenticate the user with the TOTP token, only if the TOTP not added as a first factor authenticator.
     * If the TOTP is added as a first factor authenticator, use the [authenticate] method to authenticate the user.
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
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param token The TOTP token of the user
     */
    override suspend fun authenticateWithTotp(
        context: Context,
        authenticatorId: String,
        token: String
    ) = authenticationProviderManager.authenticateWithTotp(context, authenticatorId, token)

    /**
     * Authenticate the user with the Email OTP authenticator, only if the Email OTP not added as a first factor authenticator.
     * If the Email OTP is added as a first factor authenticator, use the [authenticate] method to authenticate the user.
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
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param otpCode The OTP code of the user
     */
    override suspend fun authenticateWithEmailOtp(
        context: Context,
        authenticatorId: String,
        otpCode: String
    ) = authenticationProviderManager.authenticateWithEmailOTP(context, authenticatorId, otpCode)

    /**
     * Authenticate the user with the SMS OTP authenticator, only if the SMS OTP not added as a first factor authenticator.
     * If the SMS OTP is added as a first factor authenticator, use the [authenticate] method to authenticate the user.
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
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param otpCode The OTP code of the user
     */
    override suspend fun authenticateWithSMSOtp(
        context: Context,
        authenticatorId: String,
        otpCode: String
    ) = authenticationProviderManager.authenticateWithSMSOTP(context, authenticatorId, otpCode)

    /**
     * Authenticate the user with the OpenID Connect authenticator.
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
     * @param authenticatorId The authenticator id of the selected authenticator
     */
    override suspend fun authenticateWithOpenIdConnect(context: Context, authenticatorId: String) =
        authenticationProviderManager.authenticateWithOpenIdConnect(context, authenticatorId)

    /**
     * Authenticate the user with the Github authenticator (Redirect).
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
     * @param authenticatorId The authenticator id of the selected authenticator
     */
    override suspend fun authenticateWithGithub(context: Context, authenticatorId: String) =
        authenticationProviderManager.authenticateWithGithub(context, authenticatorId)

    /**
     * Authenticate the user with the Microsoft authenticator (Redirect).
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
     * @param authenticatorId The authenticator id of the selected authenticator
     */
    override suspend fun authenticateWithMicrosoft(context: Context, authenticatorId: String) =
        authenticationProviderManager.authenticateWithMicrosoft(context, authenticatorId)

    /**
     * Authenticate the user with the Google authenticator.
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
     * @param authenticatorId The authenticator id of the selected authenticator
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun authenticateWithGoogleNative(context: Context, authenticatorId: String) =
        authenticationProviderManager.authenticateWithGoogleNative(context, authenticatorId)

    /**
     * Authenticate the user with the Google authenticator using the legacy one tap method.
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
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param googleAuthenticateResultLauncher The result launcher for the Google authentication process
     */
    override suspend fun authenticateWithGoogleNativeLegacy(
        context: Context,
        authenticatorId: String,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    ) = authenticationProviderManager.authenticateWithGoogleNativeLegacy(
        context,
        authenticatorId,
        googleAuthenticateResultLauncher
    )

    /**
     * Handle the Google authentication result.
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     *
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     *
     * @param context The context of the application
     * @param resultCode The result code of the Google authentication process
     * @param data The [Intent] object that contains the result of the Google authentication process
     */
    override suspend fun handleGoogleNativeLegacyAuthenticateResult(
        context: Context,
        resultCode: Int,
        data: Intent
    ) = authenticationProviderManager.handleGoogleNativeLegacyAuthenticateResult(
        context,
        resultCode,
        data
    )

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
        authenticationProviderManager.selectAuthenticator(authenticator)

    /**
     * Authenticate the user with the selected authenticator.
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
     * @param detailedAuthenticator The detailed authenticator object of the selected authenticator
     * @param authParams The authentication parameters of the selected authenticator
     * as a LinkedHashMap<String, String> with the key as the parameter name and the value as the
     * parameter value
     */
    override suspend fun authenticate(
        context: Context,
        detailedAuthenticator: Authenticator?,
        authParams: LinkedHashMap<String, String>
    ) = authenticationProviderManager.authenticate(
        context,
        detailedAuthenticator,
        authParams
    )

    /**
     * Authenticate the user with the Passkey authenticator.
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
    ) = authenticationProviderManager.authenticateWithPasskey(
        context,
        authenticatorId,
        allowCredentials,
        timeout,
        userVerification
    )

    /**
     * Get the basic user information of the authenticated.
     *
     * @param context The context of the application
     *
     * @return User details as a [LinkedHashMap]
     */
    override suspend fun getBasicUserInfo(context: Context): LinkedHashMap<String, Any>? =
        userProviderManager.getBasicUserInfo(context)

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
    override suspend fun logout(context: Context) = authenticationProviderManager.logout(context)
}