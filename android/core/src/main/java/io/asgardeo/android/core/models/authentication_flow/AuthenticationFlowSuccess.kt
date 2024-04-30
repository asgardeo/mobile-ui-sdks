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

package io.asgardeo.android.core.models.authentication_flow

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core.util.JsonUtil

/**
 * Authentication flow success data class. Which is used to hold the data of an successful authentication flow.
 *
 * @property flowStatus Status of the authentication flow
 * @property authData Authentication data of the authentication flow
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticationFlowSuccess(
    override val flowStatus: String,
    val authData: AuthData,
): AuthenticationFlow(flowStatus) {
    /**
     * Authentication data data class which is used to hold the authentication data of a successful authentication flow.
     *
     * @property code Code of the authentication flow
     * @property session_state Session state of the authentication flow
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AuthData(
        val code: String,
        val session_state: String
    )

    companion object {
        /**
         * Convert a json string to a [AuthenticationFlowSuccess] object.
         *
         * @param jsonString Json string to be converted
         *
         * @return [AuthenticationFlowSuccess] converted from the json string
         */
        fun fromJson(jsonString: String): AuthenticationFlowSuccess {
            val stepTypeReference = object : TypeReference<AuthenticationFlowSuccess>() {}
            val jsonNodeAuthorizeFlow: JsonNode = JsonUtil.getJsonObject(jsonString)

            return JsonUtil.jsonNodeToObject(jsonNodeAuthorizeFlow, stepTypeReference)
        }
    }
}
