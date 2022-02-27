#!/bin/bash

NEW_VERSION=2.17.0
THIS_DIR=`pwd`

alias ll='ls -alF'

function check_log4j () {
   cd /

   FILE_ARRAY=($(find ./ -name "*log4j*" | grep WEB-INF/lib/temp | grep "\-2." | grep -v "\-${NEW_VERSION}"))
   FILE_ARRAY_LEN=${#FILE_ARRAY[@]}

   echo "Total ${FILE_ARRAY_LEN}"
   for i in ${FILE_ARRAY[@]}
   do
      echo "   FILE : ${i}"
   done
}

function delete_prev_log4j () {
   for i in ${FILE_ARRAY[@]}
   do
      echo "delete ${i}"
      rm -f ${i}
   done
}

check_log4j
delete_prev_log4j
check_log4j
