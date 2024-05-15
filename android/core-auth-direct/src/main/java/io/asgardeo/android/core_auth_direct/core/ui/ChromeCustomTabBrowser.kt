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

package io.asgardeo.android.core_auth_direct.core.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession

/**
 * Class to handle the Chrome Custom Tab browser to open the redirect URL in redirection based authentication
 *
 * TODO: As a future improvement, we can consider giving the user the ability to change the theme of the Chrome Custom Tab
 */
internal object ChromeCustomTabBrowser {
    /**
     * The package name of the Chrome browser
     */
    private const val CHROME_PACKAGE_NAME = "com.android.chrome"

    private var customTabsServiceConnection: CustomTabsServiceConnection? = null
    private var customTabClient: CustomTabsClient? = null
    private var customTabsSession: CustomTabsSession? = null

    /**
     * Set the CustomTabsServiceConnection to bind the CustomTabsService
     *
     * @param context the context of the application [Context]
     */
    private fun setCustomTabsServiceConnection(context: Context) {
        customTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                client: CustomTabsClient
            ) {
                customTabClient = client
                customTabClient?.warmup(0)
                customTabsSession = customTabClient?.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                customTabClient = null
                customTabsSession = null
            }
        }

        // Bind the CustomTabsService
        CustomTabsClient.bindCustomTabsService(
            context,
            CHROME_PACKAGE_NAME,
            customTabsServiceConnection as CustomTabsServiceConnection
        )
    }

    /**
     * Open the redirect URL in a Chrome Custom Tab
     *
     * @param context the context of the application [Context]
     * @param redirectUri the redirect URI to open [String]
     */
    internal fun openRedirectUrl(context: Context, redirectUri: String) {
        setCustomTabsServiceConnection(context)

        val uri = Uri.parse(redirectUri)

        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .build()
        customTabsIntent.intent.setPackage(CHROME_PACKAGE_NAME)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        customTabsIntent.launchUrl(context, uri)
    }
}
