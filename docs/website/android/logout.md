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

# Logout

If you want to perform a logout, you can call the `logout` function in the <a href="/mobile-ui-sdks/android/api/core/io.asgardeo.android.core.provider.providers.authentication/-authentication-provider/index.html" target="_blank">AuthenticationProvider</a>. This will emit the state **AuthenticationState.Initial** if the logout is successful, and if an error occurs, it will emit **AuthenticationState.Error**.

```kotlin
authenticationProvider.logout(context)
```
