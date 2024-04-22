package io.asgardeo.android.core.models.state

import net.openid.appauth.AuthState

/**
 * Token state model class. This class is used to hold the [AuthState] instance.
 *
 * @property appAuthState The [AuthState] instance.
 */
class TokenState(private var appAuthState: AuthState) {
    companion object {
        /**
         * Create a [TokenState] instance from a [String].
         *
         * @param jsonString The [String] to be converted to a [TokenState] instance.
         *
         * @return [TokenState] instance converted from the [String]
         */
        fun fromJsonString(jsonString: String): TokenState {
            return TokenState(AuthState.jsonDeserialize(jsonString))
        }
    }

    /**
     * Get the [AuthState] instance.
     *
     * @return The [AuthState] instance.
     */
    fun getAppAuthState(): AuthState = appAuthState

    /**
     * Update the [AuthState] instance.
     *
     * @param authState The new [AuthState] instance.
     */
    fun updateAppAuthState(authState: AuthState) {
        this.appAuthState = authState
    }

    /**
     * Convert the [TokenState] to a [String].
     *
     * @return [String] converted from the [TokenState]
     */
    fun toJsonString(): String = appAuthState.jsonSerializeString()
}
