package io.asgardeo.android.core.provider.provider_managers.user

import android.content.Context

/**
 * UserProviderManager is responsible for managing the actions related to the user details.
 */
internal interface UserProviderManager {
    /**
     * Get the basic user information of the authenticated user from the server using the
     * userinfo endpoint.
     *
     * @param context The [Context] of the application
     *
     * @return User details as a [LinkedHashMap]
     */
     suspend fun getBasicUserInfo(context: Context): LinkedHashMap<String, Any>?
}
