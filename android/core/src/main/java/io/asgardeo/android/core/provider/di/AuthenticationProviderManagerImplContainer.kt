package io.asgardeo.android.core.provider.di

import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.core.core_types.native_authentication_handler.NativeAuthenticationHandlerCoreDef
import io.asgardeo.android.core.provider.provider_managers.authenticate_handler.AuthenticateHandlerProviderManager
import io.asgardeo.android.core.provider.provider_managers.authenticate_handler.impl.AuthenticateHandlerProviderManagerImpl
import io.asgardeo.android.core.provider.provider_managers.authentication.impl.AuthenticationProviderManagerImpl
import io.asgardeo.android.core.provider.provider_managers.authentication_state.AuthenticationStateProviderManager
import io.asgardeo.android.core.provider.provider_managers.authentication_state.impl.AuthenticationStateProviderManagerImpl

/**
 * Dependency Injection container for the [AuthenticationProviderManagerImpl] class.
 */
internal object AuthenticationProviderManagerImplContainer {

    /**
     * Get the instance of the [AuthenticationStateProviderManager].
     *
     * @param authenticationCore The [AuthenticationCoreDef] instance
     *
     * @return [AuthenticationStateProviderManager] instance
     */
    internal fun getAuthenticationStateProviderManager(authenticationCore: AuthenticationCoreDef)
            : AuthenticationStateProviderManager =
        AuthenticationStateProviderManagerImpl.getInstance(authenticationCore)

    /**
     * Get the instance of the [AuthenticateHandlerProviderManager].
     *
     * @param authenticationCore The [AuthenticationCoreDef] instance
     * @param nativeAuthenticationHandlerCore The [NativeAuthenticationHandlerCoreDef] instance
     *
     * @return [AuthenticateHandlerProviderManager] instance
     */
    internal fun getAuthenticationHandlerProviderManager(
        authenticationCore: AuthenticationCoreDef,
        nativeAuthenticationHandlerCore: NativeAuthenticationHandlerCoreDef
    ): AuthenticateHandlerProviderManager =
        AuthenticateHandlerProviderManagerImpl.getInstance(
            authenticationCore,
            nativeAuthenticationHandlerCore,
            AuthenticateHandlerProviderManagerImplContainer.getAuthenticateStateProviderManager(
                authenticationCore
            )
        )
}
