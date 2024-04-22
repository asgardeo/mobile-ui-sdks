package io.asgardeo.android.core.core.managers.authenticator

import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.exceptions.AuthenticatorException

/**
 * Authenticator manager interface.
 * This interface is responsible for handling the authenticator related operations.
 */
internal interface AuthenticatorManager {
    /**
     * Get full details of the authenticator.
     *
     * @param flowId Flow id of the authentication flow
     * @param authenticator Authenticator object of the selected authenticator
     *
     * @return Authenticator type with full details [Authenticator]
     *
     * @throws AuthenticatorException
     */
    suspend fun getDetailsOfAuthenticator(
        flowId: String,
        authenticator: Authenticator
    ): Authenticator

    /**
     * Remove the instance of the [AuthenticatorManager]
     */
    fun dispose()
}
