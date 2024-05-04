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

GITHUB_RUN_NUMBER=$1
RELEASE_BRANCH=$2
RELEASE_COMMIT_MESSAGE=$3
MASTER_BRANCH="release-test"

# Go to root directory
go_to_root_dir() {
  cd ../../../
}

# Create and checkout a new branch for the release.
create_and_checkout_release_branch() {
    git checkout -b "$RELEASE_BRANCH" &&
    echo "Created and checked out to the release branch: $RELEASE_BRANCH"
}

commit_and_push() {
    # Stage the gradle.properties file
    git add android/gradle.properties

    # Commit the changes
    git commit -m "$RELEASE_COMMIT_MESSAGE"

    # Push changes to the release branch
    git push origin "$RELEASE_BRANCH"
}

merge_to_master() {
    # Switch back to the master branch
    git checkout "$MASTER_BRANCH"

    # Pull the latest changes from the master branch
    git pull origin "$MASTER_BRANCH"

    # Merge release branch into master with a merge commit
    git merge --no-ff "$RELEASE_BRANCH" -m "[Mobile-SDKs Release] [GitHub Action #$GITHUB_RUN_NUMBER] [Release] [skip ci] Merge release branch $RELEASE_BRANCH"

    # Push the merge commit to master
    git push origin "$MASTER_BRANCH"

    echo "Merged $RELEASE_BRANCH into $MASTER_BRANCH"
}

delete_release_branch() {
    # Delete the local branch.
    git branch -d "$RELEASE_BRANCH" &&
    echo "Deleted local branch: $RELEASE_BRANCH"

    # Delete the remote branch.
    git push origin --delete "$RELEASE_BRANCH" &&
    echo "Deleted remote branch: $RELEASE_BRANCH"
}

go_to_root_dir

# Create and checkout a new branch for the release.
create_and_checkout_release_branch
# Commit and push changes to the release branch.
commit_and_push
# Merge the release branch into master.
merge_to_master
# Delete the release branch.
delete_release_branch
