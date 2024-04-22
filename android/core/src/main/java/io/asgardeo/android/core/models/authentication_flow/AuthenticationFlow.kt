package io.asgardeo.android.core.models.authentication_flow

import io.asgardeo.android.core.util.JsonUtil

/**
 * Authentication flow data class. Which is used to hold the data of an authentication flow.
 *
 * @property flowStatus Status of the authentication flow
 */
abstract class AuthenticationFlow(open val flowStatus: String) {
    /**
     * Convert the object to a json string
     */
    fun toJsonString(): String {
        return JsonUtil.getJsonString(this)
    }
}

