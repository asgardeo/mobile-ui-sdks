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
import io.asgardeo.android.core.models.autheniticator.Authenticator
import io.asgardeo.android.core.util.JsonUtil

/**
 * Authentication flow data class. Which is used to hold the data of an authentication flow.
 *
 * @property flowId Id of the authentication flow
 * @property flowStatus Status of the authentication flow
 * @property flowType Type of the authentication flow
 * @property nextStep Next step of the authentication flow
 * @property links Links of the authentication flow
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticationFlowNotSuccess(
    override val flowStatus: String,
    val flowId: String,
    val flowType: String,
    var nextStep: AuthorizeFlowNotSuccessNextStep,
    val links: Any
) : AuthenticationFlow(flowStatus) {
    /**
     * Authorize flow next step data class.
     *
     * @property stepType Type of the next step
     * @property authenticators List of authenticators of the next step
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AuthorizeFlowNotSuccessNextStep(
        val stepType: String,
        var authenticators: ArrayList<Authenticator>,
        val messages: ArrayList<Any>?
    ) {
        /**
         * Convert the object to a json string
         */
        override fun toString(): String {
            return JsonUtil.getJsonString(this)
        }
    }

    companion object {
        /**
         * Convert a json string to a [AuthenticationFlowNotSuccess] object.
         *
         * @param jsonString Json string to be converted
         *
         * @return [AuthenticationFlowNotSuccess] converted from the json string
         */
        fun fromJson(jsonString: String): AuthenticationFlowNotSuccess {
            val stepTypeReference = object : TypeReference<AuthenticationFlowNotSuccess>() {}
            val jsonNodeAuthorizeFlow: JsonNode = JsonUtil.getJsonObject(jsonString)

            return JsonUtil.jsonNodeToObject(jsonNodeAuthorizeFlow, stepTypeReference)
        }
    }
}
