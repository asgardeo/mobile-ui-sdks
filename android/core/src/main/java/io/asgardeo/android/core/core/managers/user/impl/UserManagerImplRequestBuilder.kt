package io.asgardeo.android.core.core.managers.user.impl

import okhttp3.Request

/**
 * UserManagerImplRequestBuilder is responsible for building the requests for the [UserManagerImpl]
 * to get the user details.
 */
object UserManagerImplRequestBuilder {
    /**
     * Build the request to get the user details.
     *
     * @param meUri User details endpoint
     * @param accessToken Access token to authorize the request
     *
     * @return [okhttp3.Request] to get the user details
     */
    internal fun getUserDetailsRequestBuilder(meUri: String, accessToken: String): Request {
        val requestBuilder: Request.Builder = Request.Builder().url(meUri)
        requestBuilder.addHeader("Accept", "application/scim+json")
        requestBuilder.addHeader("Authorization", "Bearer $accessToken")

        return requestBuilder.get().build()
    }
}
