package io.asgardeo.android.core.core.managers.native_authentication_handler.google_native.impl

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

/**
 * Request builder class for the [GoogleNativeAuthenticationHandlerManagerImpl]
 * This class is responsible for building the requests for the Google Native Authentication
 * using the Credential Manager API
 */
object GoogleNativeAuthenticationHandlerManagerImplRequestBuilder {
    /**
     * Build the request to authenticate the user with Google using the Credential Manager API
     *
     * @param googleIdOptions [GetGoogleIdOption] to get the Google ID Token
     *
     * @return [GetCredentialRequest] to authenticate the user with Google
     */
    internal fun getAuthenticateWithGoogleNativeRequestBuilder(
        googleIdOptions: GetGoogleIdOption
    ): GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOptions)
        .build()

    /**
     * Build the request to logout the user from the google account
     *
     * @return [ClearCredentialStateRequest] to logout the user from the google account
     */
    internal fun getGoogleLogoutRequestBuilder(): ClearCredentialStateRequest =
        ClearCredentialStateRequest()
}
