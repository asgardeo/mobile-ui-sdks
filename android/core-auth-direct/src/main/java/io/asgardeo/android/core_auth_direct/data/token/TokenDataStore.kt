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

package io.asgardeo.android.core_auth_direct.data.token

import io.asgardeo.android.core_auth_direct.models.state.TokenState

/**
 * Interface which has the methods to manage the token data store.
 */
internal interface TokenDataStore {
    /**
     * Save the [TokenState] to the data store.
     *
     * @param tokenState The [TokenState] instance.
     */
    suspend fun saveTokenState(tokenState: TokenState)

    /**
     * Get the [TokenState] from the data store.
     *
     * @return The [TokenState] instance.
     */
    suspend fun getTokenState(): TokenState?

    /**
     * Clear the tokens from the token data store.
     */
    suspend fun clearTokens()
}
