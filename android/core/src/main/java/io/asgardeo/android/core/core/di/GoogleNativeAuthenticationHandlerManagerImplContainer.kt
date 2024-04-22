package io.asgardeo.android.core.core.di

import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native.impl.GoogleNativeAuthenticationHandlerManagerImplRequestBuilder
import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native.impl.GoogleNativeAuthenticationHandlerManagerImpl

/**
 * Container class to di the [GoogleNativeAuthenticationHandlerManagerImpl]
 */
object GoogleNativeAuthenticationHandlerManagerImplContainer {
    /**
     * Get the [GoogleNativeAuthenticationHandlerManagerImplRequestBuilder] instance
     *
     * @return [GoogleNativeAuthenticationHandlerManagerImplRequestBuilder] instance
     */
    internal fun getGoogleNativeAuthenticationHandlerManagerImplRequestBuilder()
            : GoogleNativeAuthenticationHandlerManagerImplRequestBuilder =
        GoogleNativeAuthenticationHandlerManagerImplRequestBuilder
}
