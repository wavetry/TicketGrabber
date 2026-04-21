#!/usr/bin/env bash
set -e

# Clean the project
echo "Cleaning project..."
./gradlew clean

# Build the project
echo "Building project..."
./gradlew build

echo "Build completed successfully!"
