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

package io.asgardeo.android.core.util

import io.asgardeo.android.core.models.autheniticator.Authenticator

/**
 * Util class to handle the authenticator
 */
object AuthenticatorUtil {
    /**
     * Check whether there are duplicates authenticators of the given authenticator in the given step
     *
     * @param authenticators List of authenticators
     * @param authenticatorIdString Authenticator id string
     *
     * @return Boolean value whether there are duplicates authenticators of the given authenticator in the given step
     */
    private fun hasDuplicatesAuthenticatorsInGivenStepOnAuthenticatorId(
        authenticators: ArrayList<Authenticator>,
        authenticatorIdString: String
    ): Boolean {
        return authenticators.count { it.authenticatorId == authenticatorIdString } > 1
    }

    /**
     * Get the authenticator from the authenticators list
     *
     * @param authenticators List of authenticators
     * @param authenticatorIdString Authenticator id string
     *
     * @return [Authenticator] object, `null` if the authenticator is not found
     * or if there are duplicates authenticators of the given authenticator in the given step
     */
    private fun getAuthenticatorFromAuthenticatorsListOnAuthenticatorId(
        authenticators: ArrayList<Authenticator>,
        authenticatorIdString: String,
        authenticatorTypeString: String
    ): Authenticator? {
        val authenticator: Authenticator? =
            authenticators.find { it.authenticatorId == authenticatorIdString }

        val hasDuplicates: Boolean = hasDuplicatesAuthenticatorsInGivenStepOnAuthenticatorId(
            authenticators,
            authenticatorIdString
        )

        return if (hasDuplicates) null else {
            if (authenticator?.authenticator == authenticatorTypeString) authenticator
            else null
        }
    }

    /**
     * Get the authenticator  from the authenticator  list.
     * Done by checking the authenticator id or authenticator .
     *
     * Precedence: authenticatorId > authenticatorType
     *
     * @param authenticators List of authenticators
     * @param authenticatorIdString The authenticator id string
     * @param authenticatorTypeString The authenticator type string
     */
    internal fun getAuthenticatorFromAuthenticatorsList(
        authenticators: ArrayList<Authenticator>,
        authenticatorIdString: String,
        authenticatorTypeString: String
    ): Authenticator? = getAuthenticatorFromAuthenticatorsListOnAuthenticatorId(
        authenticators,
        authenticatorIdString,
        authenticatorTypeString
    )
}
