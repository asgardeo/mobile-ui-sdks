package io.asgardeo.android.core.asgardeo_auth

import io.asgardeo.android.core.asgardeo_auth.di.AsgardeoAuthContainer
import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.core.core_types.authentication.impl.AuthenticationCore
import io.asgardeo.android.core.core.core_types.native_authentication_handler.NativeAuthenticationHandlerCoreDef
import io.asgardeo.android.core.core_config.providers.authentication_core_config_provider.AuthenticationCoreConfigProvider
import io.asgardeo.android.core.provider.di.AuthenticationProviderImplContainer
import io.asgardeo.android.core.provider.di.TokenProviderImplContainer
import io.asgardeo.android.core.provider.providers.authentication.AuthenticationProvider
import io.asgardeo.android.core.provider.providers.authentication.impl.AuthenticationProviderImpl
import io.asgardeo.android.core.provider.providers.token.TokenProvider
import io.asgardeo.android.core.provider.providers.token.impl.TokenProviderImpl
import java.lang.ref.WeakReference

/**
 * The [AsgardeoAuth] class act as the entry point for the SDK.
 * This class will initialize the [AuthenticationProvider] and [TokenProvider] instances,
 * which will be used throughout the application for authentication and token management.
 *
 * @param authenticationCoreConfig Configuration of the Authenticator [AuthenticationCoreConfig]
 */
class AsgardeoAuth private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig
) {
    /**
     * Instance of the [AuthenticationCoreConfigProvider] that will be used throughout the application
     */
    private val authenticationCoreConfigProvider: AuthenticationCoreConfigProvider by lazy {
        AsgardeoAuthContainer.getAuthenticationCoreConfigProvider(authenticationCoreConfig)
    }

    /**
     * Instance of the [AuthenticationCore] that will be used throughout the application
     */
    private val authenticationCore: AuthenticationCoreDef by lazy {
        AsgardeoAuthContainer.getAuthenticationCoreDef(authenticationCoreConfigProvider)
    }

    /**
     * Instance of the [NativeAuthenticationHandlerCoreDef] that will be used throughout the application
     */
    private val nativeAuthenticationHandlerCore: NativeAuthenticationHandlerCoreDef by lazy {
        AsgardeoAuthContainer.getNativeAuthenticationHandlerCoreDef(authenticationCoreConfigProvider)
    }

    companion object {
        /**
         * Instance of the [AsgardeoAuth] that will be used throughout the application
         */
        private var asgardeoAuthInstance = WeakReference<AsgardeoAuth?>(null)

        /**
         * Initialize the [AsgardeoAuth] instance and return the instance.
         *
         * @param authenticationCoreConfig Configuration of the Authenticator [AuthenticationCoreConfig]
         *
         * @return Initialized [AuthenticationCore] instance
         */
        fun getInstance(authenticationCoreConfig: AuthenticationCoreConfig): AsgardeoAuth {
            var asgardeoAuth = asgardeoAuthInstance.get()
            if (asgardeoAuth == null) {
                asgardeoAuth = AsgardeoAuth(authenticationCoreConfig)
                asgardeoAuthInstance = WeakReference(asgardeoAuth)
            }
            return asgardeoAuth
        }

        /**
         * Get the [AsgardeoAuth] instance.
         * This method will return null if the [AsgardeoAuth] instance is not initialized.
         *
         * @return [AsgardeoAuth] instance
         */
        fun getInstance(): AsgardeoAuth? = asgardeoAuthInstance.get()
    }

    /**
     * Get the [AuthenticationProvider] instance. This instance will be used for authentication.
     *
     * @return [AuthenticationProvider] instance
     */
    fun getAuthenticationProvider(): AuthenticationProvider =
        AuthenticationProviderImpl.getInstance(
            AuthenticationProviderImplContainer.getAuthenticationProviderManager(
                authenticationCore,
                nativeAuthenticationHandlerCore
            ),
            AuthenticationProviderImplContainer.getUserProviderManager(authenticationCore)
        )

    /**
     * Get the [TokenProvider] instance. This instance will be used for token management.
     *
     * @return [TokenProvider] instance
     */
    fun getTokenProvider(): TokenProvider = TokenProviderImpl.getInstance(
        TokenProviderImplContainer.getTokenProviderManager(authenticationCore)
    )
}
