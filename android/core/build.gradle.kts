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
    namespace = "io.asgardeo.android.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // For Google and Passkey authentication
    implementation(libs.androidx.credentials)
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation(libs.androidx.credentials.play.services.auth)
    implementation (libs.googleid)
    implementation(libs.play.services.auth)

    // HTTP client
    implementation(libs.okhttp)

    // JSON parser
    implementation (libs.jackson.module.kotlin)

    // App auth module
    implementation (libs.appauth)

    // Data store
    implementation(libs.androidx.datastore.preferences)
}

extra.apply {
    set("artifactId", "core")
    set("artifactName", "core")
    set("artifactDescription", "A library that provides all the necessary authentication functionality to integrate your Android application with the Asgardeo and WSO2 Identity Server.")
    set("versionNumber", properties["CORE_VERSION"])
}

//apply("${rootDir}/publish.gradle")
// publish.gradle.kts

// TOOD: the following code block to a separate gradle.kts file. Currently placed here due to an Android Studio bug, where new gradle.kts files are not recognized.

// artificial related variables
val groupName: String = rootProject.extra.get("groupName") as String
val packagingType: String = rootProject.extra.get("groupName") as String
var publishArtifactId: String = project.extra.get("artifactId") as String
val artifactName = project.extra.get("artifactName") as String
val artifactDescription = project.extra.get("artifactDescription") as String
val versionNumber = project.extra.get("versionNumber") as String

//POM related variables
val pomUrl: String = properties["POM_URL"].toString()
val pomLicenseName: String = properties["POM_LICENCE_NAME"].toString()
val pomLicenseUrl: String = properties["POM_LICENCE_URL"].toString()
val pomLicenseDistribution: String = properties["POM_LICENCE_DISTRIBUTION"].toString()

//SCM related variables
val scmConnection: String = properties["POM_SCM_CONNECTION"].toString()
val scmDeveloperConnection: String = properties["POM_SCM_DEV_CONNECTION"].toString()
val scmUrl: String = properties["POM_SCM_URL"].toString()

fun getReleaseRepositoryUrl(): URI = URI.create(System.getenv("NEXUS_RELEASE_URL") ?: "")

fun getSnapshotRepositoryUrl(): URI = URI.create(System.getenv("NEXUS_SNAPSHOT_URL") ?: "")

fun getRepositoryUsername(): String = System.getenv("NEXUS_USERNAME") ?: ""

fun getRepositoryPassword(): String = System.getenv("NEXUS_PASSWORD") ?: ""

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

                artifact("${layout.buildDirectory.get()}/outputs/aar/${artifactName}-release.aar")
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
                }
            }
        }

//        signing {
//            isRequired = gradle.taskGraph.hasTask("publish")
//            useGpgCmd()
//            sign(publishing.publications["release"])
//        }

        repositories {
            mavenLocal()
            maven {
                credentials {
                    username = getRepositoryUsername()
                    password = getRepositoryPassword()
                }

                url = if (versionNumber.endsWith("SNAPSHOT")) getSnapshotRepositoryUrl()
                else getReleaseRepositoryUrl()
            }
        }
    }
}
