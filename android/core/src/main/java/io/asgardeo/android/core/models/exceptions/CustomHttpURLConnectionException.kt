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

import io.asgardeo.android.core.models.http_client.CustomHttpURLConnection

/**
 * Exception to be thrown to the exception related to the [CustomHttpURLConnection]
 *
 * @param message Message related to the exception
 */
class CustomHttpURLConnectionException(
    override val message: String?
) : Exception(message) {
    companion object {
        /**
         * Authenticator exception TAG
         */
        const val ONLY_HTTPS_CONNCTIONS = "Only HTTPS connections are supported"

        /**
         * Message to be shown when authenticator is not initialized
         */
        const val FAILED_TO_INITIALIZE_SSL_CONTEXT = "Failed to initialize SSL context"

        /**
         * Message to be shown when authentication is not completed
         */
        const val FAILED_TO_OPEN_CONNECTION = "Failed to open connection"
    }
}
