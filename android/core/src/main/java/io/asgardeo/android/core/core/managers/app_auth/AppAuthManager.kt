package io.asgardeo.android.core.core.managers.app_auth

import android.content.Context
import io.asgardeo.android.core.models.exceptions.AppAuthManagerException
import io.asgardeo.android.core.models.state.TokenState

/**
 * Interface which has the methods to manage the AppAuth SDK.
 */
internal interface AppAuthManager {
    /**
     * Use to exchange the authorization code for the access token.
     *
     * @param authorizationCode The authorization code.
     *
     * @throws AppAuthManagerException If the token request fails.
     *
     * @return The [TokenState] instance.
     */
    suspend fun exchangeAuthorizationCode(
        authorizationCode: String,
        context: Context
    ): TokenState?

    /**
     * Use to perform the refresh token grant.
     *
     * @param context The [Context] instance.
     * @param tokenState The [TokenState] instance.
     *
     * @throws AppAuthManagerException If the token request fails.
     *
     * @return Updated [TokenState] instance.
     */
    suspend fun performRefreshTokenGrant(
        context: Context,
        tokenState: TokenState,
    ): TokenState?

    /**
     * Perform an action with the tokens. If the token is expired, it will perform the refresh the
     * tokens, and then perform the action. This will also update the token in the data store
     * as well.
     *
     * @param context The [Context] instance.
     * @param tokenState The [TokenState] instance.
     * @param action The action to perform.
     *
     * @return Updated [TokenState] instance.
     */
    suspend fun performAction(
        context: Context,
        tokenState: TokenState,
        action: suspend (String?, String?) -> Unit
    ): TokenState?
}
