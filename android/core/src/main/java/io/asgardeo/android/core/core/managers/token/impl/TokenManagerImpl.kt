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

package io.asgardeo.android.core.core.managers.token.impl

import android.content.Context
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core.core.di.TokenManagerImplContainer
import io.asgardeo.android.core.core.managers.token.TokenManager
import io.asgardeo.android.core.data.token.TokenDataStore
import io.asgardeo.android.core.models.exceptions.TokenManagerException
import io.asgardeo.android.core.models.state.TokenState
import io.asgardeo.android.core.util.Base64Util
import io.asgardeo.android.core.util.JsonUtil

/**
 * Use to manage the tokens.
 *
 * @property context The [Context] instance.
 */
internal class TokenManagerImpl internal constructor(private val context: Context) : TokenManager {
    // Get the token data store
    private val tokenDataStore: TokenDataStore =
        TokenManagerImplContainer.getTokenDataStoreFactory().getTokenDataStore(context)

    /**
     * Save the [TokenState] to the data store.
     *
     * @param tokenState The [TokenState] instance.
     */
    override suspend fun saveTokenState(tokenState: TokenState): Unit =
        tokenDataStore.saveTokenState(tokenState)

    /**
     * Get the [TokenState] from the data store.
     *
     * @return The [TokenState] instance.
     */
    override suspend fun getTokenState(): TokenState? = tokenDataStore.getTokenState()

    /**
     * Get the access token from the token data store.
     *
     * @return The access token [String]
     */
    override suspend fun getAccessToken(): String? =
        getTokenState()?.getAppAuthState()?.accessToken

    /**
     * Get the refresh token from the token data store.
     *
     * @return The refresh token [String]
     */
    override suspend fun getRefreshToken(): String? =
        getTokenState()?.getAppAuthState()?.refreshToken

    /**
     * Get the ID token from the token data store.
     *
     * @return The ID token [String]
     */
    override suspend fun getIDToken(): String? =
        getTokenState()?.getAppAuthState()?.idToken

    /**
     * Get the decoded ID token
     *
     * @param idToken The ID token
     *
     * @return The decoded ID token [String]
     */
    override fun getDecodedIDToken(idToken: String): LinkedHashMap<String, Any> {
        val idTokenParts: List<String> = idToken.split(".")

        if (idTokenParts.size != 3) {
            // Invalid ID token format
            throw TokenManagerException(TokenManagerException.INVALID_ID_TOKEN)
        }

        // reading the json from the payload body string
        val payloadBodyObject: JsonNode =
            JsonUtil.getJsonObject(Base64Util.base64UrlDecode(idTokenParts[1]))

        val stepTypeReference =
            object : TypeReference<LinkedHashMap<String, Any>>() {}

        return JsonUtil.jsonNodeToObject(payloadBodyObject, stepTypeReference)
    }

    /**
     * Get the access token expiration time from the token data store.
     *
     * @return The access token expiration time [Long]
     */
    override suspend fun getAccessTokenExpirationTime(): Long? =
        getTokenState()?.getAppAuthState()?.accessTokenExpirationTime

    /**
     * Get the scope from the token data store.
     *
     * @return The scope [String]
     */
    override suspend fun getScope(): String? =
        getTokenState()?.getAppAuthState()?.scope

    /**
     * Clear the tokens from the token data store.*
     */
    override suspend fun clearTokens(): Unit =
        tokenDataStore.clearTokens()

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
    override suspend fun validateAccessToken(): Boolean {
        return getTokenState()?.getAppAuthState()?.isAuthorized == true
    }
}
