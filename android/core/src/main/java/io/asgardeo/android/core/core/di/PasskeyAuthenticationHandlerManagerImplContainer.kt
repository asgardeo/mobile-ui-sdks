package io.asgardeo.android.core.core.di

import io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.impl.PasskeyAuthenticationHandlerManagerImplRequestBuilder
import io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.impl.PasskeyAuthenticationHandlerManagerImpl

/**
 * Container class to di the [PasskeyAuthenticationHandlerManagerImpl]
 * This class is responsible for providing the [PasskeyAuthenticationHandlerManagerImplRequestBuilder] instance
 */
object PasskeyAuthenticationHandlerManagerImplContainer {
    /**
     * Get the [PasskeyAuthenticationHandlerManagerImplRequestBuilder] instance
     *
     * @return [PasskeyAuthenticationHandlerManagerImplRequestBuilder] instance
     */
    internal fun getPasskeyAuthenticationHandlerManagerImplRequestBuilder()
            : PasskeyAuthenticationHandlerManagerImplRequestBuilder =
        PasskeyAuthenticationHandlerManagerImplRequestBuilder
}
