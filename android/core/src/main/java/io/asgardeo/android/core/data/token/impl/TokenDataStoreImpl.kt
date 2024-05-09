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

package io.asgardeo.android.core.data.token.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.asgardeo.android.core.data.token.TokenDataStore
import io.asgardeo.android.core.models.state.TokenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

/**
 * The name of the data store.
 */
private const val DATA_STORE_NAME = "token_data_store"

/**
 * Initialize the data store.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_NAME
)

/**
 * [TokenDataStoreImpl] is the implementation of the [TokenDataStore]
 *
 * @param context The [Context] instance.
 */
internal class TokenDataStoreImpl(private val context: Context) : TokenDataStore {
    companion object {
        private val TOKEN_STATE_KEY = stringPreferencesKey("TOKEN_STATE")
    }

    /**
     * Save the [TokenState] to the data store.
     *
     * @param tokenState The [TokenState] instance.
     */
    override suspend fun saveTokenState(tokenState: TokenState): Unit =
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[TOKEN_STATE_KEY] = tokenState.toJsonString()
            }
        }

    /**
     * Get the [TokenState] from the data store.
     *
     * @return The [TokenState] instance.
     */
    override suspend fun getTokenState(): TokenState? = withContext(Dispatchers.IO) {
        val preferences: Preferences? = context.dataStore.data.firstOrNull()

        return@withContext preferences?.get(TOKEN_STATE_KEY)?.let {
            TokenState.fromJsonString(it)
        }
    }

    /**
     * Clear the tokens from the token data store.
     */
    override suspend fun clearTokens(): Unit = withContext(Dispatchers.IO) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
