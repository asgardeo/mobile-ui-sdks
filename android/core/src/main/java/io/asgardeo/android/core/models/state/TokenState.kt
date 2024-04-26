/*
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.asgardeo.android.core.models.state

import net.openid.appauth.AuthState

/**
 * Token state model class. This class is used to hold the [AuthState] instance.
 *
 * @property appAuthState The [AuthState] instance.
 */
class TokenState(private var appAuthState: AuthState) {
    companion object {
        /**
         * Create a [TokenState] instance from a [String].
         *
         * @param jsonString The [String] to be converted to a [TokenState] instance.
         *
         * @return [TokenState] instance converted from the [String]
         */
        fun fromJsonString(jsonString: String): TokenState {
            return TokenState(AuthState.jsonDeserialize(jsonString))
        }
    }

    /**
     * Get the [AuthState] instance.
     *
     * @return The [AuthState] instance.
     */
    fun getAppAuthState(): AuthState = appAuthState

    /**
     * Update the [AuthState] instance.
     *
     * @param authState The new [AuthState] instance.
     */
    fun updateAppAuthState(authState: AuthState) {
        this.appAuthState = authState
    }

    /**
     * Convert the [TokenState] to a [String].
     *
     * @return [String] converted from the [TokenState]
     */
    fun toJsonString(): String = appAuthState.jsonSerializeString()
}
