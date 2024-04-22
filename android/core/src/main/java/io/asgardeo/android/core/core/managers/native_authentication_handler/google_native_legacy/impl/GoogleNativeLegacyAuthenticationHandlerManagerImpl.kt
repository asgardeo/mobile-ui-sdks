package io.asgardeo.android.core.core.managers.native_authentication_handler.google_native_legacy.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native.GoogleNativeAuthenticationHandlerManager
import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native_legacy.GoogleNativeLegacyAuthenticationHandlerManager
import io.asgardeo.android.core.models.auth_params.AuthParams
import io.asgardeo.android.core.models.auth_params.GoogleNativeAuthenticatorTypeAuthParams
import io.asgardeo.android.core.models.exceptions.GoogleNativeAuthenticationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Implementation of [GoogleNativeAuthenticationHandlerManager]
 * This manager is responsible for handling the Google Native Authentication using the
 * legacy one tap method
 *
 * @property authenticationCoreConfig [AuthenticationCoreConfig] to get the Google Web Client ID
 */
class GoogleNativeLegacyAuthenticationHandlerManagerImpl private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig,
) : GoogleNativeLegacyAuthenticationHandlerManager {
    companion object {
        /**
         * Instance of the [GoogleNativeLegacyAuthenticationHandlerManagerImpl] that will be used throughout the application
         */
        private var googleNativeLegacyAuthenticationHandlerManagerImplInstance:
                WeakReference<GoogleNativeLegacyAuthenticationHandlerManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [GoogleNativeLegacyAuthenticationHandlerManagerImpl] instance and return the instance.
         *
         * @param authenticationCoreConfig Configuration of the Authenticator [AuthenticationCoreConfig]
         *
         * @return Initialized [GoogleNativeLegacyAuthenticationHandlerManagerImpl] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig,
        ): GoogleNativeLegacyAuthenticationHandlerManagerImpl {
            var googleNativeLegacyAuthenticationHandlerImpl =
                googleNativeLegacyAuthenticationHandlerManagerImplInstance.get()
            if (googleNativeLegacyAuthenticationHandlerImpl == null) {
                googleNativeLegacyAuthenticationHandlerImpl =
                    GoogleNativeLegacyAuthenticationHandlerManagerImpl(authenticationCoreConfig)
                googleNativeLegacyAuthenticationHandlerManagerImplInstance =
                    WeakReference(googleNativeLegacyAuthenticationHandlerImpl)
            }
            return googleNativeLegacyAuthenticationHandlerImpl
        }
    }

    /**
     * Deferred object to wait for the result of the Google native authentication process using
     * the legacy method
     */
    private val googleNativeLegacyAuthenticationResultDeferred: CompletableDeferred<Unit> by lazy {
        CompletableDeferred()
    }

    /**
     * Authenticate the user with Google using the legacy one tap method.
     *
     * @param context [Context] of the application
     * @param googleAuthenticateResultLauncher [ActivityResultLauncher] to launch the Google authentication intent
     */
    override suspend fun authenticateWithGoogleNativeLegacy(
        context: Context,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    ) {
        // Get the Google Web Client ID
        val googleWebClientId: String? = authenticationCoreConfig.getGoogleWebClientId()

        if (googleWebClientId.isNullOrEmpty()) {
            throw GoogleNativeAuthenticationException(
                GoogleNativeAuthenticationException.GOOGLE_WEB_CLIENT_ID_NOT_SET
            )
        } else {
            val googleSignInClient = GoogleSignIn.getClient(
                context,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestServerAuthCode(googleWebClientId)
                    .requestIdToken(googleWebClientId)
                    .requestEmail()
                    .build()
            )
            val signInIntent = googleSignInClient.signInIntent

            googleAuthenticateResultLauncher.launch(signInIntent)

            googleNativeLegacyAuthenticationResultDeferred.await()
        }
    }

    /**
     * Handle the Google native authentication result.
     *
     * @param resultCode The result code of the Google authentication process
     * @param data The [Intent] object that contains the result of the Google authentication process
     *
     * @return The Google native authenticator parameters [LinkedHashMap] that contains the ID Token and the Auth Code
     */
    override suspend fun handleGoogleNativeLegacyAuthenticateResult(resultCode: Int, data: Intent)
            : AuthParams? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            if (resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(
                    data
                )
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    val idToken: String? = account.idToken
                    val authCode: String? = account.serverAuthCode

                    if (idToken.isNullOrEmpty() || authCode.isNullOrEmpty()) {
                        continuation.resumeWithException(
                            GoogleNativeAuthenticationException(
                                GoogleNativeAuthenticationException.GOOGLE_AUTH_CODE_OR_ID_TOKEN_NOT_FOUND
                            )
                        )
                    } else {
                        val googleNativeAuthenticatorParams: AuthParams =
                            GoogleNativeAuthenticatorTypeAuthParams(
                                accessToken = authCode,
                                idToken = idToken
                            )

                        continuation.resume(googleNativeAuthenticatorParams)
                    }
                } catch (e: ApiException) {
                    continuation.resumeWithException(e)
                }
            } else {
                continuation.resumeWithException(
                    GoogleNativeAuthenticationException(
                        GoogleNativeAuthenticationException.GOOGLE_AUTHENTICATION_FAILED
                    )
                )
            }
            googleNativeLegacyAuthenticationResultDeferred.complete(Unit)
        }
    }

    /**
     * Logout the user from the Google account
     *
     * @param context [Context] of the application
     */
    override fun logout(context: Context) {
        // Sign out from google if the user is signed in from google
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut()
    }
}
