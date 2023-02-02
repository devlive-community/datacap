#!/bin/sh
VERSION=$1
HOME=$(pwd)

job_before_echo_basic() {
    printf "\n\tJob before echo basic \n"
    printf "============================================\n"
    printf "Released new version\n"
    printf "============================================\n\n"
}

job_before_checked() {
    printf "\n\tJob before check parameter \n"
    printf "============================================\n"
    if test -z "$VERSION"; then
        printf "Please input version \n"
        exit 1
    else
        printf "Version checked successful         | %s\n" "$VERSION"
    fi
    printf "============================================\n\n"
}

job_runner_apply() {
    printf "\n\tJob runner apply \n"
    printf "============================================\n"
    printf "Apply new version for server ...\n"
    MAVEN_OPTS=-Dorg.slf4j.simpleLogger.defaultLogLevel=error ./mvnw versions:set -DnewVersion="$VERSION"
    if [ $? -ne 0 ]; then
        printf "\nApply new version for server failed\n\n"
        exit 1
    else
        printf "\nApply new version for server successful\n\n"
    fi

    printf "Apply new version for web ...\n"
    # shellcheck disable=SC2164
    cd "$HOME"/web/console-fe
    npm version "$VERSION" --no-git-tag-version
    if [ $? -ne 0 ]; then
        printf "\nApply new version for web failed\n\n"
        exit 1
    else
        printf "\nApply new version for web successful\n\n"
    fi
    cd "$HOME"
    printf "============================================\n\n"
}

job_before_echo_basic
job_before_checked
job_runner_apply
