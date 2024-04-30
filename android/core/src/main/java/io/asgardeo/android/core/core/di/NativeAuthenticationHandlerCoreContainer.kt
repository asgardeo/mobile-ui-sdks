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

package io.asgardeo.android.core.core.di

import io.asgardeo.android.core.core_config.AuthenticationCoreConfig
import io.asgardeo.android.core.core.core_types.native_authentication_handler.impl.NativeAuthenticationHandlerCore
import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native.GoogleNativeAuthenticationHandlerManager
import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native.impl.GoogleNativeAuthenticationHandlerManagerImpl
import io.asgardeo.android.core.core.managers.native_authentication_handler.google_native_legacy.impl.GoogleNativeLegacyAuthenticationHandlerManagerImpl
import io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.PasskeyAuthenticationHandlerManager
import io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.impl.PasskeyAuthenticationHandlerManagerImpl
import io.asgardeo.android.core.core.managers.native_authentication_handler.redirect.RedirectAuthenticationHandlerManager
import io.asgardeo.android.core.core.managers.native_authentication_handler.redirect.impl.RedirectAuthenticationHandlerManagerImpl

/**
 * Dependency Injection container for the [NativeAuthenticationHandlerCore] class
 */
internal object NativeAuthenticationHandlerCoreContainer {
    /**
     * Get the [GoogleNativeAuthenticationHandlerManager] instance
     *
     * @param authenticationCoreConfig [AuthenticationCoreConfig] to get the Google Web Client ID
     *
     * @return [GoogleNativeAuthenticationHandlerManager] instance
     */
    internal fun getGoogleNativeAuthenticationHandlerManager(
        authenticationCoreConfig: AuthenticationCoreConfig,
    ): GoogleNativeAuthenticationHandlerManager =
        GoogleNativeAuthenticationHandlerManagerImpl.getInstance(
            authenticationCoreConfig,
            GoogleNativeAuthenticationHandlerManagerImplContainer.getGoogleNativeAuthenticationHandlerManagerImplRequestBuilder()
        )

    /**
     * Get the [GoogleNativeLegacyAuthenticationHandlerManagerImpl] instance
     *
     * @param authenticationCoreConfig [AuthenticationCoreConfig] to get the Google Web Client ID
     *
     * @return [GoogleNativeLegacyAuthenticationHandlerManagerImpl] instance
     */
    internal fun getGoogleNativeLegacyAuthenticationHandlerManager(
        authenticationCoreConfig: AuthenticationCoreConfig,
    ): GoogleNativeLegacyAuthenticationHandlerManagerImpl =
        GoogleNativeLegacyAuthenticationHandlerManagerImpl.getInstance(authenticationCoreConfig)

    /**
     * Get the [RedirectAuthenticationHandlerManager] instance
     *
     * @return [RedirectAuthenticationHandlerManager] instance
     */
    fun getRedirectAuthenticationHandlerManager(): RedirectAuthenticationHandlerManager =
        RedirectAuthenticationHandlerManagerImpl.getInstance()

    /**
     * Get the [PasskeyAuthenticationHandlerManager] instance
     *
     * @return [PasskeyAuthenticationHandlerManager] instance
     */
    fun getPasskeyAuthenticationHandlerManager(): PasskeyAuthenticationHandlerManager =
        PasskeyAuthenticationHandlerManagerImpl.getInstance(
            PasskeyAuthenticationHandlerManagerImplContainer
                .getPasskeyAuthenticationHandlerManagerImplRequestBuilder()
        )
}
