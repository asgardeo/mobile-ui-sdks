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

# Get User Details

After the user is authenticated, to get user-related information, we can use the following function. This will return the user details in a `LinkedHashMap`.

```kotlin
GlobalScope.launch {
    runCatching {
        authenticationProvider.getBasicUserInfo(<context>) // [!code highlight]
    }.onSuccess { userDetails ->
        Profile(userDetails)
    }.onFailure { e ->
        // Display error message
    }
}
```

> [!NOTE]
> You cannot directly use Composable functions inside a coroutine, above its used to show the flow of the application. You can use the `getBasicUserInfo` function in a ViewModel or a Repository and observe the result in the Composable function.

> [!CAUTION]
> When using `GlobalScope`, make sure to cancel the coroutine when the composable is removed from the screen. This is to avoid memory leaks.
> Also instead of using `GlobalScope`, you can use `viewModelScope` if you are using the MVVM pattern in your application.
> For more information on how to handle coroutines in Jetpack Compose, see [the following documentation](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-global-scope/).