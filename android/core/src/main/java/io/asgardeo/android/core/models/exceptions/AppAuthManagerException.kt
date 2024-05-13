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

package io.asgardeo.android.core.models.exceptions

import io.asgardeo.android.core.core.managers.app_auth.AppAuthManager

/**
 * Exception to be thrown to the exception related to the [AppAuthManager]
 *
 * @param message Message related to the exception
 * @param exceptionMessage Message related to the exception
 *
 * TODO: Make a mapper function to map the exceptions to the error types, and these need to be SEALED classes
 */
class AppAuthManagerException(
    override val message: String?,
    private val exceptionMessage: String? = null
): Exception(message) {
    companion object {
        /**
         * Authenticator exception TAG
         */
        const val APP_AUTH_MANAGER_EXCEPTION = "App Auth Manager Exception"

        /**
         * Message to be shown when token response is empty
         */
        const val EMPTY_TOKEN_RESPONSE = "Token response is empty"

        /**
         * Message to be shown when token response is empty or invalid
         */
        const val INVALID_REFRESH_TOKEN = "Invalid refresh token or refresh token is expired"

        /**
         * Message to be shown when Auth state is invalid
         */
        const val INVALID_AUTH_STATE = "Invalid authentication state"
    }

    override fun toString(): String {
        return "$APP_AUTH_MANAGER_EXCEPTION: $message $exceptionMessage"
    }

    /**
     * Print the exception
     */
    fun printException() {
        println(toString())
    }
}
