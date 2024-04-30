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

package io.asgardeo.android.core.core.managers.native_authentication_handler.passkey.impl

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption

/**
 * Request builder class for the [PasskeyAuthenticationHandlerManagerImpl]
 * This class is responsible for building the requests for the Passkey Authentication
 * using the Credential Manager API
 */
object PasskeyAuthenticationHandlerManagerImplRequestBuilder {
    /**
     * Build the request to authenticate the user with Passkey using the Credential Manager API
     *
     * @param publicKeyCredentialOption [GetPublicKeyCredentialOption]
     *
     * @return [GetCredentialRequest] to authenticate the user with Google
     */
    internal fun getAuthenticateWithPasskeyRequestBuilder(
        publicKeyCredentialOption: GetPublicKeyCredentialOption
    ): GetCredentialRequest = GetCredentialRequest(
        listOf(publicKeyCredentialOption)
    )

    /**
     * Build the request to logout the user using the passkey
     *
     * @return [ClearCredentialStateRequest] to logout the user from the google account
     */
    internal fun getPasskeyLogoutRequestBuilder(): ClearCredentialStateRequest =
        ClearCredentialStateRequest()
}
