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

# Start the Authentication Process

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
