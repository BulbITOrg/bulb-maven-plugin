#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
	pwd
    openssl aes-256-cbc -K $encrypted_0ad6f3a71d9e_key -iv $encrypted_0ad6f3a71d9e_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
fi