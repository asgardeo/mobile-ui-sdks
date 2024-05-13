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

package io.asgardeo.android.core.core.managers.token

import android.content.Context
import io.asgardeo.android.core.core.managers.token.impl.TokenManagerImpl

/**
 * Factory class to get the instance of the [TokenManager].
 */
internal object TokenManagerFactory {
    private val instances = mutableMapOf<Context, TokenManager>()

    /**
     * Get the instance of the [TokenManager] based on the [Context], if
     * the instance is already created for the given [Context] then it will
     * return the same instance.
     *
     * @param context The [Context] instance.
     *
     * @return The [TokenManager] instance.
     */
    fun getTokenManager(context: Context): TokenManager {
        return instances.getOrPut(context) {
            TokenManagerImpl(context)
        }
    }
}