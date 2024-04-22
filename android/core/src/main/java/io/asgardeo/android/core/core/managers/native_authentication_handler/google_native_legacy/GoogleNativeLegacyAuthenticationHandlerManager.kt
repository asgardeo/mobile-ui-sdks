package io.asgardeo.android.core.core.managers.native_authentication_handler.google_native_legacy

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import io.asgardeo.android.core.models.auth_params.AuthParams

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
