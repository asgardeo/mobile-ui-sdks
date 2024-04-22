package io.asgardeo.android.core.core.managers.token

import io.asgardeo.android.core.models.state.TokenState

/**
 * Interface which has the methods to manage the tokens.
 */
internal interface TokenManager {

    /**
     * Save the [TokenState] to the data store.
     *
     * @param tokenState The [TokenState] instance.
     */
    suspend fun saveTokenState(tokenState: TokenState): Unit?

    /**
     * Get the [TokenState] from the data store.
     *
     * @return The [TokenState] instance.
     */
    suspend fun getTokenState(): TokenState?

    /**
     * Get the access token from the token data store.
     *
     * @return The access token [String]
     */
    suspend fun getAccessToken(): String?

    /**
     * Get the refresh token from the token data store.
     *
     * @return The refresh token [String]
     */
    suspend fun getRefreshToken(): String?

    /**
     * Get the ID token from the token data store.
     *
     * @return The ID token [String]
     */
    suspend fun getIDToken(): String?

    /**
     * Get the decoded ID token
     *
     * @param idToken The ID token
     *
     * @return The decoded ID token [String]
     */
    fun getDecodedIDToken(idToken: String): LinkedHashMap<String, Any>

    /**
     * Get the access token expiration time from the token data store.
     *
     * @return The access token expiration time [Long]
     */
    suspend fun getAccessTokenExpirationTime(): Long?

    /**
     * Get the scope from the token data store.
     *
     * @return The scope [String]
     */
    suspend fun getScope(): String?

    /**
     * Clear the tokens from the token data store.
     */
    suspend fun clearTokens(): Unit?

    /**
     * Validate the access token, by checking the expiration time of the access token, and
     * by checking if the access token is null or empty.
     *
     * **Here we are not calling the introspection endpoint to validate the access token!.
     * We are checking the expiration time of the access token and
     * if the access token is null or empty.**
     *
     * @return `true` if the access token is valid, `false` otherwise.
     */
    suspend fun validateAccessToken(): Boolean?
}
