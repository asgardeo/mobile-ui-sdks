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

package io.asgardeo.android.core.provider.provider_managers.authentication

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.state.AuthenticationState
import kotlinx.coroutines.flow.SharedFlow

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
 */
internal interface AuthenticationProviderManager {
    /**
     * Get authentication state flow of the authentication state which is exposed to the outside.
     *
     * @return authentication state flow [SharedFlow<AuthenticationState>]
     */
    fun getAuthenticationStateFlow(): SharedFlow<AuthenticationState>

    /**
     * Check whether the user is logged in or not.
     *
     * @param context The context of the application
     *
     * @return `true` if the user is logged in, `false` otherwise
     */
    suspend fun isLoggedIn(context: Context): Boolean

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
    suspend fun isLoggedInStateFlow(context: Context)

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
    suspend fun initializeAuthentication(context: Context)

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
    suspend fun authenticateWithUsernameAndPassword(
        context: Context,
        authenticatorId: String,
        username: String,
        password: String
    )

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
    suspend fun authenticateWithTotp(context: Context, authenticatorId: String, token: String)

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
    suspend fun authenticateWithEmailOTP(context: Context, authenticatorId: String, otpCode: String)

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
    suspend fun authenticateWithSMSOTP(context: Context, authenticatorId: String, otpCode: String)

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
    suspend fun authenticateWithOpenIdConnect(context: Context, authenticatorId: String)

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
    suspend fun authenticateWithGithub(context: Context, authenticatorId: String)

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
    suspend fun authenticateWithMicrosoft(context: Context, authenticatorId: String)

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
    suspend fun authenticateWithGoogleNative(context: Context, authenticatorId: String)

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
    suspend fun authenticateWithGoogleNativeLegacy(
        context: Context,
        authenticatorId: String,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    )

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
    suspend fun handleGoogleNativeLegacyAuthenticateResult(
        context: Context,
        resultCode: Int,
        data: Intent
    )

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
    suspend fun authenticateWithPasskey(
        context: Context,
        authenticatorId: String,
        allowCredentials: List<String>?,
        timeout: Long?,
        userVerification: String?
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
     *        context = context,
     *        authenticator: detailedAuthenticator,
     *        authParams = <Parameters as a LinkedHashMap>
     * )
     *
     * ...
     *
     * // for email otp
     * authenticationProvider.authenticate(
     *        context = context,
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
    suspend fun selectAuthenticator(authenticator: Authenticator): Authenticator?

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
    suspend fun authenticate(
        context: Context,
        detailedAuthenticator: Authenticator?,
        authParams: LinkedHashMap<String, String>
    )

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
    suspend fun logout(context: Context)
}
