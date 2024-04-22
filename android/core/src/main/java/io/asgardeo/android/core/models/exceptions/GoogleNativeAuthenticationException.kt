package io.asgardeo.android.core.models.exceptions

/**
 * Exception to be thrown to the exception related to the Google Native Authentication
 */
class GoogleNativeAuthenticationException (
    override val message: String?
): Exception(message) {
    companion object {
        /**
         * Google Native Authentication exception TAG
         */
        const val GOOGLE_NATIVE_AUTHENTICATION_EXCEPTION = "Google Native Authentication Exception"

        /**
         * Message to be shown when Google Web Client ID is not set
         */
        const val GOOGLE_WEB_CLIENT_ID_NOT_SET = "Google Web Client ID is not set"

        /**
         * Message for the case where the Google auth code or id token is not found
         */
        const val GOOGLE_AUTH_CODE_OR_ID_TOKEN_NOT_FOUND = "Google auth code or id token not found"

        /**
         * Message for the case where the Google authentication failed
         */
        const val GOOGLE_AUTHENTICATION_FAILED = "Google authentication failed"

        /**
         * Message for the case where the Google ID Token is not found
         */
        const val GOOGLE_ID_TOKEN_NOT_FOUND = "Google ID Token not found"
    }

    override fun toString(): String {
        return "$GOOGLE_NATIVE_AUTHENTICATION_EXCEPTION: $message"
    }
}
