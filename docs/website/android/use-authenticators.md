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

# How to use authenticators with the Android SDK

The Asgardeo Auth SDK provides out-of-the-box support for some authenticators, which are accessible via the <a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.provider.providers.authentication/-authentication-provider/index.html" target="_blank">AuthenticationProvider</a>. Each of the following functions will emit the aforementioned <a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.models.state/-authentication-state/index.html" target="_blank">AuthenticationState</a>, except for the **AuthenticationState.Initial**.

Before utilizing these authenticators, you need to integrate them into your application's login flow. You can find more information about this in the following [link](https://wso2.com/asgardeo/docs/guides/authentication/).

## Use any authentication mechanism

If you are using any other authentication mechanism like twitter login, you can use the `authenticate` function. For this, you need to pass the authenticator id or authenticator which can be retrieved from the `authenticationFlow` returned from the **Authentication.Unauthenticated** state.

This can be used in two ways:

### Authenticator parameters are known

If you are aware of the authenticator parameters required for the authenticator, you can directly call this function to authenticate the user with this authenticator.

```kotlin
authenticationProvider.authenticate(
    context,
    authenticator = authenticator,
    authParams = < as a LinkedHashMap<String, String> >
)
```

### Authenticator parameters are not known

If you are not aware of the authenticator parameters required for the authenticator, you first need to retrieve the parameters required to authenticate the user with this authenticator. For this, you can use the following function:

```kotlin
val detailedAuthenticator: Authenticator = authenticationProvider.selectAuthenticator(
    authenticator = authenticator
)
```

This will return a fully detailed authenticator object. In that object, you can get the required authentication parameters from:

```kotlin
val requiredParams: List<String>? = detailedAuthenticator.requiredParams
```

After that, you can manually set the relevant required authentication parameters and call the `authenticate` function:

```kotlin
authenticationProvider.authenticate(
    context,
    authenticator = authenticator,
    authParams = < as a LinkedHashMap<String, String> >
)
```

## Use Basic authentication

```kotlin
authenticationProvider.authenticateWithUsernameAndPassword(
    context = context,
    authenticatorId = authenticator.authenticatorId,
    username = username,
    password = password
)
```

## Use TOTP authentication

```kotlin
authenticationProvider.authenticateWithTotp(
    context = context,
    authenticatorId = authenticator.authenticatorId,
    token = token
)
```

## Use Google authentication
Before using the Google autenticator make sure to add the Google web client ID you added in Asgardeo in the <a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.core_config/-authentication-core-config/index.html" target="_blank">AuthenticationCoreConfig</a> object used to initialized the `AsgardeoAuth` object

```kotlin
private val asgardeoAuth: AsgardeoAuth = AsgardeoAuth.getInstance(
        AuthenticationCoreConfig(
            ...
            googleWebClientId = "<google_web_client_id>" // [!code highlight]
            ...
        )
    )
```

this will use to get the `idToken` of the Google web client that you add in Asgardeo.

### Using credential manager API (Supports API 34 and above)

```kotlin
authenticationProvider.authenticateWithGoogle(
    context,    
    authenticatorId = authenticator.authenticatorId
)
```

### Using legacy one tap (Not recommended for newer applications)

```kotlin
val launcher: ActivityResultLauncher<Intent> = rememberLauncherForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    authenticationProvider.handleGoogleNativeLegacyAuthenticateResult(
        context,
        result.resultCode,
        result.data
    )
}

authenticationProvider.authenticateWithGoogleLegacy(
    context,
    authenticatorId = authenticator.authenticatorId
    launcher
)
```

If you are not using Jetpack Compose for development, you can call `handleGoogleNativeLegacyAuthenticateResult` in the activity `onActivityResult`.

```kotlin
class YourActivity : AppCompatActivity() {
    // ...

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authenticationProvider.handleGoogleNativeLegacyAuthenticateResult( // [!code highlight]
            context,
            result.resultCode,
            result.data
        )
    }
    // ...
}
```

This will invoke the Google one tap authentication mechanism.

## Use Passkey authentication (Supports API 34 and above)

```kotlin
authenticationProvider.authenticateWithPasskey(
    context,
    authenticatorId = authenticator.authenticatorId,
)
```

## Use redirect based authentication

To perform redirect based authentication using a federated authenticator, first, you need to add the following code snippet to your application build.gradle file. This will add a separate activity that will handle the redirection on your behalf.

```gradle
android.defaultConfig.manifestPlaceholders = [
     ...
     'callbackUriHost': '<host>', // [!code highlight]
     'callbackUriScheme': '<scheme>' // [!code highlight]
     ...	
]
```

For example, if you want to add the deep link as *wso2sample://callback*, then the code snippet should be as follows,

```gradle
android.defaultConfig.manifestPlaceholders = [
     ...
     'callbackUriHost': 'callback', // [!code highlight]
     'callbackUriScheme': 'wso2sample' // [!code highlight]
     ...	
]
```
Also this deep link should be configured in the federated IdP side as a valid redirect URI.

### Use redirect Github authentication

```kotlin
authenticationProvider.authenticateWithGithubRedirect(
    context,
    authenticatorId = authenticator.authenticatorId
)
```

### Use redirect Microsoft authentication

```kotlin
authenticationProvider.authenticateWithMicrosoftRedirect(
    context,
    authenticatorId = authenticator.authenticatorId
)
```

### Use OpenId Connect authentication

```kotlin
authenticationProvider.authenticateWithOpenIdConnect(
   context,
    authenticatorId = authenticator.authenticatorId
)
```
