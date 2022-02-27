#!/bin/bash

NEW_VERSION=2.17.0
THIS_DIR=`pwd`

alias ll='ls -alF'

function check_log4j () {
   cd /

   FILE_ARRAY=($(find ./ -name "*log4j*" | grep WEB-INF/lib | grep -v temp | grep "\-2." | grep -v "\-${NEW_VERSION}"))
   FILE_ARRAY_LEN=${#FILE_ARRAY[@]}

   echo "Total ${FILE_ARRAY_LEN}"
   for i in ${FILE_ARRAY[@]}
   do
      echo "   FILE : ${i}"
   done
}

function check_moved_log4j () {
   cd /
   MOVED=($(find ./ -name "*log4j*" | grep WEB-INF/lib/temp | grep "\-2." | grep -v "\-${NEW_VERSION}"))
   CHANGED=($(find ./ -name "*log4j*" | grep WEB-INF/lib | grep -v temp | grep "\-${NEW_VERSION}"))
   echo "Move total ${#MOVED[@]}"
   for i in ${MOVED[@]}
   do
      echo "   MOVED FILE : ${i}"
   done
   echo "Change total ${#CHANGED[@]}"
   for i in ${CHANGED[@]}
   do
      echo "   CHANGED FILE : ${i}"
   done
}

function move_prev_log4j () {
   CURR_DIR="none"
   for i in ${FILE_ARRAY[@]}
   do
      TEMP_DIR=`dirname ${i}` 
      if [ ${CURR_DIR} != ${TEMP_DIR} ]; then
         CURR_DIR=$(dirname ${i})
      fi
      if [ ! -d $CURR_DIR/temp ]; then
         mkdir $CURR_DIR/temp
      fi
      echo "prev file(${i}) move to ${CURR_DIR}/temp"
      mv ${i} $CURR_DIR/temp
      echo "new file move to ${CURR_DIR}"
      cp -rpf ${THIS_DIR}/log4j-api-${NEW_VERSION}.jar $CURR_DIR
      cp -rpf ${THIS_DIR}/log4j-core-${NEW_VERSION}.jar $CURR_DIR
      cp -rpf ${THIS_DIR}/log4j-slf4j-impl-${NEW_VERSION}.jar $CURR_DIR
      cp -rpf ${THIS_DIR}/log4j-web-${NEW_VERSION}.jar $CURR_DIR
   done
}

function rollback_prev_log4j () {
   cd /
   MOVED_FILE_ARRAY=($(find ./ -name "*log4j*" | grep WEB-INF/lib/temp | grep "\-2." | grep -v "\-${NEW_VERSION}"))
   echo "rollback file count : ${#MOVED_FILE_ARRAY[@]}"
   CURR_DIR="none"
   for i in ${MOVED_FILE_ARRAY[@]}
   do
      TEMP_DIR=`dirname ${i}` 
      if [ ${CURR_DIR} != ${TEMP_DIR} ]; then
         CURR_DIR=$(dirname ${i})
      fi
      echo "rollback to ${CURR_DIR:0:${#CURR_DIR}-4} ${i}"
      mv ${i} ${CURR_DIR:0:${#CURR_DIR}-4}
   done
}

check_log4j
#move_prev_log4j
check_moved_log4j

#rollback_prev_log4j