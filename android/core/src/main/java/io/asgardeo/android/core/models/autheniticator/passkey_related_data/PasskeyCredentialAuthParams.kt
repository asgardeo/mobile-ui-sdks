package io.asgardeo.android.core.models.autheniticator.passkey_related_data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import io.asgardeo.android.core.util.Base64Util
import io.asgardeo.android.core.util.JsonUtil

/**
 * Data class to hold the passkey credential authentication parameters
 *
 * @property requestId The request id
 * @property credential The passkey credential
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PasskeyCredentialAuthParams(
    val requestId: String,
    val credential: PasskeyCredential
) {

    /**
     * Passkey credential data class
     *
     * @property rawId The raw id
     * @property authenticatorAttachment The authenticator attachment
     * @property id The id
     * @property response The response
     * @property clientExtensionResults The client extension results
     * @property type The type
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
