import java.net.URI

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

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "io.asgardeo.android"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        aarMetadata {
            minCompileSdk = properties["COMPILE_SDK_VERSION"].toString().toInt()
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    api(project(":core"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

extra.apply {
    set("artifactId", "android")
    set("artifactName", "main")
    set(
        "artifactDescription",
        "A library that provides all the necessary functionalities to integrate your Android application with Asgardeo."
    )
    set("versionNumber", properties["ANDROID_VERSION"])
}

// apply("${rootDir}/publish.gradle")
// publish.gradle.kts

// TOOD: the following code block to a separate gradle.kts file. Currently placed here due to an Android Studio bug, where new gradle.kts files are not recognized.

// artifact related variables
val groupName: String = rootProject.extra.get("groupName") as String
val packagingType: String = rootProject.extra.get("groupName") as String
var publishArtifactId: String = project.extra.get("artifactId") as String
val artifactName = project.extra.get("artifactName") as String
val artifactDescription = project.extra.get("artifactDescription") as String
val versionNumber = project.extra.get("versionNumber") as String

// POM related variables
val pomUrl: String = properties["POM_URL"].toString()
val pomLicenseName: String = properties["POM_LICENCE_NAME"].toString()
val pomLicenseUrl: String = properties["POM_LICENCE_URL"].toString()
val pomLicenseDistribution: String = properties["POM_LICENCE_DISTRIBUTION"].toString()

// SCM related variables
val scmConnection: String = properties["POM_SCM_CONNECTION"].toString()
val scmDeveloperConnection: String = properties["POM_SCM_DEV_CONNECTION"].toString()
val scmUrl: String = properties["POM_SCM_URL"].toString()

// Developer related variables
val developerId: String = properties["POM_DEVELOPER_ID"].toString()
val developerName: String = properties["POM_DEVELOPER_NAME"].toString()

// WSO2 nexus repository related variables
val wso2NexusReleaseRepositoryUrl: URI =
    rootProject.extra.get("wso2NexusReleaseRepositoryUrl") as URI
val wso2NexusSnapshotRepositoryUrl: URI =
    rootProject.extra.get("wso2NexusSnapshotRepositoryUrl") as URI
val wso2NexusRepositoryUsername: String =
    rootProject.extra.get("wso2NexusRepositoryUsername") as String
val wso2NexusRepositoryPassword: String =
    rootProject.extra.get("wso2NexusRepositoryPassword") as String

val androidSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
}

artifacts {
    archives(androidSourcesJar)
}

afterEvaluate {
    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("release") {
                groupId = groupName// Access groupId from rootProject.ext
                artifactId = publishArtifactId
                version = versionNumber

                artifact(
                    "${layout.buildDirectory.get()}/outputs/aar/${artifactName}-release.aar"
                )
                artifact(androidSourcesJar)

                pom {
                    name.set(publishArtifactId)
                    description.set(artifactDescription)
                    packaging = packagingType
                    url.set(pomUrl)
                    licenses {
                        license {
                            name.set(pomLicenseName)
                            url.set(pomLicenseUrl)
                            distribution.set(pomLicenseDistribution)
                        }
                    }
                    // Version control info.
                    scm {
                        connection.set(scmConnection)
                        developerConnection.set(scmDeveloperConnection)
                        url.set(scmUrl)
                    }
                    developers {
                        developer {
                            id = developerId
                            name = developerName
                        }
                    }
                }
            }
        }

//        signing {
//            isRequired = gradle.taskGraph.hasTask("publish")
//            useGpgCmd()
//            sign(publishing.publications["release"])
//        }

        repositories {
            maven {
                credentials {
                    username = wso2NexusRepositoryUsername
                    password = wso2NexusRepositoryPassword
                }

                url = if (versionNumber.endsWith("SNAPSHOT")) wso2NexusSnapshotRepositoryUrl
                else wso2NexusReleaseRepositoryUrl
            }
        }
    }
}
