<!--
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
-->

# Get Token Information

To get information, you can use the <a href="mobile-ui-sdks/android/api/core-auth-direct/io.asgardeo.android.core_auth_direct.provider.providers.token/-token-provider/index.html" target="_blank">TokenProvider</a>. This will assist you in getting token-related information and performing actions on the tokens.

```kotlin
val tokenProvider: TokenProvider = asgardeoAuth.getTokenProvider()
```

To get the token-related information, you can use the following functions:

```kotlin
val accessToken: String? = tokenProvider.getAccessToken(context)
val idToken: String? = tokenProvider.getIDToken(context)
val refreshToken: String? = tokenProvider.getRefreshToken(context)
val accessTokenExpirationTime: Long? = tokenProvider.getAccessTokenExpirationTime(context)
val scope:String? = tokenProvider.getScope(context)
```

## Perform action based on the tokens

If you want to perform any action based on the tokens that are returned, you can use the `performAction` function in the <a href="mobile-ui-sdks/android/api/core-auth-direct/io.asgardeo.android.core_auth_direct.provider.providers.token/-token-provider/index.html" target="_blank">TokenProvider</a>.

```kotlin
tokenProvider.performAction(context) { accessToken, idToken, ->
    action(accessToken, idToken)
}
```
