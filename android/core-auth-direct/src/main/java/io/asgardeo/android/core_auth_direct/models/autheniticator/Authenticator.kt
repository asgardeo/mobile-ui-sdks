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

package io.asgardeo.android.core_auth_direct.models.autheniticator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.asgardeo.android.core_auth_direct.models.autheniticator.meta_data.AuthenticatorMetaData
import io.asgardeo.android.core_auth_direct.util.JsonUtil

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
