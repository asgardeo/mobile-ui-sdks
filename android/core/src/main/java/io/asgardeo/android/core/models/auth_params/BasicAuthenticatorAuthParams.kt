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

package io.asgardeo.android.core.models.auth_params

/**
 * Authenticator parameters class - For Basic Authenticator
 */
data class BasicAuthenticatorAuthParams(
    /**
     * Username of the user
     */
    override val username: String,
    /**
     * Password of the user
     */
    override val password: String
) : AuthParams(username = username, password = password) {
    /**
     * Get the parameter body for the authenticator to be sent to the server
     *
     * @return LinkedHashMap<String, String> - Parameter body for the authenticator
     * ```
     * ex: [<"username", username>, <"password", password>]
     * ```
     */
    override fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String> {
        val paramBody = LinkedHashMap<String, String>()
        paramBody[requiredParams[0]] = username
        paramBody[requiredParams[1]] = password

        return paramBody
    }
}
