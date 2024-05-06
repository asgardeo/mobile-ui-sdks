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
