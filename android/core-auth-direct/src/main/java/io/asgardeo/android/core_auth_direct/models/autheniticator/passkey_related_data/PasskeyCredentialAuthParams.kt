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

package io.asgardeo.android.core_auth_direct.models.autheniticator.passkey_related_data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import io.asgardeo.android.core_auth_direct.util.Base64Util
import io.asgardeo.android.core_auth_direct.util.JsonUtil

/**
 * Data class to hold the passkey credential authentication parameters
 *
 * @param requestId The request id
 * @param credential The passkey credential
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PasskeyCredentialAuthParams(
    val requestId: String,
    val credential: PasskeyCredential
) {

    /**
     * Passkey credential data class
     *
     * @param rawId The raw id
     * @param authenticatorAttachment The authenticator attachment
     * @param id The id
     * @param response The response
     * @param clientExtensionResults The client extension results
     * @param type The type
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PasskeyCredential(
        val rawId: String?,
        val authenticatorAttachment: String?,
        val id: String,
        val response: Any,
        val clientExtensionResults: Any,
        val type: String
    ) {
        companion object {
            /**
             * Get the passkey credential from the response string
             *
             * @param responseString The response string
             *
             * @return The passkey credential [PasskeyCredential]
             */
            fun fromJsonString(responseString: String): PasskeyCredential {
                val responseJsonNode = JsonUtil.getJsonObject(responseString)
                val stepTypeReference = object : TypeReference<PasskeyCredential>() {}

                return JsonUtil.jsonNodeToObject(responseJsonNode, stepTypeReference)
            }
        }
    }

    /**
     * Get the passkey credential authentication parameters from the string
     *
     * @return Base64 encoded string of the passkey credential authentication parameters
     */
    override fun toString(): String {
        return Base64Util.base64UrlEncode(this)
    }
}
