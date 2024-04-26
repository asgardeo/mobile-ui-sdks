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

NEXUS_USERNAME=$1
NEXUS_PASSWORD=$2

# Function to go to android sdk directory
go_to_android_sdk_dir() {
  cd ../../../../android
}

# Go to scripts directory
go_to_scripts_dir() {
  cd ../.github/workflows/scripts/android
}

# Function to check if local.properties exists
check_local_properties_file() {
  if [ ! -f "local.properties" ]; then
    echo "local.properties not found. Creating a new one."
    touch local.properties
  fi
}

# Go to android sdk directory
go_to_android_sdk_dir

# Check if local.properties exists in the android sdk directory
check_local_properties_file

# Update NEXUS_USERNAME property
sed -i "s/^NEXUS_USERNAME=.*/NEXUS_USERNAME=$NEXUS_USERNAME/" local.properties

# Update NEXUS_PASSWORD property
sed -i "s/^NEXUS_PASSWORD=.*/NEXUS_PASSWORD=$NEXUS_PASSWORD/" local.properties

echo "Updated local.properties with NEXUS credentials."

# Go to scripts directory
go_to_scripts_dir
