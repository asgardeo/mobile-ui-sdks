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

# Function to go to android sdk directory
go_to_android_sdk_dir() {
  cd ../../../../android
}

# Go to scripts directory
go_to_scripts_dir() {
  cd ../.github/workflows/scripts/android
}

# Function to derive snapshot version from current version
derive_snapshot_version() {
    local current_version="$1"
    local major=$(echo "$current_version" | cut -d '.' -f 1)
    local minor=$(echo "$current_version" | cut -d '.' -f 2)
    local patch=$(echo "$current_version" | cut -d '.' -f 3 | cut -d '-' -f 1)
    local new_patch=$((patch + 1))
    echo "$major.$minor.$new_patch-SNAPSHOT"
}

# Go to android sdk directory
go_to_android_sdk_dir

# Read and update version properties in gradle.properties
# Assumes version properties end with -VERSION and are in the format NAME=VALUE
# Replace gradle.properties with the actual filename and path if it's located in a different directory
while IFS= read -r line; do
    # Extract version name and current version
    version_name=${line%%=*}
    current_version=${line#*=}

    # Skip lines that are not version properties or don't end with -SNAPSHOT
    if [[ ! "$version_name" =~ _VERSION$ ]]; then
      continue
    fi

     # Derive the new snapshot version
    updated_version=$(derive_snapshot_version "$current_version")

    # Update version in gradle.properties
    sed -i "s/$version_name=.*/$version_name=$updated_version/" ./gradle.properties
    echo "Updated the SNAPSHOT version of $version_name to: $updated_version"
  done < gradle.properties

echo "Updated all version properties to snapshot versions"

go_to_scripts_dir
