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

package io.asgardeo.android.core.models.authentication_flow

import io.asgardeo.android.core.util.JsonUtil

/**
 * Authentication flow data class. Which is used to hold the data of an authentication flow.
 *
 * @property flowStatus Status of the authentication flow
 */
abstract class AuthenticationFlow(open val flowStatus: String) {
    /**
     * Convert the object to a json string
     */
    fun toJsonString(): String {
        return JsonUtil.getJsonString(this)
    }
}

