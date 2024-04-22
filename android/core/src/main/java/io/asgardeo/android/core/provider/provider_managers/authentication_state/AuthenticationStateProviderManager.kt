package io.asgardeo.android.core.provider.provider_managers.authentication_state

import android.content.Context
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlow
import io.asgardeo.android.core.models.state.AuthenticationState
import kotlinx.coroutines.flow.SharedFlow

/**
 * Authentication state provider manager that is used to manage the authentication state.
 * It is responsible for providing the authentication state and handling the authentication flow.
 *
 * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
 * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
 * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
 * emit: [AuthenticationState.Error] - An error occurred during the authentication process
 */
interface AuthenticationStateProviderManager {
    /**
     * Get authentication state flow
     *
     * @return authentication state flow [SharedFlow<AuthenticationState>]
     */
    fun getAuthenticationStateFlow(): SharedFlow<AuthenticationState>

    /**
     * Emit the authentication state.
     *
     * @param state The [AuthenticationState] to emit
     */
    fun emitAuthenticationState(state: AuthenticationState)

    /**
     * Handle the authentication flow result.
     *
     * @param authenticationFlow The [AuthenticationFlow] to handle
     * @param context The context of the application
     *
     * @return The list of [Authenticator] to use next
     */
    suspend fun handleAuthenticationFlowResult(
        authenticationFlow: AuthenticationFlow,
        context: Context
    ): ArrayList<Authenticator>?
}