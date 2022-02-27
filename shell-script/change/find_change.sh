#!/bin/bash

ROOT="/home"
CHANGE_DIR_NAME="change_lib"
TEMP_DIR_NAME="temp"
EXTENTION=".jar"
CHANGE_DIR_PATH=`pwd -P`"/$CHANGE_DIR_NAME"
TARGET_NAME=""
TARGET=""

TARGET_NAME_DIRS=""
COUNT=0
OLD_FILES=()

USAGE="
find_change.sh
=== SYNOPSIS ===
find_change.sh [OPTION] [TARGET]
=== OPTION ===
[-h] : help
[-l] : founded list check
[-c] : change
[-r] : rollback
[-d] : backup directory delete
"

function find_target() {
    echo "$TARGET_NAME*$EXTENTION"

    cd $ROOT

    TARGET_NAME_DIRS=`find ./ -type f -name "$TARGET" | grep -v $CHANGE_DIR_NAME | grep -v $TEMP_DIR_NAME | sed "s/$TARGET_NAME.*                                                                                                                                          $EXTENTION//" | uniq`
    if [ ${#TARGET_NAME_DIRS[0]} -gt 0 ]; then
        for i in ${TARGET_NAME_DIRS[@]}
        do
            echo "DIR : ${i}"
            TEMP=`find ${i} -type f -name "$TARGET" | rev | cut -d '/' -f 1 | rev`
            for j in ${TEMP[@]}
            do
                FILE_NAME=`echo ${j} | sed 's/\-[0-9].*.jar//'`
                if [ $(find $CHANGE_DIR_PATH -type f -name $FILE_NAME*$EXTENTION | wc -l) -gt 0 ]; then
                    echo -en "\033[36m[변경 대상]\033[0m "
                    OLD_FILES+=(${i}${j})
                    ((COUNT++))
                fi
                echo "FILE_NAME : $FILE_NAME -> ${j}"
            done
            echo "=========="
        done
    else
        echo -e "\033[36m"$ROOT"("$TEMP_DIR_NAME" 제외)\033[0m부터 조회한 결과 \033[36m"$TARGET"\033[0m을 찾을 수 없습니다."
    fi
}

function change_target() {
    if [ $COUNT -gt 0 ]; then
        while true
        do
            echo -n "변경 대상 파일이 $COUNT건 있습니다. 변경하시겠습니까? "
            read input

            if [[ -z "${input//[n,y]/}" && ${input} == 'y' ]]; then
                echo
                move_old_files
                break
            elif [[ -z "${input//[n,y]/}" && ${input} == 'n' ]]; then
                echo "변경을 취소합니다."
                break
            else
                echo -e "\033[41m잘못된 입력입니다. 입력 가능 값[n, y]\033[0m"
                sleep 1
            fi
         done
    else
        echo "변경 대상이 없습니다."
    fi
}

function move_old_files() {
    for i in ${OLD_FILES[@]}
    do
        TARGET_DIR=`echo ${i} | sed "s/$TARGET_NAME.*$EXTENTION//"`
        TARGET_TEMP_DIR=$TARGET_DIR$TEMP_DIR_NAME
        CHANGE_FILE=`find $CHANGE_DIR_PATH -type f -name "$(echo ${i} | rev | cut -d '/' -f 1 | rev | sed 's/\-[0-9].*.jar//')*"                                                                                                                                           | rev | cut -d '/' -f 1 | rev`
        if [ ! -d $TARGET_TEMP_DIR ] ; then
            mkdir $TARGET_TEMP_DIR
        fi
        mv ${i} $TARGET_TEMP_DIR
        echo -e "\033[32m${i} -> $TARGET_TEMP_DIR\033[0m으로 이동 완료"

        cp -rp $CHANGE_DIR_PATH/$CHANGE_FILE $TARGET_DIR
        echo -e "\033[36m$CHANGE_DIR_PATh/$CHANGE_FILE -> $TARGET_DIR\033[0m으로 복사 완료"
        echo "=========="
    done
}

function rollback_target() {
    while true
    do
        echo -n "기존 파일로 원복하시겠습니까? "
        read input

        if [[ -z "${input//[n,y]/}" && ${input} == 'y' ]]; then
            ROLLBACK_DIRS=`find ./ -type f -name "$TARGET" | grep -v $CHANGE_DIR_NAME | grep $TEMP_DIR_NAME | sed "s/$TARGET_NAME                                                                                                                                          .*$EXTENTION//" | uniq`
            if [ ${#ROLLBACK_DIRS[0]} -gt 0 ]; then
                echo
                for i in ${ROLLBACK_DIRS[@]}
                do
                   echo "원복할 파일이 있는 디렉터리 : ${i}"
                   for j in $(find ${i} -type f -name "$TARGET" | rev | cut -d '/' -f 1 | rev)
                   do
                       LIB_DIR=`echo ${i} | sed "s/\/$TEMP_DIR_NAME//"`
                       CHANGED_FILE=`find $LIB_DIR -type f -name "$(echo ${j} | sed 's/\-[0-9].*.jar//')*" | grep ".jar$" | grep                                                                                                                                           -v $TEMP_DIR_NAME | rev | cut -d '/' -f 1 | rev`
                       echo "삭제할 변경된 파일 : $LIB_DIR$CHANGED_FILE"

                       if [[ ${#CHANGED_FILE} -gt 0 && $(find $CHANGE_DIR_PATH -type f -name $CHANGED_FILE | wc -l) -gt 0 ]]; the                                                                                                                                          n
                           rm -rf $LIB_DIR$CHANGED_FILE
                           echo -e "\033[31m$LIB_DIR$CHANGED_FILE\033[0m 제거 완료"
                       fi
                       mv ${i}${j}* $LIB_DIR
                       echo -e "\033[36m${i}${j} -> $LIB_DIR\033[0m 원복 완료"
                   done
                   rm -rf ${i}
                   echo "=========="
                done
            else
                echo "원복할 대상이 없습니다."
            fi
        break
        elif [[ -z "${input//[n,y]/}" && ${input} == 'n' ]]; then
            echo "원복을 취소합니다."
        break
        else
            echo -e "\033[41m잘못된 입력입니다. 입력 가능 값[n, y]\033[0m"
            sleep 1
        fi
     done
}

function delete_temp() {
    while true
    do
        echo -n "백업 디렉터리를 삭제하시겠습니까? "
        read input

        if [[ -z "${input//[n,y]/}" && ${input} == 'y' ]]; then
            TEMP_DIRS=`find ./ -type f -name "$TARGET" | grep -v $CHANGE_DIR_NAME | grep $TEMP_DIR_NAME | sed "s/$TARGET_NAME.*$E                                                                                                                                          XTENTION//" | uniq`
            if [ ${#TEMP_DIRS[0]} -gt 0 ]; then
                echo
                for i in ${TEMP_DIRS[@]}
                do
                   rm -rf ${i}
                   echo -e "백업 디렉터리 \033[36m${i}\033[0m 삭제"
                   echo "=========="
                done
            else
                echo "백업 디렉터리가 없습니다."
            fi
        break
        elif [[ -z "${input//[n,y]/}" && ${input} == 'n' ]]; then
            echo "백업 디렉터리 삭제를 취소합니다."
        break
        else
            echo -e "\033[41m잘못된 입력입니다. 입력 가능 값[n, y]\033[0m"
            sleep 1
        fi
     done

}

(( $# == 0 )) && echo -e "$USAGE" && exit 1
(( $# == 1 )) && [ "$1" != '-h' ] && echo -e "\033[41mTARGET을 입력해주세요.\033[0m" && echo -e "$USAGE" && exit 1
set -- $(getopt hlcrd $*)
for i in $*
do
        case $i in
        -h) echo -e "$USAGE" && exit 0 ;;
        -l) shift 2
            TARGET_NAME=$1 && TARGET=$TARGET_NAME*$EXTENTION && find_target && exit 0
            ;;
        -c) shift 2
            TARGET_NAME=$1 && TARGET=$TARGET_NAME*$EXTENTION && find_target && change_target && exit 0
            ;;
        -r) shift 2
            TARGET_NAME=$1 && TARGET=$TARGET_NAME*$EXTENTION && rollback_target && exit 0
            ;;
        -d) shift 2
            TARGET_NAME=$1 && TARGET=$TARGET_NAME*$EXTENTION && delete_temp && exit 0
            ;;
        esac
done
