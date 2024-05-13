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
# Asgardeo Android SDK

The Asgardeo Auth Android SDK enables Android applications (written in Kotlin) to utilize OpenID Connect (OIDC) authentication with Asgardeo serving as the Consumer Identity and Access Management (CIAM) Provider through application-native authentication. 

This SDK assists you in creating custom login flows directly within the applications themselves, without relying on browser redirects, thereby prioritizing user experience.

## Requirements and recommendations

* An Android application written in Kotlin programming language.
* The minimum supported SDK is API level 26, compiled to API level 34. However, there are certain limitations when using specific authentication methods:
    * Passkeys are only supported on API level 34 and above.
    * Google authentication using the Credential Manager API is supported on API level 34 and above.

## Getting Started

### Prerequisites

- [Register to Asgardeo and create an organization if you don't already have one](https://wso2.com/asgardeo/docs/get-started/create-asgardeo-account/). The organization name you choose will be referred to as `<org_name>` throughout this document.
- [Register a mobile application in Asgardeo to integrate your application with Asgardeo](https://wso2.com/asgardeo/docs/guides/applications/register-mobile-app/). You will obtain a `client_id` from Asgardeo for your application which will need to be embedded later for the SDK integration. Also note the redirect URI that you used to create the application, this is also required for the SDK integration.
- In the created mobile application, go to the **Advanced** tab and [enable the application native authentication for your Android application](https://is.docs.wso2.com/en/latest/guides/authentication/add-application-native-login/#enable-app-native-authentication).

### Installing the SDK

1. Open the `build.gradle` file of your Android application and add the following dependency to add the latest released SDK.

    ```groovy
    dependencies {
        implementation 'io.asgardeo:android:<latest-version>'
    }
    ```

    You can find the latest version of the SDK from the [Maven Repository](https://central.sonatype.com/artifact/io.asgardeo/android.ui).

2. Add a redirect scheme in the Android application. You need to add the `appAuthRedirectScheme` in the application `build.gradle` file.

    This should be consistent with the CallBack URL of the Service Provider that you configured in the Asgardeo.

    For example, if you have configured the CallBack URL as *wso2sample://oauth2*, then the `appAuthRedirectScheme` should be *wso2sample*.

    ```groovy
    android.defaultConfig.manifestPlaceholders = [
        'appAuthRedirectScheme': 'wso2sample' // [!code highlight]
    ]
    ```

### Start the authentication process

1. Initialize the SDK object `AsgardeoAuth`, to authenticate users into your application. This can be done in a repository if you are using an [MVVM](https://www.geeksforgeeks.org/mvvm-model-view-viewmodel-architecture-pattern-in-android/) pattern in your application.

    ```kotlin
    private val asgardeoAuth: AsgardeoAuth = AsgardeoAuth.getInstance(
        AuthenticationCoreConfig(
            discoveryEndpoint = "https://api.asgardeo.io/t/<org_name>/oauth2/token/.well-known/openid-configuration",
            authnEndpoint = "https://api.asgardeo.io/t/<org_name>/oauth2/authn",
            redirectUri = "wso2sample://oauth2",
            clientId = "<client_id>",
            scope = "openid profile email"
        )
    )
    ```
    <a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.core_config/-authentication-core-config/index.html" target="_blank">AuthenticationCoreConfig</a> holds the configuration details that are required to set up the communication between the SDK and the Asgardeo.

2. Get the <a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.provider.providers.authentication/-authentication-provider/index.html" target="_blank">AuthenticationProvider</a> from the created `AsgardeoAuth` instance. This will assist you in handling the authentication process.

```kotlin
val authenticationProvider: AuthenticationProvider = asgardeoAuth.getAuthenticationProvider()
```

<a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.provider.providers.authentication/-authentication-provider/index.html" target="_blank">AuthenticationProvider</a> handles the authentication process using `SharedFlow`. This will help you to handle each state of the authentication process easily. There are four states in the authentication process (<a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.models.state/-authentication-state/index.html" target="_blank">AuthenticationState</a>):

- **AuthenticationState.Initial**: Initial state of the authentication process.
- **AuthenticationState.Loading**: SDK is calling an API to handle the authentication and waiting for the result.
- **AuthenticationState.Unauthenticated**: This means the authentication flow is still not completed and a particular step is getting challenged for authentication. In this state, the list of available authenticators will be returned to you in a 
<a href="mobile-ui-sdks/android/api/core/io.asgardeo.android.core.models.authentication_flow/-authentication-flow-not-success/index.html" target="_blank">AuthenticationFlowNotSuccess</a> object.
- **AuthenticationState.Authenticated**: User is authenticated.
- **AuthenticationState.Error**: An error occurred during the authentication flow.

3. To start the authentication process, call `authenticationProvider.isLoggedInStateFlow`, this will check if there is an active session available, and if available, the authentication state will emit **AuthenticationState.Authenticated**, else will emit **AuthenticationState.Initial**.

    After that, you can call the `authenticationProvider.initializeAuthentication` to initialize the authentication process.

```kotlin
@Composable
internal fun LandingScreen() {
    val state = authenticationProvider.getAuthenticationStateFlow()

    // Initiate a call to /authorize endpoint only if a valid AT is not available
    authenticationProvider.isLoggedInStateFlow(context)  // [!code highlight]
    handleAuthenticationState(state)
}

private fun handleAuthenticationState(state: AuthenticationState) {
    authStateJob = state.collect {
        when (it) {
            is AuthenticationState.Initial -> {
                // pre /authorize
                authenticationProvider.initializeAuthentication(context)  // [!code highlight]
            }
            is AuthenticationState.Unauthenticated -> {
               /** 
                * Gets called when /authorize and /authn responds with an “INCOMPLETE” state. 
                * This means authentication flow is still not completed and a particular step is getting
                * challenged for authentication.
                */
                LoginForm(it.authenticationFlow)
            }
            is AuthenticationState.Error -> {
               /** 
                * Gets called when /authorize and /authn responds with an “FAILED_INCOMPLETE” state 
                * which responds at an error of a particular authentication step
                */
            }
            is AuthenticationState.Authenticated -> {
               /** 
                * Gets called when /authn responds with an “SUCCESS” state. This means 
                * authentication flow is completed
                */
                onSuccessfulLogin()
            }
            is AuthenticationState.Loading -> {
                // Show loading
            }
        }
    }
}
```

Assuming that you have configured Username and Password as the first authentication step and TOTP as the second step, you can develop the UI as follows using the <a href="mobile-ui-sdks/android/api/core/io.asgardeo.android.core.models.autheniticator/-authenticator-types/index.html" target="_blank">AuthenticatorTypes</a> provided by the SDK to populate the login form.
```kotlin
/**
 * Assuming the authentication process is, basic as first factor and TOTP as second factor
 */
@Composable
internal fun LoginForm() {
    authenticationFlow: AuthenticationFlowNotSuccess,
    onSuccessfulLogin: (User) -> Unit
) {
    authenticationFlow.nextStep.authenticators.forEach {
        when (it.authenticator) {
            AuthenticatorTypes.BASIC_AUTHENTICATOR.authenticatorType -> { // [!code highlight]
                BasicAuth(authenticatorType = it)
            }

            AuthenticatorTypes.TOTP_AUTHENTICATOR.authenticatorType -> { // [!code highlight]
                TotpAuth(authenticatorType = it)
            }
        }
    }
}
```

In the BasicAuth component you can call the authentication function provided by the <a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.provider.providers.authentication/-authentication-provider/index.html" target="_blank">AuthenticationProvider</a> to authenticate with username and password. Similarly, this can be done in other components as well.
```kotlin
@Composable
internal fun BasicAuth(authenticator: Authenticator) {
    BasicAuthComponent(
        onLoginClick = { username, password ->
            authenticationProvider.authenticateWithUsernameAndPassword(  // [!code highlight]
                username = username,
                password = password
            )
        }
    )
}

@Composable
fun BasicAuthComponent(
    onLoginClick: (username: String, password: String) -> Unit
) {
    Column() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username"
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )
        Button(onClick = { onLoginClick(username, password) }) {
            Text(text = "Login")
        }
    }
}
```

You will not need to handle the authentication state in multiple places, you can do it at the start of the application, and it will handle the state accordingly.

### Get user details

After the user is authenticated, to get user-related information, we can use the following function. This will return the user details in a `LinkedHashMap`.

```kotlin
coroutineScope.launch {
    runCatching {
        authenticationProvider.getUserDetails(<context>) // [!code highlight]
    }.onSuccess { userDetails ->
        Profile(userDetails)
    }.onFailure { e ->
        // Display error message
    }
}
```

### Get token information

To get information, you can use the <a href="mobile-ui-sdks/android/api/core/io.asgardeo.android.core.provider.providers.token/-token-provider/index.html" target="_blank">TokenProvider</a>. This will assist you in getting token-related information and performing actions on the tokens.

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

### Perform action based on the tokens

If you want to perform any action based on the tokens that are returned, you can use the `performAction` function in the <a href="mobile-ui-sdks/android/api/core/io.asgardeo.android.core.provider.providers.token/-token-provider/index.html" target="_blank">TokenProvider</a>.

```kotlin
tokenProvider.performAction(context) { accessToken, idToken, ->
    action(accessToken, idToken)
}
```

### Logout

If you want to perform a logout, you can call the `logout` function in the <a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.provider.providers.authentication/-authentication-provider/index.html" target="_blank">AuthenticationProvider</a>. This will emit the state **AuthenticationState.Initial** if the logout is successful, and if an error occurs, it will emit **AuthenticationState.Error**.

```kotlin
authenticationProvider.logout(context)
```

### Client attestation
You can also use client attestation with the SDK as well.

#### How to setup client attestation for your application

1. In the created mobile application in Asgardeo, go **Advanced** and check **Enable client attestation**.
2. Subscribe to the "Google Play Integrity API" from your Google Cloud project. You can do this from **Enabled API's & Services** in your Google Cloud project.
3. Create a new service account in the Google project. You can create a Service Account for yourself with the following steps.

    1. In your Google project, go to **IAM & Admin** -> **Service Accounts**.
    2. Click **Create Service Account**.
    3. Fill in the name and click **Create**.
    4. Grant your service account the roles of Service Account User and Service Usage Consumer.
    5. Click **Continue** and then **Done**
    6. You can see the service account added without keys, go to **Actions** -> **Manage Keys** on the service account.
    7. Click **Add** key and Select `JSON`.
    8. Save the JSON in a secure place as this is required for Android Attestation Credentials for application metadata.

4. After that, update the Application's Advanced properties. The application you created requires two properties to perform Android attestation.

    1. Android package name
    2. `androidAttestationServiceCredentials`
       The downloaded JSON secret of the Service Account. Note that this attribute is defined as a JSON object hence use the JSON key as it is.

5. Now you can call the **Google Play Integrity API** from your application and pass the integrity token that you will get from the API result to the `integrityToken` value of the `AuthenticationCoreConfig`.

```kotlin
private val asgardeoAuth: AsgardeoAuth = AsgardeoAuth.getInstance(
    AuthenticationCoreConfig(
        discoveryEndpoint = "https://api.asgardeo.io/t/<org_name>/oauth2/token/.well-known/openid-configuration",
        redirectUri = "wso2sample://oauth2",
        clientId = "<client_id>",
        scope = "openid profile",
        integrityToken = "<integrity_token>"  // [!code highlight]
    )
)
```
