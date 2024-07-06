#!/bin/sh0

HOME=$(pwd)
VERSION=2024.03.8-SNAPSHOT
#"$HOME/lib/*:$HOME/plugin/*:$HOME/notify/*:$HOME/scheduler/*:$HOME/parser/*:$HOME/fs/*:$HOME/convert/*:$HOME/executor/*"

common_install_handler() {
    TYPE=$1
    if [ ! -d "${HOME}/${TYPE}s" ];
      then
          mkdir "${HOME}/${TYPE}s"
          echo "Create ${TYPE}s directory"
    fi

    echo "Install datacap ${TYPE}, usage version is ${VERSION}"
    while read line; do
        full_line=$(echo "$line" | cut -c 1)
        if [ "$full_line" != "-" ] && [ "$full_line" != "#" ] && [ ! -z $full_line ]
        then
            echo "install connector : $line"
        		"${HOME}"/mvnw dependency:get -DgroupId=io.edurt.datacap -DartifactId="${line}" -Dversion=${VERSION} -Ddest="${HOME}/${TYPE}s"
        fi
    done <"${HOME}/configure/${TYPE}".conf
}

job_install_plugin() {
  printf "========== Job install plugin start ========== \n"
  common_install_handler "plugin"
  printf "========== Job install plugin end ========== \n"
}

job_install_plugin
