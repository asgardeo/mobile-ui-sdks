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

package io.asgardeo.android.core_auth_direct.core.managers.authenticator

import io.asgardeo.android.core_auth_direct.models.autheniticator.Authenticator
import io.asgardeo.android.core_auth_direct.models.exceptions.AuthenticatorException

/**
 * Authenticator manager interface.
 * This interface is responsible for handling the authenticator related operations.
 */
internal interface AuthenticatorManager {
    /**
     * Get full details of the authenticator.
     *
     * @param flowId Flow id of the authentication flow
     * @param authenticator Authenticator object of the selected authenticator
     *
     * @return Authenticator type with full details [Authenticator]
     *
     * @throws AuthenticatorException
     */
    suspend fun getDetailsOfAuthenticator(
        flowId: String,
        authenticator: Authenticator
    ): Authenticator

    /**
     * Remove the instance of the [AuthenticatorManager]
     */
    fun dispose()
}
