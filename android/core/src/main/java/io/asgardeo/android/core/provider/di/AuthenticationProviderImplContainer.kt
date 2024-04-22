package io.asgardeo.android.core.provider.di

import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.core.core_types.native_authentication_handler.NativeAuthenticationHandlerCoreDef
import io.asgardeo.android.core.provider.provider_managers.authentication.AuthenticationProviderManager
import io.asgardeo.android.core.provider.provider_managers.authentication.impl.AuthenticationProviderManagerImpl
import io.asgardeo.android.core.provider.provider_managers.user.UserProviderManager
import io.asgardeo.android.core.provider.provider_managers.user.impl.UserProviderManagerImpl
import io.asgardeo.android.core.provider.providers.authentication.impl.AuthenticationProviderImpl

/**
 * Dependency Injection container for the [AuthenticationProviderImpl] class.
 */
internal object AuthenticationProviderImplContainer {
    /**
     * Get the instance of the [AuthenticationProviderManager].
     *
     * @param authenticationCore [AuthenticationCoreDef] instance
     * @param nativeAuthenticationHandlerCore [NativeAuthenticationHandlerCoreDef] instance
     *
     * @return [AuthenticationProviderManager] instance
     */
    internal fun getAuthenticationProviderManager(
        authenticationCore: AuthenticationCoreDef,
        nativeAuthenticationHandlerCore: NativeAuthenticationHandlerCoreDef
    ): AuthenticationProviderManager =
        AuthenticationProviderManagerImpl.getInstance(
            authenticationCore,
            nativeAuthenticationHandlerCore,
            AuthenticationProviderManagerImplContainer.getAuthenticationStateProviderManager(
                authenticationCore
            ),
            AuthenticationProviderManagerImplContainer.getAuthenticationHandlerProviderManager(
                authenticationCore,
                nativeAuthenticationHandlerCore
            )
        )

    /**
     * Get the instance of the [UserProviderManager].
     *
     * @param authenticationCore [AuthenticationCoreDef] instance
     *
     * @return [UserProviderManager] instance
     */
    internal fun getUserProviderManager(
        authenticationCore: AuthenticationCoreDef
    ): UserProviderManager =
        UserProviderManagerImpl.getInstance(
            authenticationCore,
            UserProviderManagerImplContainer.getTokenProviderManager(authenticationCore)
        )
}
