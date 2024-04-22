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

package io.asgardeo.android.core.core_config

import com.fasterxml.jackson.databind.JsonNode
import io.asgardeo.android.core.core.core_types.authentication.AuthenticationCoreDef
import io.asgardeo.android.core.models.http_client.LessSecureHttpClient
import okhttp3.OkHttpClient

/**
 * Holds the configuration related to the [AuthenticationCoreDef].
 *
 * @property discoveryEndpoint Discovery endpoint of the WSO2 identity server
 * @property authorizeEndpoint Authorization endpoint of the WSO2 identity server - optional
 * @property tokenEndpoint Token endpoint of the WSO2 identity server - optional
 * @property userInfoEndpoint User info endpoint of the WSO2 identity server - optional
 * @property logoutEndpoint Logout endpoint of the WSO2 identity server - optional
 * @property authnEndpoint Authentication endpoint of the WSO2 identity server - optional
 * @property redirectUri Redirect uri of the application
 * @property clientId Client id of the application
 * @property scope Scope of the application (ex: openid profile email)
 * @property integrityToken Client attestation integrity token - optional
 * @property googleWebClientId Google web client id - optional
 * This is required when the application needs to authenticate with Google, add the client id of the
 * Google connection that is used to create the connection in the WSO2 identity server.
 * @property isDevelopment The flag to check whether the app is in development mode or not.
 * If true, the [LessSecureHttpClient] instance will be returned. Otherwise, the default
 * [OkHttpClient] instance will be returned. Default value is `false`. It is not recommended to
 * keep this value as `false` in the production environment.
 */
