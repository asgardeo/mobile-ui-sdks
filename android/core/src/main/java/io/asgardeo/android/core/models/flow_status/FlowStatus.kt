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

package io.asgardeo.android.core.models.flow_status

/**
 * Enum class for flow status
 *
 * @property flowStatus Flow status value
 */
enum class FlowStatus(val flowStatus: String) {
    /**
     * Flow status is fail and incomplete.
     */
    FAIL_INCOMPLETE("FAIL_INCOMPLETE"),

    /**
     * Flow status is incomplete.
     */
    INCOMPLETE("INCOMPLETE"),

    /**
     * Flow status is success.
     */
    SUCCESS("SUCCESS_COMPLETED"),
}
