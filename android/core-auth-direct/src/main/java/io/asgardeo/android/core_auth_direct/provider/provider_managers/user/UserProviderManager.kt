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

package io.asgardeo.android.core_auth_direct.provider.provider_managers.user

import android.content.Context

/**
 * UserProviderManager is responsible for managing the actions related to the user details.
 */
internal interface UserProviderManager {
    /**
     * Get the basic user information of the authenticated user from the server using the
     * userinfo endpoint.
     *
     * @param context The [Context] of the application
     *
     * @return User details as a [LinkedHashMap]
     */
     suspend fun getBasicUserInfo(context: Context): LinkedHashMap<String, Any>?
}
