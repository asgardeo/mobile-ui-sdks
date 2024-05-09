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

# version type: major, minor, patch
VERSION_TYPE=$1
# nexus username
NEXUS_USERNAME=$2
# nexus password
NEXUS_PASSWORD=$3
# Github token
GH_TOKEN=$4
# Github action run number
GITHUB_RUN_NUMBER=$5
# Github repository
GITHUB_REPOSITORY=$6
# The release branch name
RELEASE_BRANCH=release-action-$GITHUB_RUN_NUMBER

# Main version
MAIN_VERSION=""
# Core version
CORE_VERSION=""

# Go to android sdk directory
go_to_android_sdk_dir() {
  cd ../../../../android
}

# Go to scripts directory
go_to_scripts_dir() {
  cd ../.github/workflows/scripts/android
}

# Go to common scripts directory
go_to_common_scripts_dir() {
  cd ../common
}

# Function to update versions
update_versions() {
  echo 
  bash version.sh $VERSION_TYPE

  go_to_android_sdk_dir
  source updated_versions.txt
  # Access the versions as variables
  MAIN_VERSION=${MAIN_VERSION}
  CORE_VERSION=${CORE_VERSION}

  go_to_scripts_dir
}

# Function to update Nexus credentials
update_nexus_credentials() {
  echo 
  bash ./update_local_properties.sh $NEXUS_USERNAME $NEXUS_PASSWORD
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

# Release Android SDKs WSO2 nexus repository
gradle_publish_release_to_wso2_nexus() {
  echo 
  ./gradlew publishToWso2Nexus -Dorg.gradle.internal.publish.checksums.insecure=true
  ./gradlew findWso2NexusStagingRepository closeWso2NexusStagingRepository -Dorg.gradle.internal.publish.checksums.insecure=true
  ./gradlew findWso2NexusStagingRepository releaseWso2NexusStagingRepository -Dorg.gradle.internal.publish.checksums.insecure=true
}

# Function to generate API docs
generate_api_docs() {
  echo
  ./gradlew dokkaHtmlMultiModule
}

# run gradle tasks to release Android SDKs to WSO2 nexus repository
release_android_sdks() {
    # Go to android sdk directory
    go_to_android_sdk_dir

    # Functions to release Android SDKs
    regenerate_gradle_wrapper
    check_gradle_wrapper
    gradle_build
    #gradle_assemble
    #gradle_publish_release_to_wso2_nexus
    generate_api_docs

    # Go to scripts directory
    go_to_scripts_dir
}

# Function to update snapshot version
update_snapshot_version() {
  echo 
  bash ./update_snapshot_version.sh
}

# Function to commit and push
commit_and_push() {
  echo 

  # Go to common scripts directory
  go_to_common_scripts_dir

  bash ./commit_and_push.sh $GITHUB_RUN_NUMBER $RELEASE_BRANCH "Bump versions of Mobile-SKDs Android SDKs"
}

# Function to create GitHub release
create_github_release() {
  echo 
  
  # Go to common scripts directory
  go_to_common_scripts_dir

  # Create the release tag
  local release_tag="android-v$MAIN_VERSION"
  # Get the current date in YYYY-MM-DD format
  local release_date=$(date +'%Y-%m-%d')
  # Create the release body
  local release_body="Released on: $release_date\n\nReleased Versions:\nandroid: $MAIN_VERSION\nandroid-core: $CORE_VERSION"

  bash ./create_github_release.sh $GH_TOKEN $release_tag $GITHUB_REPOSITORY $MAIN_VERSION $CORE_VERSION
}

# Call the functions in sequence
update_versions
update_nexus_credentials
release_android_sdks
update_snapshot_version
commit_and_push
create_github_release
