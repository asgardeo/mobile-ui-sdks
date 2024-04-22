package io.asgardeo.android.core.core.managers.user

/**
 * UserManager is responsible for managing the user details.
 */
interface UserManager {
    /**
     * Get the basic user information of the authenticated.
     *
     * @param accessToken Access token to authorize the request
     *
     * @return User details as a [LinkedHashMap]
     */
    suspend fun getBasicUserInfo(accessToken: String?): LinkedHashMap<String, Any>?
}
