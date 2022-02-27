# find_change.sh 가이드

## 1. 사용법 출력

- find_change.sh 사용법 출력
- ex) $ ./find_change.sh -h

## 2. 타겟 파일 확인

- 입력받은 타겟을 대상으로 ROOT로 선언한 디렉터리부터 타겟 파일 확인 및 변경 대상 확인
- ex) $ ./find_change.sh -l log4j

## 3. 변경

- change_lib 디렉터리에 있는 파일과 버전을 제외한 파일명이 동일한 타겟 파일을 temp 디렉터리에 백업하고, change_lib 디렉터리의 파일로 변경
- ex) $ ./find_change.sh -c log4j

## 4. 삭제

- 백업 파일 및 temp 디렉터리 삭제
- ex) $ ./find_change.sh -d log4j

## 5. 원복

- 변경한 파일 삭제 후 temp 디렉터리의 백업 파일로 원복
- ex) $ ./find_change.sh -r log4j