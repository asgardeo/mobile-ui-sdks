package io.asgardeo.android.core.core.di

import io.asgardeo.android.core.core.managers.token.impl.TokenManagerImpl
import io.asgardeo.android.core.data.token.TokenDataStoreFactory

/**
 * Dependency container for the [TokenManagerImpl] class.
 */
internal object TokenManagerImplContainer {
    /**
     * Returns an instance of the [TokenDataStoreFactory] class.
     *
     * @return [TokenDataStoreFactory] instance.
     */
    internal fun getTokenDataStoreFactory(): TokenDataStoreFactory = TokenDataStoreFactory
}
