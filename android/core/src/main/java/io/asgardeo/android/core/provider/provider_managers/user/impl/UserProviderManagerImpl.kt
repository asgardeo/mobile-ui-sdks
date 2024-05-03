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

package io.asgardeo.android.core.provider.provider_managers.user.impl

import android.content.Context
import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.provider.provider_managers.token.TokenProviderManager
import io.asgardeo.android.core.provider.provider_managers.user.UserProviderManager
import java.lang.ref.WeakReference

/**
 * UserProviderManagerImpl is responsible for managing the actions related to the user details.
 *
 * @property authenticationCore [AuthenticationCoreDef] instance to get the user details
 * @property tokenProviderManager [TokenProviderManager] instance to get the access token
 */
class UserProviderManagerImpl private constructor(
    private val authenticationCore: AuthenticationCoreDef,
    private val tokenProviderManager: TokenProviderManager
) : UserProviderManager {

    companion object {
        /**
         * Instance of the [UserProviderManagerImpl] that will be used throughout the
         * application
         */
        private var userProviderManagerInstance:
                WeakReference<UserProviderManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [UserProviderManagerImpl] instance and return the instance.
         *
         * @param authenticationCore The [AuthenticationCoreDef] instance
         * @param tokenProviderManager The [TokenProviderManager] instance
         *
         * @return The [UserProviderManagerImpl] instance
         */
        fun getInstance(
            authenticationCore: AuthenticationCoreDef,
            tokenProviderManager: TokenProviderManager
        ): UserProviderManagerImpl {
            var userProviderManager = userProviderManagerInstance.get()
            if (userProviderManager == null) {
                userProviderManager = UserProviderManagerImpl(
                    authenticationCore,
                    tokenProviderManager
                )
                userProviderManagerInstance = WeakReference(userProviderManager)
            }
            return userProviderManager
        }
    }

    /**
     * Get the basic user information of the authenticated user from the server using the
     * userinfo endpoint.
     *
     * @param context The [Context] of the application
     *
     * @return The user details [LinkedHashMap] that contains the user details
     */
    override suspend fun getBasicUserInfo(context: Context): LinkedHashMap<String, Any>? {
        var userDetails: LinkedHashMap<String, Any>? = null

        tokenProviderManager.performAction(context) { accessToken, _ ->
            userDetails = authenticationCore.getBasicUserInfo(accessToken)
        }

        return userDetails
    }
}
