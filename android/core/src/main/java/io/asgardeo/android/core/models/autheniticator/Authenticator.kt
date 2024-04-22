package io.asgardeo.android.core.models.autheniticator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.asgardeo.android.core.models.autheniticator.meta_data.AuthenticatorMetaData
import io.asgardeo.android.core.util.JsonUtil

/**
 * Class to represent an Authenticator
 *
 * @param authenticatorId Id of the authenticator
 * @param authenticator Type of the authenticator
 * @param idp IDP of the authenticator
 * @param metadata Metadata of the authenticator
 * @param requiredParams Required params that should be sent to the server for authentication in this authenticator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class Authenticator(
    /**
     * Id of the authenticator
     */
    open val authenticatorId: String,
    /**
     * Name of the authenticator
     */
    open val authenticator: String?,
    /**
     * Id of the idp of the authenticator
     */
    open val idp: String?,
    /**
     * Metadata of the authenticator
     */
    open val metadata: AuthenticatorMetaData?,
    /**
     * Required params that should be sent to the server for authentication in this authenticator
     */
    open val requiredParams: List<String>?
) {
    fun toJsonString(): String {
        return JsonUtil.getJsonString(this)
    }
}
