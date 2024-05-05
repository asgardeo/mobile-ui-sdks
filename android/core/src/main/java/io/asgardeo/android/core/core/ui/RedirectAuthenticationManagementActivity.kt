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

package io.asgardeo.android.core.core.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.asgardeo.android.core.core.di.RedirectAuthenticationManagementActivityContainer
import io.asgardeo.android.core.core.managers.native_authentication_handler.redirect.RedirectAuthenticationHandlerManager

/**
 * Stores state and handles events related to the redirect authentication flow
 * by OpenID Connect authenticators.
 *
 * Started by the Initiating Activity, the user has selected an OpenID Connect provider like
 * GitHub redirect authentication. After that, from the [RedirectAuthenticationHandlerManager], the
 * [RedirectAuthenticationManagementActivity] is started to handle the redirection URI.
 *
 * From this activity, the [ChromeCustomTabBrowser] is used to open the redirect URI in a
 * Chrome Custom Tab. Then if the user successfully completes the authentication process, the
 * [RedirectUriReceiverActivity] is started and redirected to the
 * [RedirectAuthenticationManagementActivity] with the response URI to handle the authentication
 * success, which then calls the [RedirectAuthenticationHandlerManager] to handle the
 * authentication success. If the user cancels the authentication, it returns to this activity and
 * calls the [RedirectAuthenticationHandlerManager] to handle the authentication cancel.
 *
 * The following diagram illustrates the operation of the activity:
 *
 * ```
 *                          Back Stack Towards Top
 *                +------------------------------------------>
 *
 * +------------+              +---------------+        +----------------+      +--------------+
 * |            |     (1)      |               | (2)    |                | (S1) |              |
 * |            |+-----------> |    Redirect   |+------>|                |      |              |
 * | Initiating |              | Authorization |        | Chrome Custom  |      | Redirect URI |
 * |  Activity  |              |  Management   |        |      Tab       |+---->|   Receiver   |
 * |            |<-----------+ |   Activity    |<------+|                |      |   Activity   |
 * |            |   (SA)       |               | (C1)   |                |      |              |
 * +------------+              +-+---+---------+        +----------------+      +-------+------+
 *                                 ^                                              |
 *                                 |                                              |
 *                                 |                      (S2)                    |
 *                                 +----------------------------------------------+
 *
 * - Step 1: Start the [RedirectAuthenticationManagementActivity] from the Initiating Activity.
 *
 * - Step 2: This activity opens the Chrome custom tab to open the redirect URI. At this point,
 *   depending on user action, we will either end up in a "completion" flow (S),
 *   "cancellation" flow (C), or "successful authentication" flow (SA).
 *
 * - Cancellation (C) flow:
 *     - Step C1: If the user presses the back button or otherwise causes the authorization
 *       activity to finish, the AuthorizationManagementActivity will be recreated or restarted.
 *
 * - Completion (S) flow:
 *     - Step S1: The Chrome Custom Tab forwards the redirect URI to the
 *        [RedirectUriReceiverActivity].
 *
 *     - Step S2: {@link RedirectUriReceiverActivity} extracts the forwarded data and invokes
 *       [RedirectAuthenticationManagementActivity] using an intent derived from
 *       {@link #createResponseHandlingIntent}. This intent has flag ACTIVITY_SINGLE_TOP set,
 *       which will result in both Chrome Custom Tab and [RedirectUriReceiverActivity] being
 *       destroyed, such that [RedirectAuthenticationManagementActivity] is once again at the
 *       top of the back stack.
 *
 * - Successful Authentication (SA) flow:
 *    - Step SA: If the user successfully completes the authentication process or an error occurs
 *      like the user cancels the authentication process, the [RedirectUriReceiverActivity]
 *      redirects to the [RedirectAuthenticationHandlerManager] to handle the authentication success or
 *      authentication failure and then finish the activity.
 */
class RedirectAuthenticationManagementActivity : AppCompatActivity() {

    // The redirect authentication manager
    private val redirectAuthenticationManager: RedirectAuthenticationHandlerManager? by lazy {
        RedirectAuthenticationManagementActivityContainer.getRedirectAuthenticationHandlerManager()
    }

    // Flag to check if the redirection is handled
    private var redirectSuccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the redirection URI
        openRedirectUrl(intent.extras ?: savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        if (redirectSuccess) {
            // Handle the redirect authentication success
            if (intent.data != null) {
                val responseUri: Uri = intent.data as Uri
                handleRedirectAuthenticationSuccess(responseUri)
            } else {
                handleRedirectAuthenticationCancel()
            }

            finish()
        } else {
            redirectSuccess = true
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        // Set the new intent with the response URI
        setIntent(intent)
    }

    /**
     * Open the redirect URL in a Chrome Custom Tab
     *
     * @param state the state bundle that carries the redirect URI [Bundle]
     */
    private fun openRedirectUrl(state: Bundle?) {
        val redirectUri: String = state?.getString(KEY_REDIRECT_URI)!!

        ChromeCustomTabBrowser.openRedirectUrl(this, redirectUri)
    }

    /**
     * Handle the redirect authentication success
     *
     * @param responseUri the response URI, which carries the parameters describing the response.
     */
    private fun handleRedirectAuthenticationSuccess(responseUri: Uri) {
        redirectAuthenticationManager?.handleRedirectUri(this, responseUri)
    }

    /**
     * Handle the redirect authentication cancel
     */
    private fun handleRedirectAuthenticationCancel() {
        redirectAuthenticationManager?.handleRedirectAuthenticationCancel()
    }

    companion object {
        private const val KEY_REDIRECT_URI = "redirectUri"

        /**
         * Creates an intent to handle the completion of an authorization flow. This restores
         * the original AuthorizationManagementActivity that was created at the start of the flow.
         *
         * @param context the package context for the app.
         * @param responseUri the response URI, which carries the parameters describing the response.
         *
         * @return an intent that can be used to resume the authorization flow.
         */
        internal fun createResponseHandlingIntent(context: Context?, responseUri: Uri?): Intent {
            val intent = Intent(context, RedirectAuthenticationManagementActivity::class.java)
            intent.setData(responseUri)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            return intent
        }

        /**
         * Creates an intent to start an authorization flow.
         *
         * @param context the package context for the app.
         * @param redirectUri the redirect URI to be opened in the Chrome Custom Tab.
         *
         * @return an intent that can be used to start the authorization flow.
         */
        internal fun createStartIntent(context: Context?, redirectUri: String): Intent {
            val intent = Intent(context, RedirectAuthenticationManagementActivity::class.java)
            intent.putExtra(KEY_REDIRECT_URI, redirectUri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }
}
