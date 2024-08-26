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

# Error Handling in the Asgardeo Android SDK

Error handling is an important aspect of any application. It is crucial to handle errors gracefully and provide meaningful error messages to the user. This guide provides details on error handling in the Asgardeo Android SDK.

As mentioned in the [Start the Authentication Process](./start.md) guide, the `AuthenticationState.Error` state is triggered when an error occurs during the authentication process. This state provides an error message that can be displayed to the user.

### How to Capture Errors in the Authentication Process
The above mentioned error state is triggered when an error occurs during the authentication process, and throws a `FlowManagerException`. You can capture this error and handle it as shown below:

```kotlin
..
is AuthenticationState.Error -> {
    when (it.throwable::class.java.simpleName) {
        "FlowManagerException" -> {
            // Handle the error
        }
    }
}
..
```

You can handle this error by displaying a meaningful error message to the user like a toast message or a snackbar.

```kotlin
..
is AuthenticationState.Error -> {
    when (it.throwable::class.java.simpleName) {
        "FlowManagerException" -> {
            Toast.makeText(context, "An error occurred during the authentication process", Toast.LENGTH_SHORT).show()
        }
    }
}
..
```

Also if you want to show an error message in the TextFields, you have to navigate to the login component and show the error message.

To view the specific error message, you can use the the `messages` property of the `FlowManagerException` as shown below:

```kotlin
val messages: ArrayList<any> = (it.throwable as FlowManagerException).messages
```
This will return an array of error messages that is returned by Asgardeo, explaining the error in detail.

### Authenticator Not Found Error

If the used authenticator is not found in current authentication step, the `AuthenticationState.Error` state is triggered with a `AuthenticatorProviderException` that contains the error message `Authenticator not found`. You can capture this error and handle it as shown below:

```kotlin
..
is AuthenticationState.Error -> {
    when (it.throwable::class.java.simpleName) {
        "AuthenticatorProviderException" -> {
            // Handle the error
        }
    }
}
..
```

But the best approach is to avoid this error by checking the availability of the authenticator before using it. This can be done by dynamically checking the availability of the authenticator as shown in the [Start the Authentication Process](./start.md) guide.

### Authenticator Specific Errors

#### Redirect Based Authentication Errors

If the authenticator is a redirect-based authenticator, the `AuthenticationState.Error` state is triggered with a `RedirectAuthenticationException` that contains the error messages like `Redirect URI not found`. You can capture this error and handle it as shown below:

```kotlin
..
is AuthenticationState.Error -> {
    when (it.throwable::class.java.simpleName) {
        "RedirectAuthenticationException" -> {
            // Handle the error
        }
    }
}
..
```

You can see all the error messages that can be thrown by the redirect-based authenticator in the <a href="/mobile-ui-sdks/android/api/core-auth-direct/io.asgardeo.android.core_auth_direct.models.exceptions/-redirect-authentication-exception/-companion/index.html" target="_blank">RedirectAuthenticationException</a> from here.

#### Google Authentication Error (Legacy & New)

If the authenticator is Google, the `AuthenticationState.Error` state is triggered with a `GoogleNativeAuthenticationException` that contains the error messages like `Google Web Client ID is not set`. You can capture this error and handle it as shown below:

```kotlin
..
is AuthenticationState.Error -> {
    when (it.throwable::class.java.simpleName) {
        "GoogleNativeAuthenticationException" -> {
            // Handle the error
        }
    }
}
..
```

You can see all the error messages that can be thrown by the Google authenticator in the <a href="/mobile-ui-sdks/android/api/core-auth-direct/io.asgardeo.android.core_auth_direct.models.exceptions/-google-native-authentication-exception/-companion/index.html" target="_blank">GoogleNativeAuthenticationException</a> from here.

#### Passkey Authentication Error

If the authenticator is Passkey, the `AuthenticationState.Error` state is triggered with a `PasskeyAuthenticationException` that contains the error messages like `Passkey Authentication is not supported`. You can capture this error and handle it as shown below:

```kotlin
..
is AuthenticationState.Error -> {
    when (it.throwable::class.java.simpleName) {
        "PasskeyAuthenticationException" -> {
            // Handle the error
        }
    }
}
..
```

You can see all the error messages that can be thrown by the Passkey authenticator in the <a href="/mobile-ui-sdks/android/api/core-auth-direct/io.asgardeo.android.core_auth_direct.models.exceptions/-passkey-authentication-exception/-companion/index.html" target="_blank">PasskeyAuthenticationException</a> from here.
