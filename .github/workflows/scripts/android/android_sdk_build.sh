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

# Go to android sdk directory
go_to_android_sdk_dir() {
  cd ../../../../android
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

# run gradle tasks to build Android SDKs
build_android_sdks() {
    # Go to android sdk directory
    go_to_android_sdk_dir

    # Functions to release Android SDKs
    regenerate_gradle_wrapper
    check_gradle_wrapper
    gradle_build
}

# Call the functions in sequence
build_android_sdks
