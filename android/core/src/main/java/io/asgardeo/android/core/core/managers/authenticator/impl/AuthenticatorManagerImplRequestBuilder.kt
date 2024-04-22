package io.asgardeo.android.core.core.managers.authenticator.impl

import io.asgardeo.android.core.util.JsonUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Builder function related to the Authenticator.
 */
internal object AuthenticatorManagerImplRequestBuilder {

    /**
     * Build the request to get details of the authenticator.
     *
     * @param authnUri Authentication next step endpoint
     * @param flowId Flow id of the authentication flow
     * @param authenticatorId Authenticator type id of the authenticator
     *
     * @return [okhttp3.Request] to get details of the authenticator
     */
    internal fun getAuthenticatorRequestBuilder(
        authnUri: String,
        flowId: String,
        authenticatorId: String
    ): Request {
        val authBody = LinkedHashMap<String, Any>()
        authBody["flowId"] = flowId

        val selectedAuthenticator = LinkedHashMap<String, String>()
        selectedAuthenticator["authenticatorId"] = authenticatorId

        authBody["selectedAuthenticator"] = selectedAuthenticator

        val formBody: RequestBody =  JsonUtil.getJsonObject(authBody).toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val requestBuilder: Request.Builder = Request.Builder().url(authnUri)
        return requestBuilder.post(formBody).build()
    }
}
