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

package io.asgardeo.android.core.models.state

import io.asgardeo.android.core.models.authentication_flow.AuthenticationFlow

/**
 * Authentication state of the application. This sealed class is used to represent the different
 * states of the authentication process
 *
 * States:
 * - [Initial]: The initial state of the application
 *
 * - [Unauthenticated]: The user is not authenticated to access the application
 *
 * - [Authenticated]: The user is authenticated to access the application
 *
 * - [Loading]: The application is in the process of loading the authentication state
 *
 * - [Error]: An error occurred during the authentication process
 */
sealed class AuthenticationState {
    data object Initial : AuthenticationState()
    data class Unauthenticated(val authenticationFlow: AuthenticationFlow?) : AuthenticationState()
    data object Authenticated : AuthenticationState()
    data object Loading : AuthenticationState()
    data class Error(val throwable: Throwable) : AuthenticationState()
}
