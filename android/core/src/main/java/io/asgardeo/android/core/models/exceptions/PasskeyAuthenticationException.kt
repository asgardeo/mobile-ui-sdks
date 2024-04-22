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

package io.asgardeo.android.core.models.exceptions

/**
 * Exception to be thrown to the exception related to the Passkey Authentication
 */
class PasskeyAuthenticationException (
    override val message: String?
): Exception(message) {
    companion object {
        /**
         * Passkey Authentication Exception TAG
         */
        private const val PASSKEY_AUTHENTICATION_EXCEPTION = "PasskeyAuthenticationException"

        /**
         * Passkey Authentication is not supported message
         */
        const val PASSKEY_AUTHENTICATION_NOT_SUPPORTED = "Passkey Authentication is not supported"

        /**
         * Passkey Authentication failed message
         */
        const val PASSKEY_AUTHENTICATION_FAILED = "Passkey Authentication failed"

        /**
         * Passkey Challenge String is empty message
         */
        const val PASSKEY_CHALLENGE_STRING_EMPTY = "Passkey Challenge String is empty"
    }

    override fun toString(): String {
        return "$PASSKEY_AUTHENTICATION_EXCEPTION: $message"
    }
}
