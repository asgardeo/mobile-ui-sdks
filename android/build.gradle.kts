import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.time.Duration

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
}

val groupName: String = properties["GROUP"] as String

nexusPublishing {
    packageGroup = groupName

    repositories {
        create("wso2Nexus") {
            nexusUrl.set(uri("https://your-server.com/staging"))
            snapshotRepositoryUrl.set(uri("https://your-server.com/snapshots"))
            username.set("your-username") // defaults to project.properties["myNexusUsername"]
            password.set("your-password") // defaults to project.properties["myNexusPassword"]
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
    set("compileSdkVersion", properties["COMPILE_SDK_VERSION"])
    set("packagingType", properties["PACKAGING_TYPE"])
}
