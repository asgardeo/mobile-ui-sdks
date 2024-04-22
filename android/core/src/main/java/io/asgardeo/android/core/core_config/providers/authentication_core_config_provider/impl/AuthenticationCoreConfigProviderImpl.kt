package io.asgardeo.android.core.core_config.providers.authentication_core_config_provider.impl

import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core_config.managers.authentication_core_config_manager.AuthenticationCoreConfigManager
import io.asgardeo.android.core.core_config.providers.authentication_core_config_provider.AuthenticationCoreConfigProvider
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference

/**
 * Implementation of [AuthenticationCoreConfigProvider]
 *
 * @property authenticationCoreConfigManager Manager to update the [AuthenticationCoreConfig] based on the discovery response
 */
class AuthenticationCoreConfigProviderImpl private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig,
    private val authenticationCoreConfigManager: AuthenticationCoreConfigManager
) : AuthenticationCoreConfigProvider {

    companion object {
        /**
         * Instance of the [AuthenticationCoreConfigProviderImpl] that will be used throughout the application
         */
        private var authenticationCoreConfigProviderImplInstance: WeakReference<AuthenticationCoreConfigProviderImpl> =
            WeakReference(null)

        /**
         * Initialize the [AuthenticationCoreConfigProviderImpl] instance and return the instance.
         *
         * @param authenticationCoreConfigManager Manager to update the [AuthenticationCoreConfig] based on the discovery response
         *
         * @return The [AuthenticationCoreConfigProviderImpl] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig,
            authenticationCoreConfigManager: AuthenticationCoreConfigManager
        ): AuthenticationCoreConfigProviderImpl {
            var authenticationCoreConfigProviderImpl =
                authenticationCoreConfigProviderImplInstance.get()
            if (authenticationCoreConfigProviderImpl == null) {
                authenticationCoreConfigProviderImpl = AuthenticationCoreConfigProviderImpl(
                    authenticationCoreConfig,
                    authenticationCoreConfigManager
                )
                authenticationCoreConfigProviderImplInstance =
                    WeakReference(authenticationCoreConfigProviderImpl)
            }
            return authenticationCoreConfigProviderImpl
        }
    }

    /**
     * Get the updated [AuthenticationCoreConfig] based on the discovery response
     *
     * @return Updated [AuthenticationCoreConfig]
     */
    override fun getUpdatedAuthenticationCoreConfig(): AuthenticationCoreConfig {
        return runBlocking {
            authenticationCoreConfigManager
                .getUpdatedAuthenticationCoreConfig(
                    authenticationCoreConfig.getDiscoveryEndpoint(),
                    authenticationCoreConfig
                )
        }
    }
}
