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

package io.asgardeo.android.core.models.http_client

import android.net.Uri
import io.asgardeo.android.core.models.exceptions.CustomHttpURLConnectionException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Use to create the [CustomHttpURLConnection] for the token call from `appauth-android`
 *
 * @property uri The URI of the endpoint.
 * @property trustManager The [X509TrustManager] instance.
 * @property sslSocketFactory The [SSLSocketFactory] instance.
 *
 * Initializing the [CustomHttpURLConnection] class can throw the following exceptions:
 *
 * @throws IllegalArgumentException If the protocol of the URI is not HTTPS.
 * @throws RuntimeException If the SSL context cannot be initialized.
 * @throws RuntimeException If the connection cannot be opened.
 */
internal class CustomHttpURLConnection(
    private val uri: Uri,
    private val trustManager: X509TrustManager,
    private val sslSocketFactory: SSLSocketFactory
) {
    private val connection: HttpURLConnection

    init {
        val url = URL(uri.toString())
        if (url.protocol != "https") {
            throw IllegalArgumentException(CustomHttpURLConnectionException.ONLY_HTTPS_CONNCTIONS)
        }

        try {
            val httpsURLConnection = url.openConnection() as HttpsURLConnection
            httpsURLConnection.sslSocketFactory = sslSocketFactory
            httpsURLConnection.hostnameVerifier = HostnameVerifier { _, _ -> true }

            val trustManagers = arrayOf<TrustManager>(trustManager)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagers, null)

            httpsURLConnection.sslSocketFactory = sslContext.socketFactory

            this.connection = httpsURLConnection
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(CustomHttpURLConnectionException.FAILED_TO_INITIALIZE_SSL_CONTEXT, e)
        } catch (e: KeyManagementException) {
            throw RuntimeException(CustomHttpURLConnectionException.FAILED_TO_INITIALIZE_SSL_CONTEXT, e)
        } catch (e: IOException) {
            throw RuntimeException(CustomHttpURLConnectionException.FAILED_TO_OPEN_CONNECTION, e)
        }
    }

    /**
     * Returns the [HttpURLConnection] instance.
     *
     * @return [HttpURLConnection] instance.
     */
    fun getConnection(): HttpURLConnection {
        return this.connection
    }
}
