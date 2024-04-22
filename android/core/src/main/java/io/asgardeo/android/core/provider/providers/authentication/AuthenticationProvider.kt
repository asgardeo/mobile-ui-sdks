package io.asgardeo.android.core.provider.providers.authentication

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.state.AuthenticationState
import kotlinx.coroutines.flow.SharedFlow

/**
 * Interface for the authentication provider to be implemented by the application.
 * This has the methods to handle the authentication process.
 */
interface AuthenticationProvider {
    /**
     * Get authentication state flow
     *
     * @return authentication state flow [SharedFlow<AuthenticationState>]
     */
    fun getAuthenticationStateFlow(): SharedFlow<AuthenticationState>

    /**
     * Check whether the user is logged in or not.
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
    suspend fun authenticateWithUsernameAndPassword(
        context: Context,
        authenticatorId: String,
        username: String,
        password: String
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
    suspend fun authenticateWithTotp(
        context: Context,
        authenticatorId: String,
        token: String
    )

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
    suspend fun authenticateWithEmailOtp(
        context: Context,
        authenticatorId: String,
        otpCode: String
    )

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
    suspend fun authenticateWithSMSOtp(
        context: Context,
        authenticatorId: String,
        otpCode: String
    )

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
    suspend fun authenticateWithOpenIdConnect(context: Context, authenticatorId: String)

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
    suspend fun authenticateWithGithub(context: Context, authenticatorId: String)

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
    suspend fun authenticateWithMicrosoft(context: Context, authenticatorId: String)

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
    suspend fun authenticateWithGoogleNative(context: Context, authenticatorId: String)

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
    suspend fun authenticateWithGoogleNativeLegacy(
        context: Context,
        authenticatorId: String,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
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
    suspend fun handleGoogleNativeLegacyAuthenticateResult(
        context: Context,
        resultCode: Int,
        data: Intent
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
    suspend fun selectAuthenticator(authenticator: Authenticator): Authenticator?

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
    suspend fun authenticate(
        context: Context,
        detailedAuthenticator: Authenticator?,
        authParams: LinkedHashMap<String, String>
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
    suspend fun authenticateWithPasskey(
        context: Context,
        authenticatorId: String,
        allowCredentials: List<String>? = emptyList(),
        timeout: Long? = null,
        userVerification: String? = null
    )

    /**
     * Get the basic user information of the authenticated.
     *
     * @param context The context of the application
     *
     * @return User details as a [LinkedHashMap]
     */
    suspend fun getBasicUserInfo(context: Context): LinkedHashMap<String, Any>?

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
