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
RELEASE_BODY=$3

# Go to root directory
go_to_root_dir() {
    cd ../../../../
}

# Go to root directory
go_to_root_dir

# Create a release
RELEASE_NAME=$RELEASE_TAG
RELEASE_URL=$(curl -s -X POST \
    -H "Accept: application/vnd.github+json" \
    -H "Authorization: token $GH_TOKEN" \
    -H "X-GitHub-Api-Version: 2022-11-28" \
    -d "{\"tag_name\": \"$RELEASE_TAG\", \"name\": \"$RELEASE_NAME\", \"body\": \"$RELEASE_BODY\", \"draft\": false, \"prerelease\": false}" \
    "https://api.github.com/repos/asgardeo/mobile-ui-sdks/releases" \
    | jq -r '.html_url')

echo "Release created: $RELEASE_URL"