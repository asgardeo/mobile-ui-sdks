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

package io.asgardeo.android.core_auth_direct.provider.provider_managers.token.impl

import android.content.Context
import io.asgardeo.android.core_auth_direct.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core_auth_direct.provider.provider_managers.token.TokenProviderManager
import java.lang.ref.WeakReference

/**
 * The [TokenProviderManagerImpl] class provides the functionality to get the tokens, validate the
 * tokens, refresh the tokens, and clear the tokens. This class uses the functionality provided by
 * the [AuthenticationCoreDef] class.
 */
internal class TokenProviderManagerImpl private constructor(
    private val authenticationCore: AuthenticationCoreDef
) : TokenProviderManager {
    companion object {
        /**
         * Instance of the [TokenProviderManagerImpl] that will be used throughout the application
         */
        private var tokenProviderManagerInstance: WeakReference<TokenProviderManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [TokenProviderManagerImpl] instance and return the instance.
         */
        fun getInstance(authenticationCore: AuthenticationCoreDef): TokenProviderManagerImpl {
            var tokenProviderManager = tokenProviderManagerInstance.get()
            if (tokenProviderManager == null) {
                tokenProviderManager = TokenProviderManagerImpl(authenticationCore)
                tokenProviderManagerInstance = WeakReference(tokenProviderManager)
            }
            return tokenProviderManager
        }
    }

    /**
     * Get the access token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The access token [String]
     */
    override suspend fun getAccessToken(context: Context): String? =
        authenticationCore.getAccessToken(context)

    /**
     * Get the refresh token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The refresh token [String]
     */
    override suspend fun getRefreshToken(context: Context): String? =
        authenticationCore.getRefreshToken(context)

    /**
     * Get the ID token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The ID token [String]
     */
    override suspend fun getIDToken(context: Context): String? =
        authenticationCore.getIDToken(context)

    /**
     * Get the decoded ID token
     *
     * @param context The [Context] instance.
     *
     * @return The decoded ID token [String]
     */
    override suspend fun getDecodedIDToken(context: Context): LinkedHashMap<String, Any> {
        val idToken: String? = authenticationCore.getIDToken(context)

        return authenticationCore.getDecodedIDToken(context, idToken!!)
    }


    /**
     * Get the access token expiration time from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The access token expiration time [Long]
     */
    override suspend fun getAccessTokenExpirationTime(context: Context): Long? =
        authenticationCore.getAccessTokenExpirationTime(context)

    /**
     * Get the scope from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The scope [String]
     */
    override suspend fun getScope(context: Context): String? = authenticationCore.getScope(context)


    /**
     * Validate the access token, by checking the expiration time of the access token, and
     * by checking if the access token is null or empty.
     *
     * **Here we are not calling the introspection endpoint to validate the access token!.
     * We are checking the expiration time of the access token and
     * if the access token is null or empty.**
     *
     * @param context The [Context] instance.
     *
     * @return `true` if the access token is valid, `false` otherwise.
     */
    override suspend fun validateAccessToken(context: Context): Boolean? =
        authenticationCore.validateAccessToken(context)

    /**
     * Perform refresh token grant. This method will perform the refresh token grant and save the
     * updated token state in the data store. If refresh token grant fails, it will throw an
     * Exception.
     *
     * @param context The [Context] instance.
     */
    override suspend fun performRefreshTokenGrant(context: Context) {
        var tokenState = authenticationCore.getTokenState(context)
        tokenState = authenticationCore.performRefreshTokenGrant(context, tokenState!!)
        authenticationCore.saveTokenState(context, tokenState!!)
    }

    /**
     * Perform an action with fresh tokens. This method will perform the action with fresh tokens
     * and save the updated token state in the data store. Developer can directly use this method
     * perform an action with fresh tokens, without worrying about refreshing the tokens. If this
     * action fails, it will throw an Exception.
     *
     * @param context The [Context] instance.
     * @param action The action to perform.
     */
    override suspend fun performAction(
        context: Context,
        action: suspend (String?, String?) -> Unit
    ) {
        var tokenState = authenticationCore.getTokenState(context)
        tokenState = authenticationCore.performAction(context, tokenState!!, action)
        authenticationCore.saveTokenState(context, tokenState!!)
    }

    /**
     * Clear the tokens from the token data store. This method will clear the tokens from the
     * data store. After calling this method, developer needs to perform the authorization flow
     * again to get the tokens.
     *
     * @param context The [Context] instance.
     */
    override suspend fun clearTokens(context: Context): Unit? =
        authenticationCore.clearTokens(context)
}
