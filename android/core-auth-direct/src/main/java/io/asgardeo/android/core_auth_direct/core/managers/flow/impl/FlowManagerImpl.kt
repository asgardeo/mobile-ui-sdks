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

package io.asgardeo.android.core_auth_direct.core.managers.flow.impl

import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core_auth_direct.core.managers.flow.FlowManager
import io.asgardeo.android.core_auth_direct.models.authentication_flow.AuthenticationFlow
import io.asgardeo.android.core_auth_direct.models.authentication_flow.AuthenticationFlowNotSuccess
import io.asgardeo.android.core_auth_direct.models.authentication_flow.AuthenticationFlowSuccess
import io.asgardeo.android.core_auth_direct.models.exceptions.AuthenticatorException
import io.asgardeo.android.core_auth_direct.models.exceptions.FlowManagerException
import io.asgardeo.android.core_auth_direct.models.flow_status.FlowStatus
import java.lang.ref.WeakReference

/**
 * [FlowManager] implementation class
 * This class is responsible for handling the state of the authorization flow.
 */
internal class FlowManagerImpl private constructor() : FlowManager {
    companion object {
        /**
         * Instance of the [FlowManagerImpl] that will be used throughout the application
         */
        private var flowManagerImplInstance: WeakReference<FlowManagerImpl> = WeakReference(null)

        /**
         * Initialize the [FlowManagerImpl] instance and return the instance.
         */
        fun getInstance(): FlowManagerImpl {
            var flowManagerImpl = flowManagerImplInstance.get()
            if (flowManagerImpl == null) {
                flowManagerImpl = FlowManagerImpl()
                flowManagerImplInstance = WeakReference(flowManagerImpl)
            }
            return flowManagerImpl
        }
    }

    /**
     * Flow id of the authorization flow
     */
    private lateinit var flowId: String

    /**
     * Set the flow id of the authorization flow
     *
     * @param flowId Flow id of the authorization flow
     *
     */
    override fun setFlowId(flowId: String) {
        this.flowId = flowId
    }

    /**
     * Get the flow id of the authorization flow
     *
     * @return Flow id of the authorization flow
     */
    override fun getFlowId(): String {
        return flowId
    }

    /**
     * Handle the authorization flow and return the authenticators in the next step.
     *
     * @param responseBodyString Response body string of the authorization request
     *
     * @return [AuthenticationFlow] with the authenticators in the next step
     *
     * @throws [AuthenticatorException]
     */
    private fun handleAuthorizeFlow(
        responseBodyString: String
    ): AuthenticationFlowNotSuccess = AuthenticationFlowNotSuccess.fromJson(responseBodyString)

    /**
     * Manage the state of the authorization flow.
     * This function will return the [AuthenticationFlowNotSuccess] if the flow is incomplete.
     * This function will return the [AuthenticationFlowSuccess] if the flow is completed.
     *
     * @param responseObject Response object of the authorization request
     *
     * @return [AuthenticationFlow] with the authenticators in the next step
     * @throws [AuthenticatorException] If the flow is failed
     * @throws [FlowManagerException] If the flow is failed incomplete
     *
     * TODO: Need to check additional check to flowid to check if the flow is the same as the current flow
     */
    override fun manageStateOfAuthorizeFlow(responseObject: JsonNode): AuthenticationFlow {
        return when (responseObject.get("flowStatus").asText()) {
            FlowStatus.FAIL_INCOMPLETE.flowStatus -> {
                throw FlowManagerException(
                    FlowManagerException.AUTHENTICATION_NOT_COMPLETED
                )
            }

            FlowStatus.INCOMPLETE.flowStatus -> {
                handleAuthorizeFlow(responseObject.toString())
            }

            FlowStatus.SUCCESS.flowStatus -> {
                AuthenticationFlowSuccess.fromJson(responseObject.toString())
            }

            else -> {
                throw FlowManagerException(
                    FlowManagerException.AUTHENTICATION_NOT_COMPLETED_UNKNOWN
                )
            }
        }
    }

    /**
     * Remove the instance of the [FlowManagerImpl]
     */
    override fun dispose() {
        flowManagerImplInstance.clear()
    }
}
