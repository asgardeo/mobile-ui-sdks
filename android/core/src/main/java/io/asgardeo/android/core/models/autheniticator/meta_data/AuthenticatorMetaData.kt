/*
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

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
