package io.asgardeo.android.core.provider.di

import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.provider.provider_managers.authentication_state.AuthenticationStateProviderManager
import io.asgardeo.android.core.provider.provider_managers.authentication_state.impl.AuthenticationStateProviderManagerImpl

/**
 * Dependency Injection container for [AuthenticationStateProviderManagerImpl]
 */
internal object AuthenticateHandlerProviderManagerImplContainer {
    /**
     * Get the [AuthenticationStateProviderManager] instance
     *
     * @param authenticationCore The [AuthenticationCoreDef] instance
     *
     * @return The [AuthenticationStateProviderManager] instance
     */
    internal fun getAuthenticateStateProviderManager(
        authenticationCore: AuthenticationCoreDef
    ): AuthenticationStateProviderManager = AuthenticationStateProviderManagerImpl.getInstance(
        authenticationCore
    )
}
