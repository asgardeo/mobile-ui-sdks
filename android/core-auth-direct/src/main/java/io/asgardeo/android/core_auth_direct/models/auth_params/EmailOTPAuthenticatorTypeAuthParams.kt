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

package io.asgardeo.android.core_auth_direct.models.auth_params

/**
 * Authenticator parameters class - For TOTP Authenticator
 *
 * Currently this class is not used for authentication with Email OTP as a first factor, only for the
 * second factor and beyond. If in future Email OTP we should use this class for the first factor
 */
data class EmailOTPAuthenticatorTypeAuthParams(
    /**
     * Email OTP code received to the user
     */
    override val otpCode: String? = null,
    /**
     * Username of the user - if the authenticator used in the first step
     */
    override val username: String? = null
) : AuthParams(otpCode = otpCode) {
    /**
     * Get the parameter body for the authenticator to be sent to the server
     *
     * @return LinkedHashMap<String, String> - Parameter body for the authenticator
     * ```
     * ex: [<"OTPcode", otpCode>]
     * ```
     */
    override fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String> {
        val paramBody = LinkedHashMap<String, String>()

        if (otpCode == null || username == null) {
            paramBody[requiredParams[0]] = otpCode ?: username!!
        } else {
            paramBody[requiredParams[0]] = username
            paramBody[requiredParams[1]] = otpCode
        }

        return paramBody
    }
}
