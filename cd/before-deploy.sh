#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_0ad6f3a71d9e_key -iv $encrypted_0ad6f3a71d9e_iv -in codesigning.asc.enc -out codesigning.asc -d
fi