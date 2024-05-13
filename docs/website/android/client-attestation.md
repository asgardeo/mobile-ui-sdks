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

# Client Attestation
You can also use client attestation with the SDK as well.

## How to setup client attestation for your application

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
