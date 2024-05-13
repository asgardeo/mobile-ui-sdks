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
