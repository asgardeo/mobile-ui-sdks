package io.asgardeo.android.core.models.state

import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlow

/**
 * Authentication state of the application. This sealed class is used to represent the different
 * states of the authentication process
 *
 * States:
 * - [Initial]: The initial state of the application
 *
 * - [Unauthenticated]: The user is not authenticated to access the application
 *
 * - [Authenticated]: The user is authenticated to access the application
 *
 * - [Loading]: The application is in the process of loading the authentication state
 *
 * - [Error]: An error occurred during the authentication process
 */
sealed class AuthenticationState {
    data object Initial : AuthenticationState()
    data class Unauthenticated(val authenticationFlow: AuthenticationFlow?) : AuthenticationState()
    data object Authenticated : AuthenticationState()
    data object Loading : AuthenticationState()
    data class Error(val throwable: Throwable) : AuthenticationState()
}
