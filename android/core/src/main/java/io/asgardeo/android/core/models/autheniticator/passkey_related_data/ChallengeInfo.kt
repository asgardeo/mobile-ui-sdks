package io.asgardeo.android.core.models.autheniticator.passkey_related_data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core.util.Base64Util
import io.asgardeo.android.core.util.JsonUtil

/**
 * Data class to hold the challenge info of the passkey authenticator
 *
 * @property requestId The request id
 * @property publicKeyCredentialRequestOptions The public key credential request options
 * @property request The request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ChallengeInfo (
    val requestId: String,
    val publicKeyCredentialRequestOptions: PublicKeyCredentialRequestOptions,
    val request: Any?
) {
    companion object {
        /**
         * Get the challenge info from the challenge string
         *
         * @param challengeString The challenge string
         *
         * @return The challenge info [ChallengeInfo]
         */
        fun getChallengeInfoFromChallengeString(challengeString: String): ChallengeInfo {

            val challengeJsonString: String = Base64Util.base64UrlDecode(challengeString)
            val challengeJson: JsonNode = JsonUtil.getJsonObject(challengeJsonString)

            val stepTypeReference = object : TypeReference<ChallengeInfo>() {}
            return JsonUtil.jsonNodeToObject(challengeJson, stepTypeReference)
        }
    }

    /**
     * Get the passkey challenge from the challenge info
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PublicKeyCredentialRequestOptions (
        val challenge: String,
        val rpId: String,
        val extensions: Any?
    )

    /**
     * Get the passkey challenge from the challenge info
     *
     * @param allowCredentials The list of allowed credentials. Default is empty list
     * @param timeout The timeout value. Default is 300000
     * @param userVerification The user verification method. Default is "required"
     *
     * @return The passkey challenge [PasskeyChallenge]
     */
    fun getPasskeyChallenge(
        allowCredentials: List<String>?,
        timeout: Long?,
        userVerification: String?
    ): PasskeyChallenge {
        return PasskeyChallenge(
            challenge = this.publicKeyCredentialRequestOptions.challenge,
            allowCredentials = allowCredentials ?: emptyList(),
            timeout = timeout ?: 300000,
            userVerification = userVerification ?: "required",
            rpId = this.publicKeyCredentialRequestOptions.rpId
        )
    }
}