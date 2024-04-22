package io.asgardeo.android.core.core_config.managers.discovery_manager

import com.fasterxml.jackson.databind.JsonNode

/**
 * Manager to handle the discovery endpoint.
 */
interface DiscoveryManager {
    /**
     * Call the discovery endpoint and return the response.
     *
     * @param discoveryEndpoint Discovery endpoint
     *
     * @return Discovery response as a [JsonNode]
     */
    suspend fun callDiscoveryEndpoint(discoveryEndpoint: String): JsonNode
}
