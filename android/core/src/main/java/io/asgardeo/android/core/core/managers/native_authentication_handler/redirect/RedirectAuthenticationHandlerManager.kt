package io.asgardeo.android.core.core.managers.native_authentication_handler.redirect

import android.content.Context
import android.net.Uri
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.exceptions.RedirectAuthenticationException

/**
 * This manager is responsible for handling the redirect authentication process
 * using the redirection prompt type
 */
interface RedirectAuthenticationHandlerManager {
    /**
     * Redirect the user to the authenticator's authentication page.
     *
     * @param context The context of the application
     * @param authenticator The authenticator to redirect the user
     */
    suspend fun redirectAuthenticate(
        context: Context,
        authenticator: Authenticator
    ): LinkedHashMap<String, String>?

    /**
     * Handle the redirect URI and authenticate the user with the selected authenticator.
     *
     * @param context The context of the application
     * @param deepLink The deep link URI that is received from the redirect URI
     *
     * @return The authentication parameters extracted from the redirect URI
     */
    fun handleRedirectUri(context: Context, deepLink: Uri)

    /**
     * Handle the cancel event of the redirect authentication process.
     *
     * @throws [RedirectAuthenticationException] with the message [RedirectAuthenticationException.AUTHENTICATION_CANCELLED]
     */
    fun handleRedirectAuthenticationCancel()
}
