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

package io.asgardeo.android.core_auth_direct.models.exceptions

/**
 * Exception to be thrown to the exception related to the Authenticator
 *
 * @param message Message related to the exception
 */
class AuthenticationCoreException(
    override val message: String?
): Exception(message) {
    companion object {
        /**
         * Authenticator exception TAG
         */
        const val AUTHORIZATION_SERVICE_EXCEPTION = "Authorization Service Exception"
    }

    override fun toString(): String {
        return "$AUTHORIZATION_SERVICE_EXCEPTION: $message"
    }

    /**
     * Print the exception
     */
    fun printException() {
        println(toString())
    }
}
