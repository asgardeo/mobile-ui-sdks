/*
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.asgardeo.android.core.util

import android.util.Base64
import java.nio.charset.Charset

/**
 * Util class to handle the base64 encoding and decoding
 */
object Base64Util {
    /**
     * Decode the base64 encoded string
     *
     * @param input Base64 encoded string
     *
     * @return Decoded string
     */
    internal fun base64UrlDecode(input: String): String {
        val base64Encoded = input.replace('-', '+').replace('_', '/')
        val paddedLength = (4 - base64Encoded.length % 4) % 4
        val paddedString = base64Encoded + "=".repeat(paddedLength)

        val decodedBytes = Base64.decode(paddedString, Base64.URL_SAFE)
        return String(decodedBytes, Charset.defaultCharset())
    }

    /**
     * Encode the given object to base64
     *
     * @param dataObject Object to be encoded
     *
     * @return Base64 encoded string
     */
    internal fun base64UrlEncode(dataObject: Any): String {
        val dataObjectString: String = JsonUtil.getJsonString(dataObject)
        return Base64.encodeToString(
            dataObjectString.toByteArray(Charsets.UTF_8),
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }
}
