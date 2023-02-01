#!/bin/sh

HOME=$(pwd)
JAVA_HOME=${JAVA_HOME:-/opt/jdk}
APPLICATION_NAME='io.edurt.datacap.server.DataCap'
APPLICATION_PID=

job_before_echo_basic() {
    printf "\n\tJob before echo basic \n"
    printf "============================================\n"
    printf "Runtime home                           | %s\n" "$HOME"
    printf "Runtime java home                      | %s\n" "$JAVA_HOME"
    printf "Runtime application name               | %s\n" "$APPLICATION_NAME"
    printf "============================================\n\n"
}

job_before_apply_server() {
    APPLICATION_PID=$(pgrep -f "$APPLICATION_NAME" | awk '{print $1}')
}

job_runner_checker_server() {
    printf "\n\tJob runner check server \n"
    printf "============================================\n"
    job_before_apply_server
    printf "Runtime process                        | %s\n" "$APPLICATION_PID"
    if test -z "$APPLICATION_PID"; then
        printf "Server status                          | %s\n" "stopped"
        printf "============================================\n\n"
    else
        printf "Server status                          | %s\n" "running"
        printf "============================================\n\n"
        exit
    fi
}

job_runner_debug_server() {
    printf "\n\tJob runner server \n"
    printf "============================================\n"
    printf "Server starting                        | %s\n" "$APPLICATION_NAME"
    cd "$HOME"
    "$JAVA_HOME"/bin/java -classpath 'lib/*' "$APPLICATION_NAME" \
        --spring.config.location="$HOME/configure/" \
        --plugin-dir="$HOME/plugins/"
}

job_before_echo_basic
# shellcheck disable=SC2119
job_runner_checker_server
job_runner_debug_server
