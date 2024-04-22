package io.asgardeo.android.core.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Util class to handle json related operations
 */
internal object JsonUtil {
    /**
     * [ObjectMapper] instance to handle json related operations
     */
    private val mapper: ObjectMapper = jacksonObjectMapper()

    /**
     * Convert an object of type `T` to a [String]
     *
     * @param dataObject Object to be converted
     *
     * @return [String] converted from the object
     */
    internal fun getJsonString(dataObject: Any): String {
        return mapper.writeValueAsString(dataObject)
    }

    /**
     * Convert a [Map<String, Any>] to a [JsonNode]
     *
     * @param jsonMap Map to be converted
     *
     * @return [JsonNode] converted from the map
     */
    internal fun getJsonObject(jsonMap: Map<String, Any>): JsonNode {
        return mapper.valueToTree(jsonMap)
    }

    /**
     * Convert a [String] to a [JsonNode]
     *
     * @param jsonString String to be converted
     *
     * @return [JsonNode] converted from the string
     */
    internal fun getJsonObject(jsonString: String): JsonNode {
        return mapper.readTree(jsonString)
    }

    /**
     * Convert a [JsonNode] to an object of type `T`
     *
     * @param jsonNode JsonNode to be converted
     * @param typeReference Type of the object to be converted to
     *
     * @return Object of type `T` converted from the JsonNode
     */
    internal fun <T> jsonNodeToObject(jsonNode: JsonNode, typeReference: TypeReference<T>): T {
        return mapper.readValue(jsonNode.toString(), typeReference)
    }
}
