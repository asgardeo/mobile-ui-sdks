package io.asgardeo.android.core.core.managers.native_authentication_handler.passkey

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.asgardeo.android.core.models.auth_params.AuthParams

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
