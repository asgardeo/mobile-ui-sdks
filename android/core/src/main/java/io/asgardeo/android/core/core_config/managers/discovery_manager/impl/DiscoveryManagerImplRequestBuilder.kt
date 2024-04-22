package io.asgardeo.android.core.core_config.managers.discovery_manager.impl

import okhttp3.Request

/**
 * Builder function related to the [DiscoveryManagerImpl]
 */
object DiscoveryManagerImplRequestBuilder {

    /**
     * Build the request to call the discovery endpoint.
     *
     * @param discoveryUri Discovery endpoint
     *
     * @return [okhttp3.Request] to call the discovery endpoint
     */
    internal fun discoveryRequestBuilder(discoveryUri: String): Request {
        val requestBuilder: Request.Builder = Request.Builder().url(discoveryUri)
        requestBuilder.addHeader("Accept", "application/json")

        return requestBuilder.get().build()
    }
}
