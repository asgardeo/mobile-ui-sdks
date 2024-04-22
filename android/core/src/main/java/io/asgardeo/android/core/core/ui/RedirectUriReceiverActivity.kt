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
