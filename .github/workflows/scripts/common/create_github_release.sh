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

GH_TOKEN=$1 # Get the release tag and body
RELEASE_TAG=$2
GITHUB_REPOSITORY=$3
MAIN_VERSION=$4
CORE_VERSION=$5
MASTER_BRANCH="release-test1"

# Go to root directory
go_to_root_dir() {
  cd ../../../../
}

# Go to root directory
go_to_root_dir

RELEASE_NAME=$RELEASE_TAG

# Create a new tag
git tag ${RELEASE_TAG}

echo "Tag created: ${RELEASE_TAG}"

# Push the new tag
git push origin ${RELEASE_TAG}

echo "Tag pushed: ${RELEASE_TAG}"

echo "Creating release for the repository: $GITHUB_REPOSITORY"

local release_date=$(date +'%Y-%m-%d')

# Create a new release
CREATE_RELEASE_RESPONSE=$(curl --fail --location --request POST "https://api.github.com/repos/$GITHUB_REPOSITORY/releases" \
        --header "Authorization: Bearer $GH_TOKEN" \
        --header "Accept: application/vnd.github+json" \
        --header "X-GitHub-Api-Version: 2022-11-28" \
        --data '{
        "tag_name": "'"$RELEASE_TAG"'",
        "target_commitish": "'"$MASTER_BRANCH"'",
        "name": "'"$RELEASE_NAME"'",
        "body": "Released on: '"$release_date"'\n\nReleased Versions:\nandroid: '"$MAIN_VERSION"'\nandroid-core: '"$CORE_VERSION"'"
        }')

RELEASE_ID=$(echo $CREATE_RELEASE_RESPONSE | jq -r '.id')
echo "Created GitHub release for Hotfix $RELEASE_TAG with release_id $RELEASE_ID"
