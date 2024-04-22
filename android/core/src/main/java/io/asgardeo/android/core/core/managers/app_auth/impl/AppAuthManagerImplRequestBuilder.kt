package io.asgardeo.android.core.core.managers.app_auth.impl

import android.net.Uri
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenRequest

/**
 * Request builder functions related to the [AppAuthManagerImpl]
 */
object AppAuthManagerImplRequestBuilder {
    /**
     * Use to get the [TokenRequest.Builder] instance.
     *
     * @return The [TokenRequest.Builder] instance.
     */
    private fun getTokenRequestBuilder(
        serviceConfig: AuthorizationServiceConfiguration,
        clientId: String
    ): TokenRequest.Builder = TokenRequest.Builder(
        serviceConfig,
        clientId
    )

    /**
     * Use to get the [TokenRequest] instance to exchange the authorization code for the access token.
     *
     * @param serviceConfig The [AuthorizationServiceConfiguration] instance.
     * @param clientId The client id.
     * @param authorizationCode The authorization code.
     * @param redirectUri The redirect uri.
     *
     * @return The [TokenRequest] instance.
     */
    internal fun getExchangeAuthorizationCodeRequestBuilder(
        serviceConfig: AuthorizationServiceConfiguration,
        clientId: String,
        authorizationCode: String,
        redirectUri: Uri
    ): TokenRequest =
        getTokenRequestBuilder(serviceConfig, clientId)
            .setAuthorizationCode(authorizationCode)
            .setClientId(clientId)
            .setRedirectUri(redirectUri)
            .build()

    /**
     * Use to get the [TokenRequest] instance to refresh the access token.
     *
     * @param serviceConfig The [AuthorizationServiceConfiguration] instance.
     * @param clientId The client id.
     * @param refreshToken The refresh token.
     * @param redirectUri The redirect uri.
     *
     * @return The [TokenRequest] instance.
     */
    internal fun getRefreshTokenRequestBuilder(
        serviceConfig: AuthorizationServiceConfiguration,
        clientId: String,
        refreshToken: String,
        redirectUri: Uri
    ): TokenRequest = getTokenRequestBuilder(serviceConfig, clientId)
        .setGrantType(GrantTypeValues.REFRESH_TOKEN)
        .setRedirectUri(redirectUri)
        .setRefreshToken(refreshToken)
        .build()

}
