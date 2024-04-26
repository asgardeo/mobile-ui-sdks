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

VERSION_TYPE=$1

# Function to go to android sdk directory
go_to_android_sdk_dir() {
  cd ../../../../android
}

# Go to scripts directory
go_to_scripts_dir() {
  cd ../.github/workflows/scripts/android
}

# Function to bump version based on type
bump_version() {
  local current_version=$1
  local bump_type=$2
  IFS="." read -r major minor patch <<< "$current_version"

  if [ "$bump_type" == "major" ]; then
    major=$((major + 1))
    minor=0
    patch=0
  elif [ "$bump_type" == "minor" ]; then
    minor=$((minor + 1))
    patch=0
  elif [ "$bump_type" == "patch" ]; then
    patch=$((patch))
  else
    echo "Invalid bump type: $bump_type"
    exit 1
  fi

  echo "$major.$minor.$patch"
}

go_to_android_sdk_dir
 
# Loop through all version properties in gradle.properties
  while IFS= read -r line; do
    # Extract version name and current version
    version_name=${line%%=*}
    current_version=${line#*=}

    # Skip lines that are not version properties or don't end with -SNAPSHOT
    if [[ ! "$version_name" =~ _VERSION$ || ! "$current_version" =~ -SNAPSHOT ]]; then
      continue
    fi

    # Remove -SNAPSHOT suffix
    stripped_version=${current_version%-SNAPSHOT}

    # Bump version based on type
    new_version=$(bump_version "$stripped_version" "$VERSION_TYPE")

    # Update version in gradle.properties
    sed -i "s/$version_name=.*/$version_name=$new_version/" ./gradle.properties
    echo "Updated $version_name to: $new_version"
  done < gradle.properties

go_to_scripts_dir
