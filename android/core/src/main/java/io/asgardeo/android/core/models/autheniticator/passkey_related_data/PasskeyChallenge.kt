package io.asgardeo.android.core.models.autheniticator.passkey_related_data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.asgardeo.android.core.util.JsonUtil

/**
 * Data class to hold the passkey challenge data
 *
 * @property challenge The challenge string
 * @property allowCredentials The list of allowed credentials
 * @property timeout The timeout value
 * @property userVerification The user verification method
 * @property rpId The rpId value
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PasskeyChallenge(
    val challenge: String,
    val allowCredentials: List<String>,
    val timeout: Long,
    val userVerification: String,
    val rpId: String
) {
    /**
     * Get the JSON string of the passkey challenge
     */
    override fun toString(): String {
        return JsonUtil.getJsonString(this)
    }
}
