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

import io.asgardeo.android.core_auth_direct.util.JsonUtil

/**
 * Authenticator parameters class
 */
abstract class AuthParams(
    /**
     * Username of the user - For Basic Authenticator, TOTP Authenticator, Email OTP Authenticator, SMS OTP Authenticator
     */
    open val username: String? = null,
    /**
     * Password of the user - For Basic Authenticator
     */
    open val password: String? = null,
    /**
     * access token retrieved from the Google authenticator - For Google Native Authenticator
     */
    open val accessToken: String? = null,
    /**
     * id token retrieved from the Google authenticator - For Google Native Authenticator
     */
    open val idToken: String? = null,
    /**
     * Code retrieved from the authenticator application - For TOTP Authenticator
     */
    open val token: String? = null,
    /**
     * Token response retrieved from the passkey authenticator - For Passkey Authenticator
     */
    open val tokenResponse: String? = null,
    /**
     * OTP code retrieved from the authenticator application - For SMS OTP Authenticator, Email OTP Authenticator
     */
    open val otpCode: String? = null
) {
    fun toJsonString(): String {
        return JsonUtil.getJsonString(this)
    }

    /**
     * Get the parameter body for the authenticator to be sent to the server
     */
    abstract fun getParameterBodyAuthenticator(requiredParams: List<String>)
            : LinkedHashMap<String, String>
}
