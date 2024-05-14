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

package io.asgardeo.android.core_auth_direct.core.managers.flow

import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core_auth_direct.models.authentication_flow.AuthenticationFlow
import io.asgardeo.android.core_auth_direct.models.authentication_flow.AuthenticationFlowNotSuccess
import io.asgardeo.android.core_auth_direct.models.authentication_flow.AuthenticationFlowSuccess
import io.asgardeo.android.core_auth_direct.models.exceptions.AuthenticatorException
import io.asgardeo.android.core_auth_direct.models.exceptions.FlowManagerException

/**
 * Flow manager interface
 * This interface is responsible for handling the state of the authorization flow.
 */
internal interface FlowManager {
    /**
     * Set the flow id of the authorization flow
     *
     * @param flowId Flow id of the authorization flow
     */
    fun setFlowId(flowId: String)

    /**
     * Get the flow id of the authorization flow
     *
     * @return Flow id of the authorization flow
     */
    fun getFlowId(): String

    /**
     * Manage the state of the authorization flow.
     * This function will return the [AuthenticationFlowNotSuccess] if the flow is incomplete.
     * This function will return the [AuthenticationFlowSuccess] if the flow is completed.
     * This function will throw an [AuthenticatorException] if the flow is failed.
     *
     * @param responseObject Response object of the authorization request
     *
     * @return [AuthenticationFlow] with the authenticators in the next step
     * @throws [AuthenticatorException] If the flow is failed
     * @throws [FlowManagerException] If the flow is failed incomplete
     */
    fun manageStateOfAuthorizeFlow(responseObject: JsonNode): AuthenticationFlow

    /**
     * Remove the instance of the [FlowManager]
     */
    fun dispose()
}
