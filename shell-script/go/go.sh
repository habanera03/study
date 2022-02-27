#!/bin/bash

DIR_FILE_NAME="directory.txt"
CURR_DIR="$(cd "$(dirname -- "$0")" && pwd)/go"
IFS=$'\n' DIRS=($(<$CURR_DIR/$DIR_FILE_NAME))
SELECTED_DIR=""
USAGE="
go
=== SYNOPSIS ===
go [NUMBER]
"

function show_dir() {
    echo -e "$USAGE"
    echo -e "\033[36m[ 이동 가능한 경로 ]\033[0m"

    for ((i=0;i<${#DIRS[*]};i++))
    do
        echo [$(($i+1))] ${DIRS[$i]}
    done 
    echo ""
}

function goto_dir() {
    echo -e "\033[36m$SELECTED_DIR\033[0m 이동"

    cd "$SELECTED_DIR"
}

(( $# == 0 )) && show_dir
(( $# == 1 )) && [[ $1 =~ [[:digit:]]  && $1 -gt 0 ]] && SELECTED_DIR="${DIRS[$(($1-1))]}" && goto_dir
