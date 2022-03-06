#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=taglog

echo "> Build 파일복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl taglog.*.jar | grep jar | awk '{print $1}')

echo "현재 구동중인 애플리케이션 Pid : $CURRENT_PID"

if [ -z "$CURRENT_PID"]; then
        echo "> 구동중 어플리캐이션 없음"
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR NAME : $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"

cd $REPOSITORY/

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
    -Dspring.config.location=classpath:/application-real.yml,/home/ec2-user/app/application-real-db.yml \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &