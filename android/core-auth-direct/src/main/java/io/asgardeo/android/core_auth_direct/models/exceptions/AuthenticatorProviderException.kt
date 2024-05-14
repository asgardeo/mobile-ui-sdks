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

import io.asgardeo.android.core_auth_direct.models.autheniticator.Authenticator

/**
 * Exception to be thrown to the exception related to [Authenticator]
 *
 * @param message Message related to the exception
 */
class AuthenticatorProviderException(
    override val message: String?
) : Exception(message) {
    companion object {
        /**
         * AuthenticatorProvider Exception
         */
        const val AUTHENTICATOR_PROVIDER_EXCEPTION = "AuthenticatorProvider Exception"

        /**
         * Message for the case where the authenticator is not found
         */
        const val AUTHENTICATOR_NOT_FOUND = "Authenticator not found"
    }

    override fun toString(): String {
        return "$AUTHENTICATOR_PROVIDER_EXCEPTION: $message"
    }
}
