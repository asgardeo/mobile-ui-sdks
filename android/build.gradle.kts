/*
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

import java.net.URI
import java.time.Duration
import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false

    id("org.jetbrains.dokka") version "1.9.20"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

apply(plugin = "org.jetbrains.dokka")
apply(plugin = "io.github.gradle-nexus.publish-plugin")
apply(plugin = "java-library")

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "org.jetbrains.dokka")
}

val groupName: String = properties["GROUP"] as String

// Read local.properties file to get the Nexus credentials
val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { stream ->
        localProperties.load(stream)
    }
}

// Get the WSO2 Nexus repository URL
fun getWSO2NexusReleaseRepositoryUrl(): URI =
    URI.create((properties["NEXUS_RELEASE_URL"] ?: "").toString())

// Get the WSO2 Nexus snapshot repository URL
fun getWSO2NexusSnapshotRepositoryUrl(): URI =
    URI.create((properties["NEXUS_SNAPSHOT_URL"] ?: "").toString())

// Get the WSO2 Nexus repository username
fun getWSO2NexusRepositoryUsername(): String =
    (localProperties.getProperty("NEXUS_USERNAME") ?: "").toString()

// Get the WSO2 Nexus repository password
fun getWSO2NexusRepositoryPassword(): String =
    (localProperties.getProperty("NEXUS_PASSWORD") ?: "").toString()

nexusPublishing {
    group = groupName
    packageGroup = groupName

    repositories {
        create("wso2Nexus") {
            nexusUrl.set(getWSO2NexusReleaseRepositoryUrl())
            snapshotRepositoryUrl.set(getWSO2NexusSnapshotRepositoryUrl())
            username.set(getWSO2NexusRepositoryUsername())
            password.set(getWSO2NexusRepositoryPassword())
        }
    }

    clientTimeout = Duration.ofMinutes(5)
    connectTimeout = Duration.ofMinutes(1)

    transitionCheckOptions {
        maxRetries.set(40)
        delayBetween.set(Duration.ofSeconds(10))
    }
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(File("${project.rootDir}/docs"))
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

extra.apply {
    set("groupName", groupName)
    set("minCompileSdkVersion", properties["MIN_COMPILE_SDK"])
    set("packagingType", properties["PACKAGING_TYPE"])
    set("wso2NexusReleaseRepositoryUrl", getWSO2NexusReleaseRepositoryUrl())
    set("wso2NexusSnapshotRepositoryUrl", getWSO2NexusSnapshotRepositoryUrl())
    set("wso2NexusRepositoryUsername", getWSO2NexusRepositoryUsername())
    set("wso2NexusRepositoryPassword", getWSO2NexusRepositoryPassword())
}
