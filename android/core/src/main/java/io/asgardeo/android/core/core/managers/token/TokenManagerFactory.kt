package io.asgardeo.android.core.core.managers.token

import android.content.Context
import io.asgardeo.android.core.core.managers.token.impl.TokenManagerImpl

/**
 * Factory class to get the instance of the [TokenManager].
 *
 * @property instances The [MutableMap] instance.
 */
internal object TokenManagerFactory {
    private val instances = mutableMapOf<Context, TokenManager>()

    /**
     * Get the instance of the [TokenManager] based on the [Context], if
     * the instance is already created for the given [Context] then it will
     * return the same instance.
     *
     * @param context The [Context] instance.
     *
     * @return The [TokenManager] instance.
     */
    fun getTokenManager(context: Context): TokenManager {
        return instances.getOrPut(context) {
            TokenManagerImpl(context)
        }
    }
}