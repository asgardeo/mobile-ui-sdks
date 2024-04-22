package io.asgardeo.android.core.models.exceptions

import io.asgardeo.android.core.models.http_client.CustomHttpURLConnection

/**
 * Exception to be thrown to the exception related to the [CustomHttpURLConnection]
 *
 * @property message Message related to the exception
 */
class CustomHttpURLConnectionException(
    override val message: String?
) : Exception(message) {
    companion object {
        /**
         * Authenticator exception TAG
         */
        const val ONLY_HTTPS_CONNCTIONS = "Only HTTPS connections are supported"

        /**
         * Message to be shown when authenticator is not initialized
         */
        const val FAILED_TO_INITIALIZE_SSL_CONTEXT = "Failed to initialize SSL context"

        /**
         * Message to be shown when authentication is not completed
         */
        const val FAILED_TO_OPEN_CONNECTION = "Failed to open connection"
    }
}
