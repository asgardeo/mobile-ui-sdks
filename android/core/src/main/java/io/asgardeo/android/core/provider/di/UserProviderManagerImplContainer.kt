package io.asgardeo.android.core.provider.di

import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.provider.provider_managers.token.TokenProviderManager
import io.asgardeo.android.core.provider.provider_managers.token.impl.TokenProviderManagerImpl
import io.asgardeo.android.core.provider.provider_managers.user.impl.UserProviderManagerImpl

/**
 * Dependency Injection container for the [UserProviderManagerImpl] interface.
 */
internal object UserProviderManagerImplContainer {
    /**
     * Get the instance of the [TokenProviderManager].
     *
     * @param authenticationCore [AuthenticationCoreDef] instance
     *
     * @return [TokenProviderManager] instance
     */
    internal fun getTokenProviderManager(
        authenticationCore: AuthenticationCoreDef
    ): TokenProviderManager =
        TokenProviderManagerImpl.getInstance(authenticationCore)
}
