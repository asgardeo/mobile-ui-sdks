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

package io.asgardeo.android.core.models.auth_params

/**
 * Authenticator parameters class - For TOTP Authenticator.
 *
 * Currently this class is not used for authentication with TOTP as a first factor, only for the
 * second factor and beyond. If in future TOTP we should use this class for the first factor
 * as well.
 */
data class TotpAuthenticatorTypeAuthParams(
    /**
     * Code retrieved from the authenticator application
     */
    override val token: String? = null,
    /**
     * Username of the user - if the authenticator used in the first step
     */
    override val username: String? = null
) : AuthParams(
    token = token
) {
    /**
     * Get the parameter body for the authenticator to be sent to the server
     *
     * @return LinkedHashMap<String, String> - Parameter body for the authenticator
     * ```
     * ex: [<"totp", totp>]
     * ```
     */
    override fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String> {
        val paramBody = LinkedHashMap<String, String>()

        if (token == null || username == null) {
            paramBody[requiredParams[0]] = token ?: username!!
        } else {
            paramBody[requiredParams[0]] = username
            paramBody[requiredParams[1]] = token
        }

        return paramBody
    }
}
