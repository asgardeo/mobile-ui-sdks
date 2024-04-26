#!/bin/bash

# Retrieve version from environment variable (assuming it's set by the workflow)
NEW_VERSION=$1  # This assumes the version is passed as the first argument

IFS="." read -r major minor patch <<< "${NEW_VERSION}"

# Function to go to android sdk directory
go_to_android_sdk_dir() {
  cd ../../../../android
}

# Go to android sdk directory
go_to_android_sdk_dir

# Increment patch version
patch=$((patch + 1))

# Update version with incremented patch and -SNAPSHOT suffix
new_version_with_snapshot="$major.$minor.$patch-SNAPSHOT"

echo "Updating version to: $new_version_with_snapshot"

# Update gradle.properties (assuming the working directory is set elsewhere)
sed -i "s/^$VERSION_NAME=.*/$VERSION_NAME=$new_version_with_snapshot/" ./gradle.properties

# Set output variable for next steps
echo "UPDATED_VERSION=$new_version_with_snapshot"
