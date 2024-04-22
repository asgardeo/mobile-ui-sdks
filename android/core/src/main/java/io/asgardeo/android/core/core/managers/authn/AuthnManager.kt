package io.asgardeo.android.core.core.managers.authn

import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlow

/**
 * Interface which has the methods to initiate the authorization and authentication flows.
 */
internal interface AuthnManager {

    /**
     * Authorize the application.
     * This method will call the authorization endpoint and get the authenticators available for the
     * first step in the authentication flow.
     */
    suspend fun authorize(): AuthenticationFlow?

    /**
     * Send the authentication parameters to the authentication endpoint and get the next step of the
     * authentication flow. If the authentication flow has only one step, this method will return
     * the success response of the authentication flow if the authentication is successful.
     *
     * @param authenticator Detailed object of the selected authenticator
     * @param authenticatorParameters Authenticator parameters of the selected authenticator
     * as a LinkedHashMap<String, String> with the key as the parameter name and the value as the
     * parameter value
     *
     * @return [AuthenticationFlow] with the next step of the authentication flow
     */
    suspend fun authn(
        authenticator: Authenticator,
        authenticatorParameters: LinkedHashMap<String, String>,
    ): AuthenticationFlow?
}
