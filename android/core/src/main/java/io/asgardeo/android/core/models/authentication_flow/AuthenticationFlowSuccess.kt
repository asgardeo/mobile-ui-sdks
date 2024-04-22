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
