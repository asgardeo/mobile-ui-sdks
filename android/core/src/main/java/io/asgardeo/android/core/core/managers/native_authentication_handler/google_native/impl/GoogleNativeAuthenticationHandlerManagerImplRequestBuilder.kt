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

package io.asgardeo.android.core.core.managers.native_authentication_handler.google_native.impl

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

/**
 * Request builder class for the [GoogleNativeAuthenticationHandlerManagerImpl]
 * This class is responsible for building the requests for the Google Native Authentication
 * using the Credential Manager API
 */
object GoogleNativeAuthenticationHandlerManagerImplRequestBuilder {
    /**
     * Build the request to authenticate the user with Google using the Credential Manager API
     *
     * @param googleIdOptions [GetGoogleIdOption] to get the Google ID Token
     *
     * @return [GetCredentialRequest] to authenticate the user with Google
     */
    internal fun getAuthenticateWithGoogleNativeRequestBuilder(
        googleIdOptions: GetGoogleIdOption
    ): GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOptions)
        .build()

    /**
     * Build the request to logout the user from the google account
     *
     * @return [ClearCredentialStateRequest] to logout the user from the google account
     */
    internal fun getGoogleLogoutRequestBuilder(): ClearCredentialStateRequest =
        ClearCredentialStateRequest()
}
