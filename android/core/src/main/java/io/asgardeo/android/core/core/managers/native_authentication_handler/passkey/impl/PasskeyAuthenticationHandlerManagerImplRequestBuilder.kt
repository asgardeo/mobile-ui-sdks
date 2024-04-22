package io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.impl

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption

/**
 * Request builder class for the [PasskeyAuthenticationHandlerManagerImpl]
 * This class is responsible for building the requests for the Passkey Authentication
 * using the Credential Manager API
 */
object PasskeyAuthenticationHandlerManagerImplRequestBuilder {
    /**
     * Build the request to authenticate the user with Passkey using the Credential Manager API
     *
     * @param publicKeyCredentialOption [GetPublicKeyCredentialOption]
     *
     * @return [GetCredentialRequest] to authenticate the user with Google
     */
    internal fun getAuthenticateWithPasskeyRequestBuilder(
        publicKeyCredentialOption: GetPublicKeyCredentialOption
    ): GetCredentialRequest = GetCredentialRequest(
        listOf(publicKeyCredentialOption)
    )

    /**
     * Build the request to logout the user using the passkey
     *
     * @return [ClearCredentialStateRequest] to logout the user from the google account
     */
    internal fun getPasskeyLogoutRequestBuilder(): ClearCredentialStateRequest =
        ClearCredentialStateRequest()
}
