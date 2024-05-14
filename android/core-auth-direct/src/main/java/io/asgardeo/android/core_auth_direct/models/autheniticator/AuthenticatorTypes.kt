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

package io.asgardeo.android.core_auth_direct.models.autheniticator

/**
 * Enum class for authenticators
 *
 * @param authenticatorType Authenticator type value
 */
enum class AuthenticatorTypes(val authenticatorType: String) {
    /**
     * Basic authenticator type
     */
    BASIC_AUTHENTICATOR("Username & Password"),
    /**
     * Google native authenticator type
     */
    GOOGLE_AUTHENTICATOR("Google"),
    /**
     * TOTP authenticator type
     */
    TOTP_AUTHENTICATOR("TOTP"),
    /**
     * Passkey authenticator type
     */
    PASSKEY_AUTHENTICATOR("Passkey"),
    /**
     * OpenID Connect authenticator type
     */
    OPENID_CONNECT_AUTHENTICATOR("openidconnect"),
    /**
     * Github authenticator type
     */
    GITHUB_REDIRECT_AUTHENTICATOR("Github"),
    /**
     * Microsoft authenticator type
     */
    MICROSOFT_REDIRECT_AUTHENTICATOR("openidconnect"),
    /**
     * Email OTP authenticator type
     */
    EMAIL_OTP_AUTHENTICATOR("Email OTP"),
    /**
     * SMS OTP authenticator type
     */
    SMS_OTP_AUTHENTICATOR("SMS OTP")
}
