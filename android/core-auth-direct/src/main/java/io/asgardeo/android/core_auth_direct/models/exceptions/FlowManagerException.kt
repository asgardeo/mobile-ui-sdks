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

package io.asgardeo.android.core_auth_direct.models.exceptions

import io.asgardeo.android.core_auth_direct.core.managers.flow.FlowManager

/**
 * Exception to be thrown to the exception related to [FlowManager]
 */
class FlowManagerException(
    override val message: String?,
    val messages: ArrayList<Any> = arrayListOf()
) : Exception(message) {
    companion object {
        /**
         * Flow manager exception TAG
         */
        const val FLOW_MANAGER_EXCEPTION = "FlowManager Exception"

        /**
         * Message to be shown when authentication is not completed
         */
        const val AUTHENTICATION_NOT_COMPLETED =
            "Authentication is not completed. Response returned FAIL_INCOMPLETE"

        /**
         * Message to be shown when authentication is not completed due to an unknown error
         */
        const val AUTHENTICATION_NOT_COMPLETED_UNKNOWN =
            "Authentication is not completed. Unknown error occurred"
    }

    override fun toString(): String {
        return "$FLOW_MANAGER_EXCEPTION: $message"
    }
}
