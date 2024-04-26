# -------------------------------------------------------------------------------------
#
# Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
#
# WSO2 LLC. licenses this file to you under the Apache License,
# Version 2.0 (the "License"); you may not use this file except
# in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
#
# --------------------------------------------------------------------------------------

#!/bin/bash

VERSION_TYPE=$1

# Go to android sdk directory
go_to_android_sdk_dir() {
  cd ../../../../android
}

# Go to scripts directory
go_to_scripts_dir() {
  cd ../.github/workflows/scripts/android
}

# Function to update versions
update_versions() {
  echo 
  bash version.sh $VERSION_TYPE
}

# Function to regenerate Gradle wrapper
regenerate_gradle_wrapper() {
  echo 
  ./gradlew wrapper
}

# Function to check Gradle wrapper
check_gradle_wrapper() {
  echo 
  if [ ! -f "./gradlew" ]; then
    echo "Gradle wrapper (./gradlew) not found. Please regenerate using './gradlew wrapper'"
    exit 1
  fi

  echo "Gradle Wrapper - OK"

  if [ ! -f "./gradlew.bat" ]; then
    echo "Gradle wrapper (./gradlew.bat) not found. Please regenerate using './gradlew wrapper'"
    exit 1
  fi

  echo "Gradle Wrapper (Windows) - OK"

  ./gradlew --version
  if [ $? -ne 0 ]; then
    echo "Error: Gradle wrapper execution failed. Please check Gradle installation and configuration."
    exit 1
  fi
  echo "Gradle Wrapper - OK"
}

# Function to build with Gradle
gradle_build() {
  echo 
  ./gradlew clean build
}

# Function to assemble with Gradle
gradle_assemble() {
  echo 
  ./gradlew assembleRelease
}

# Function to generate API docs
generate_api_docs() {
  echo
  ./gradlew dokkaHtmlMultiModule
}

release_android_sdks() {
    # Go to android sdk directory
    go_to_android_sdk_dir

    # Functions to release Android SDKs
    regenerate_gradle_wrapper
    check_gradle_wrapper
    gradle_build
    gradle_assemble
    generate_api_docs

    # Go to scripts directory
    go_to_scripts_dir
}

# Function to update snapshot version
update_snapshot_version() {
  echo 
  bash update_snapshot_version.sh $NEW_VERSION
}

# Call the functions in sequence
update_versions
release_android_sdks
update_snapshot_version
