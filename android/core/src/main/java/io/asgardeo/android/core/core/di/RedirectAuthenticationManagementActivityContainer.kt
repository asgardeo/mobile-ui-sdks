package io.asgardeo.android.core.core.di

import io.asgardeo.android.core.core.managers.native_authentication_handler.redirect.RedirectAuthenticationHandlerManager
import io.asgardeo.android.core.core.managers.native_authentication_handler.redirect.impl.RedirectAuthenticationHandlerManagerImpl
import io.asgardeo.android.core.core.ui.RedirectAuthenticationManagementActivity

/**
 * Dependency Injection container for [RedirectAuthenticationManagementActivity]
 */
object RedirectAuthenticationManagementActivityContainer {
    /**
     * Get the [RedirectAuthenticationHandlerManager] instance
     *
     * @return [RedirectAuthenticationHandlerManager] instance
     */
    fun getRedirectAuthenticationHandlerManager(): RedirectAuthenticationHandlerManager =
        RedirectAuthenticationHandlerManagerImpl.getInstance()
}
