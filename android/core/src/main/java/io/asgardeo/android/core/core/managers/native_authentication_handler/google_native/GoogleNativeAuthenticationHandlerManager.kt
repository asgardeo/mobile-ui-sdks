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
