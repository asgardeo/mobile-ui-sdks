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

# Best Practices when using the Asgardeo Android SDK

This guide provides best practices to follow when using the Asgardeo Android SDK. Following these best practices will help you to integrate the SDK seamlessly into your Android application.

## Use the latest version of the SDK

Always use the latest version of the Asgardeo Android SDK to ensure that you have access to the latest features and bug fixes. You can find the latest version of the SDK in the [Maven Repository](https://central.sonatype.com/artifact/io.asgardeo/asgardeo-android).

## MVVM architecture

When integrating the Asgardeo Android SDK into your application, it is recommended to use the [Model-View-ViewModel (MVVM) architecture](https://www.geeksforgeeks.org/mvvm-model-view-viewmodel-architecture-pattern-in-android/). This architecture helps to separate the UI logic from the business logic and provides a clean and maintainable codebase.

## Use `Dispatchers.IO` 

Suspended functions in the SDK are designed to be optimized for `Dispatchers.IO` context. 