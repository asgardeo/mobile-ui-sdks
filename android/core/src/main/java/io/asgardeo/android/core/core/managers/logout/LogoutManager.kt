package io.asgardeo.android.core.core.managers.logout

import io.asgardeo.android.core.models.exceptions.LogoutException
import java.io.IOException

/**
 * Manager to handle the logout of the user from the application
 * This manager is responsible for handling the logout of the user from the application
 */
interface LogoutManager {
    /**
     * Logout the user from the application.
     *
     * @param idToken Id token of the user
     *
     * @throws [LogoutException] If the logout fails
     * @throws [IOException] If the request fails due to a network error
     */
    suspend fun logout(idToken: String): Unit?
}