class AuthenticationCoreConfig(
    private val discoveryEndpoint: String? = null,
    private var authorizeEndpoint: String? = null,
    private var tokenEndpoint: String? = null,
    private var userInfoEndpoint: String? = null,
    private var logoutEndpoint: String? = null,
    private var authnEndpoint: String? = null,
    private val redirectUri: String,
    private val clientId: String,
    private val scope: String,
    private val integrityToken: String? = null,
    private val googleWebClientId: String? = null,
    private val isDevelopment: Boolean? = false
) {
    /**
     * Update the core configuration based on the discovery response.
     *
     * @param discoveryResponse Discovery response from the server.
     */
    fun updateBasedOnDiscoveryResponse(discoveryResponse: JsonNode?): AuthenticationCoreConfig {
        authorizeEndpoint =
            discoveryResponse?.get("authorization_endpoint")?.asText() ?: getAuthorizeUrl()
        tokenEndpoint =
            discoveryResponse?.get("token_endpoint")?.asText() ?: getTokenUrl()
        userInfoEndpoint =
            discoveryResponse?.get("userinfo_endpoint")?.asText() ?: getUserinfoEndpoint()
        logoutEndpoint =
            discoveryResponse?.get("end_session_endpoint")?.asText() ?: getLogoutUrl()
        authnEndpoint =
            discoveryResponse?.get("authn_endpoint")?.asText() ?: getAuthnUrl()

        return this
    }

    /**
     * Get base endpoint from the discovery endpoint.
     *
     * @param discoveryEndpoint Discovery endpoint
     *
     * @return Base endpoint
     */
    private fun getBaseEndpointFromDiscoveryEndpoint(discoveryEndpoint: String): String {
        val splitIndex: Int = discoveryEndpoint.indexOf("/oauth2/token")
        return discoveryEndpoint.substring(0, splitIndex)
    }

    /**
     * @sample `https://localhost:9443/oauth2/token/.well-known/openid-configuration`
     *
     * @return Discovery endpoint of the WSO2 identity server.
     */
    fun getDiscoveryEndpoint(): String? = discoveryEndpoint

    /**
     * @sample `https://localhost:9443/oauth2/authorize`
     *
     * @return Authorization url of the WSO2 identity server.
     */
    fun getAuthorizeUrl(): String =
        authorizeEndpoint
            ?: "${getBaseEndpointFromDiscoveryEndpoint(discoveryEndpoint!!)}/oauth2/authorize"

    /**
     * @sample `https://localhost:9443/oauth2/authn`
     *
     * @return Authentication url of the WSO2 identity server.
     */
    fun getAuthnUrl(): String =
        authnEndpoint ?: "${getBaseEndpointFromDiscoveryEndpoint(discoveryEndpoint!!)}/oauth2/authn"

    /**
     * @sample `https://localhost:9443/oauth2/token`
     *
     * @return Token url of the WSO2 identity server.
     */
    fun getTokenUrl(): String =
        tokenEndpoint ?: "${getBaseEndpointFromDiscoveryEndpoint(discoveryEndpoint!!)}/oauth2/token"

    /**
     * @sample `https://localhost:9443/oidc/logout`
     *
     * @return Logout url of the WSO2 identity server.
     */
    fun getLogoutUrl(): String =
        logoutEndpoint ?: "${getBaseEndpointFromDiscoveryEndpoint(discoveryEndpoint!!)}/oidc/logout"

    /**
     * @sample `https://localhost:9443/oauth2/userinfo`
     *
     * @return Me endpoint of the WSO2 identity server.
     */
    fun getUserinfoEndpoint(): String =
        userInfoEndpoint
            ?: "${getBaseEndpointFromDiscoveryEndpoint(discoveryEndpoint!!)}/oauth2/userinfo"

    /**
     * @sample `https://example-app.com/redirect`
     *
     * @return Redirect uri of the application.
     */
    fun getRedirectUri(): String = redirectUri

    /**
     * @return Client id of the application.
     */
    fun getClientId(): String = clientId

    /**
     * @return Scope of the application (ex: openid profile email).
     */
    fun getScope(): String = scope

    /**
     * @return Client attestation integrity token.
     */
    fun getIntegrityToken(): String? = integrityToken

    /**
     * @return Google web client id.
     */
    fun getGoogleWebClientId(): String? = googleWebClientId

    /**
     * @return The flag to check whether the app is in development mode or not [Boolean].
     */
    fun getIsDevelopment(): Boolean? = isDevelopment

    /**
     * Checks the equality of the passed object with the current object.
     *
     * @param other The object to compare with the current object.
     *
     * @return `true` if the objects are equal, `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is AuthenticationCoreConfig) return false

        return discoveryEndpoint == other.discoveryEndpoint &&
                authorizeEndpoint == other.authorizeEndpoint &&
                tokenEndpoint == other.tokenEndpoint &&
                userInfoEndpoint == other.userInfoEndpoint &&
                logoutEndpoint == other.logoutEndpoint &&
                authnEndpoint == other.authnEndpoint &&
                redirectUri == other.redirectUri &&
                clientId == other.clientId &&
                scope == other.scope &&
                integrityToken == other.integrityToken &&
                googleWebClientId == other.googleWebClientId &&
                isDevelopment == other.isDevelopment
    }

    /**
     * Generates a hash code for the current object.
     *
     * @return The hash code of the object.
     */
    override fun hashCode(): Int {
        var result = discoveryEndpoint.hashCode()
        result = 31 * result + (authorizeEndpoint?.hashCode() ?: 0)
        result = 31 * result + (tokenEndpoint?.hashCode() ?: 0)
        result = 31 * result + (userInfoEndpoint?.hashCode() ?: 0)
        result = 31 * result + (logoutEndpoint?.hashCode() ?: 0)
        result = 31 * result + (authnEndpoint?.hashCode() ?: 0)
        result = 31 * result + redirectUri.hashCode()
        result = 31 * result + clientId.hashCode()
        result = 31 * result + scope.hashCode()
        result = 31 * result + (integrityToken?.hashCode() ?: 0)
        result = 31 * result + (googleWebClientId?.hashCode() ?: 0)
        result = 31 * result + (isDevelopment?.hashCode() ?: 0)
        return result
    }
}
