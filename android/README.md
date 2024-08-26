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

To see the full details on how to integrate the Asgardeo Android SDK, refer to following <a href="https://asgardeo.github.io/mobile-ui-sdks/android/introduction.html" target="_blank">documentation link</a>.

## Architecture of the Asgardeo Android SDK

The full details on the architecture of the Asgardeo Android SDK can be found [here](./ARCHITECTURE.md).

## Development of the Asgardeo Android SDK

The Asgardeo Android SDK is developed using Kotlin and is built using the Android SDK. The SDK is developed to be optimized with the MVVM architecture.

### Setup the Development Environment

- Required versions.
    - [Android Studio](https://developer.android.com/studio) Giraffe (2020.3.1) or later.
    - Java 17 or later.

### Build the SDK

1. Clone the repository.
2. Open the project in Android Studio, and select the project to open with an `Android view` from the project view selection.
3. You may require to sync the gradle files again. This can be done using the `Sync project with Gradle files` icon in the top right hand corner of the IDE (or in Apple `Shift + Command + O`).
4. Build the project using the following command.
    ```shell
    ./gradlew clean build
    ```
    This can also be done using the [Gradle side panel in Android Studio](https://www.jetbrains.com/help/idea/jetgradle-tool-window.html).

### Release the SDK to Local Maven Repository

1. Now you can make changes to the SDK. Once you have made the changes, you can build the SDK again using the above command.
2. After building the SDK, you can release the SDK to the local Maven repository using the following command.
    ```shell
    ./gradlew clean assembleRelease
    ./gradlew publishToMavenLocal
    ```
    This will publish the SDK to the local Maven repository. You can find the SDK in the following location.
    ```
    ~/.m2/repository/io/asgardeo/asgardeo-android/<MAIN_VERSION>
    ```
    You can also find the SDK in the `External Libraries` section in the project view of Android Studio.
3. Note the version of the SDK that you have released to the local Maven repository. You can find the version in the `gradle.properties` file in the root of the SDK project, in the variable `MAIN_VERSION`.

> [!CAUTION]
> When commiting changes to the Github, make sure <b>NOT TO UPDATE</b> the version of the SDK in the `gradle.properties` file. This should be updated only when releasing the SDK to the Maven repository using the [release Github Action](https://github.com/asgardeo/mobile-ui-sdks/actions/workflows/release.yml).

### Test the Changes

You can test the changes by running any Android application and including the SDK as a dependency. But it is recommended to use the sample application provided in [wso2/sample-is](https://github.com/wso2/samples-is/tree/master/petcare-sample/b2c/mobile-app/petcare-with-sdk) repository.

#### Setup the Sample Application

1. Clone the [wso2/sample-is](https://github.com/wso2/samples-is) repository.
2. Open the `petcare-with-sdk`(<SAMPLE-IS>/petcare-sample/b2c/mobile-app/petcare-with-sdk) project in Android Studio.
3. Read this [documentation](https://github.com/wso2/samples-is/blob/master/petcare-sample/b2c/mobile-app/petcare-with-sdk/README.md) and setup the sample application with the necessary configurations to test your changes.

#### Include the changed SDK in the Sample Application

1. Open the `libs.version.toml` file in the root of the sample application and change the version of the SDK to the version that you have released to the local Maven repository.
    ```toml
    [versions]
    ...
    asgardeoAndroid = "1.0.0-SNAPSHOT"
    ...
    ```

2. Open the `settings.gradle` file in the root of the sample application and include `mavenLocal()` in the repositories.
    ```gradle
    pluginManagement {
        repositories {
            ...
            mavenLocal()
        }
    }

    dependencyResolutionManagement {
        repositories {
            ...
            mavenLocal()
        }
    }
    ```
 > [!TIP]
 > There are some reports that the `mavenLocal()` repository is not being resolved in the `settings.gradle` file. To 
 > resolve this issue, please refer to this StackOverflow [thread](https://stackoverflow.com/questions/32107205/gradle-does-not-use-the-maven-local-repository-for-a-new-dependency).


3. Build and run the sample application using the above mentioned [documentation](https://github.com/wso2/samples-is/blob/master/petcare-sample/b2c/mobile-app/petcare-with-sdk/README.md).

### Create a Pull Request

After testing the changes, you can create a pull request to the [asgardeo/mobile-ui-sdks](https://github.com/asgardeo/mobile-ui-sdks/) repository. For more details about this, refer to the [CONTRIBUTING.md](../CONTRIBUTING.md) file.
