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

package io.asgardeo.android.core.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * Activity to receive the deep link redirection URI
 */
class RedirectUriReceiverActivity : ComponentActivity() {
    // Flag to check if the redirection is handled
    private var redirectionHandled: Boolean = false

    companion object {
        private const val KEY_REDIRECTION_HANDLED = "redirectionHandled"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the redirection URI
        if (savedInstanceState == null || !savedInstanceState.getBoolean(KEY_REDIRECTION_HANDLED)) {
            intent?.data?.let { deepLink ->
                redirectionHandled = true // Mark redirection as handled

                // this will redirect to the RedirectAuthenticationManagementActivity
                // to continue the authentication process
                startActivity(
                    RedirectAuthenticationManagementActivity.createResponseHandlingIntent(
                        this,
                        deepLink
                    )
                )
            }
        }

        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_REDIRECTION_HANDLED, redirectionHandled)
    }
}
