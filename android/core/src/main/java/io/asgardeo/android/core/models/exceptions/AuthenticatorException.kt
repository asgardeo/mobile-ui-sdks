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

import io.asgardeo.android.core.models.autheniticator.Authenticator

/**
 * Exception to be thrown to the exception related to an [Authenticator]
 *
 * @param message Message related to the exception
 * @param authenticator Authenticator type
 * @param code Code of the exception
 */
class AuthenticatorException(
    override val message: String?,
    private val authenticator: String?,
    private val code: String? = null
) : Exception(message) {
    companion object {
        /**
         * Authenticator exception TAG
         */
        const val AUTHENTICATOR_EXCEPTION = "Authenticator Exception"

        /**
         * Authenticator not found or more than one authenticator found
         */
        const val AUTHENTICATOR_NOT_FOUND_OR_MORE_THAN_ONE =
            "Authenticator not found or more than one authenticator found"
    }

    override fun toString(): String {
        val codeString: String = if (code != null) "$code " else ""

        return "$AUTHENTICATOR_EXCEPTION: $authenticator $codeString $message"
    }

    /**
     * Print the exception
     */
    fun printException() {
        println(toString())
    }
}
