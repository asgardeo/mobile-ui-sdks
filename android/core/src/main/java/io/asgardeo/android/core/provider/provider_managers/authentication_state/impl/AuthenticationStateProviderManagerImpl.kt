/*
 *  Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.asgardeo.android.core.provider.provider_managers.authentication_state.impl

import android.content.Context
import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlow
import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlowNotSuccess
import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlowSuccess
import io.asgardeo.android.core.models.flow_status.FlowStatus
import io.asgardeo.android.core.models.state.AuthenticationState
import io.asgardeo.android.core.models.state.TokenState
import io.asgardeo.android.core.provider.provider_managers.authentication_state.AuthenticationStateProviderManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.ref.WeakReference

/**
 * Authentication state provider manager that is used to manage the authentication state.
 *
 * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
 *
 * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
 *
 * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
 *
 * emit: [AuthenticationState.Error] - An error occurred during the authentication process
 *
 * @property authenticationCore The [AuthenticationCoreDef] instance
 */
internal class AuthenticationStateProviderManagerImpl private constructor(
    private val authenticationCore: AuthenticationCoreDef,
) : AuthenticationStateProviderManager {

    companion object {
        /**
         * Instance of the [AuthenticationStateProviderManagerImpl] to use in the
         * application
         */
        private var authenticationStateProviderManagerInstance:
                WeakReference<AuthenticationStateProviderManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [AuthenticationStateProviderManagerImpl] instance and return the instance.
         *
         * @param authenticationCore The [AuthenticationCoreDef] instance
         *
         * @return The [AuthenticationStateProviderManagerImpl] instance
         */
        fun getInstance(
            authenticationCore: AuthenticationCoreDef
        ): AuthenticationStateProviderManagerImpl {
            var authenticatorStateProviderManager = authenticationStateProviderManagerInstance.get()
            if (authenticatorStateProviderManager == null) {
                authenticatorStateProviderManager = AuthenticationStateProviderManagerImpl(
                    authenticationCore
                )
                authenticationStateProviderManagerInstance =
                    WeakReference(authenticatorStateProviderManager)
            }
            return authenticatorStateProviderManager
        }
    }

    // The authentication state flow
    private val _authenticationStateFlow = MutableStateFlow<AuthenticationState>(
        AuthenticationState.Initial
    )

    // The authentication state flow as a state flow
    private val authenticationStateFlow: StateFlow<AuthenticationState> = _authenticationStateFlow

    /**
     * Get authentication state flow
     *
     * @return authentication state flow [SharedFlow<AuthenticationState>]
     */
    override fun getAuthenticationStateFlow(): SharedFlow<AuthenticationState> =
        authenticationStateFlow

    /**
     * Emit the authentication state.
     *
     * @param state The [AuthenticationState] to emit
     */
    override fun emitAuthenticationState(state: AuthenticationState) {
        _authenticationStateFlow.tryEmit(state)
    }

    /**
     * Handle the authentication flow result.
     *
     * @param authenticationFlow The [AuthenticationFlow] to handle
     * @param context The context of the application
     *
     * @return The list of [Authenticator] to use next
     */
    override suspend fun handleAuthenticationFlowResult(
        authenticationFlow: AuthenticationFlow,
        context: Context
    ): ArrayList<Authenticator>? {
        when (authenticationFlow.flowStatus) {
            FlowStatus.SUCCESS.flowStatus -> {
                // Exchange the authorization code for the access token and save the tokens
                runCatching {
                    val tokenState: TokenState? = authenticationCore.exchangeAuthorizationCode(
                        (authenticationFlow as AuthenticationFlowSuccess).authData.code,
                        context
                    )
                    authenticationCore.saveTokenState(context, tokenState!!)
                }.onSuccess {
                    emitAuthenticationState(AuthenticationState.Authenticated)
                }.onFailure {
                    emitAuthenticationState(AuthenticationState.Error(it))
                }

                return null
            }

            else -> {
                emitAuthenticationState(AuthenticationState.Unauthenticated(authenticationFlow))

                return (authenticationFlow as AuthenticationFlowNotSuccess).nextStep.authenticators
            }
        }
    }
}