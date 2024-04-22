package io.asgardeo.android.core.models.autheniticator.meta_data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.asgardeo.android.core.util.JsonUtil

/**
 * Meta data related to the authenticator
 *
 * @param i18nKey I18n key related to the authenticator
 * @param promptType Prompt type
 * @param params Params related to the authenticator
 * @param additionalData Additional data related to the authenticator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class AuthenticatorMetaData(
    /**
     * I18n key related to the authenticator
     */
    open val i18nKey: String? = null,
    /**
     * Prompt type
     */
    open val promptType: String? = null,
    /**
     * Params related to the authenticator
     */
    open val params: ArrayList<AuthenticatorParam>? = null,
    /**
     * Additional data related to the authenticator
     */
    open val additionalData: AuthenticatorAdditionalData? = null
) {
    /**
     * Parameters related to the authenticator
     *
     * @param param Parameter related to the authenticator
     * @param type Type of the parameter
     * @param order Order of the parameter
     * @param i18nKey I18n key related to the parameter
     * @param displayName Display name of the parameter
     * @param confidential Is the parameter confidential
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    open class AuthenticatorParam(
        open val param: String? = null,
        open val type: String? = null,
        open val order: Int? = null,
        open val i18nKey: String? = null,
        open val displayName: String? = null,
        open val confidential: Boolean? = null
    )

    /**
     * Additional data related to the authenticator
     *
     * @param nonce Nonce
     * @param clientId Client id
     * @param scope Scope
     * @param challengeData Challenge data
     * @param state State
     * @param redirectUrl Redirect URL
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    open class AuthenticatorAdditionalData(
        open val nonce: String? = null,
        open val clientId: String? = null,
        open val scope: String? = null,
        open val challengeData: String? = null,
        open val state: String? = null,
        open val redirectUrl: String? = null
    )

    /**
     * Convert the [AuthenticatorMetaData] object to a JSON string
     */
    fun toJsonString(): String {
        return JsonUtil.getJsonString(this)
    }
}
