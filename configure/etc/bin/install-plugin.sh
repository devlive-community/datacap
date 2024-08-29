#!/bin/sh0

HOME=$(pwd)
VERSION=2024.3.10-SNAPSHOT

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

job_install_notify() {
  printf "========== Job install notify start ========== \n"
  common_install_handler "notify"
  printf "========== Job install notify end ========== \n"
}

job_install_scheduler() {
  printf "========== Job install scheduler start ========== \n"
  common_install_handler "scheduler"
  printf "========== Job install scheduler end ========== \n"
}

job_install_parser() {
  printf "========== Job install parser start ========== \n"
  common_install_handler "parser"
  printf "========== Job install parser end ========== \n"
}

job_install_fs() {
  printf "========== Job install fs start ========== \n"
  common_install_handler "fs"
  printf "========== Job install fs end ========== \n"
}

job_install_convert() {
  printf "========== Job install convert start ========== \n"
  common_install_handler "convert"
  printf "========== Job install convert end ========== \n"
}

job_install_executor() {
  printf "========== Job install executor start ========== \n"
  common_install_handler "executor"
  printf "========== Job install executor end ========== \n"
}

job_install_plugin
job_install_notify
job_install_scheduler
job_install_parser
job_install_fs
job_install_convert
job_install_executor
