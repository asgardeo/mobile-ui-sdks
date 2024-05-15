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

package io.asgardeo.android.core_auth_direct.core.managers.authn.impl

import io.asgardeo.android.core_auth_direct.models.autheniticator.Authenticator
import io.asgardeo.android.core_auth_direct.util.JsonUtil
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Builder function related to the Authenticator.
 */
internal object AuthnManagerImplRequestBuilder {

    /**
     * Build the request to authorize the application.
     *
     * @param authorizeUri Authorization endpoint
     * @param clientId Client id of the application
     * @param scope Scope of the application (ex: openid profile email)
     * @param integrityToken Client attestation integrity token
     *
     * @return [okhttp3.Request] to authorize the application
     */
    internal fun authorizeRequestBuilder(
        authorizeUri: String,
        clientId: String,
        redirectUri: String,
        scope: String,
        integrityToken: String? = null
    ): Request {
        val formBody: RequestBody = FormBody.Builder()
            .add("client_id", clientId)
            .add("redirect_uri", redirectUri)
            .add("scope", scope)
            .add("response_type", "code")
            .add("response_mode", "direct")
            .build()

        val requestBuilder: Request.Builder = Request.Builder().url(authorizeUri)
        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded")

        // Pass the client attestation token if it is not null
        if (integrityToken != null) {
            requestBuilder.addHeader("x-client-attestation", integrityToken)
        }

        return requestBuilder.post(formBody).build()
    }

    /**
     * Build the request for the authentication.
     * This request will be used to get the next step of the authentication flow.
     *
     * @param authnUri Authentication next step endpoint
     * @param flowId Flow id of the authentication flow
     * @param authenticator Detailed object of the selected authenticator
     * @param authenticatorAuthParams Authenticator parameters of the selected authenticator
     * as a [LinkedHashMap]
     *
     * @return [okhttp3.Request] to get the next step of the authentication flow
     */
    internal fun authenticateRequestBuilder(
        authnUri: String,
        flowId: String,
        authenticator: Authenticator,
        authenticatorAuthParams: LinkedHashMap<String, String>,
    ): Request {
        val authBody = LinkedHashMap<String, Any>()
        authBody["flowId"] = flowId

        val selectedAuthenticator = LinkedHashMap<String, Any>()
        selectedAuthenticator["authenticatorId"] = authenticator.authenticatorId
        selectedAuthenticator["params"] = authenticatorAuthParams

        authBody["selectedAuthenticator"] = selectedAuthenticator

        val formBody: RequestBody = JsonUtil.getJsonObject(authBody).toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val requestBuilder: Request.Builder = Request.Builder().url(authnUri)
        return requestBuilder.post(formBody).build()
    }
}
