# FROM java:8u111-jre-alpine
FROM openjdk:8u332
MAINTAINER chengliang4810
# 设定时区
ENV TZ=Asia/Shanghai


RUN mkdir -p /pangu/iot/driver/hikvision/logs \
    /pangu/iot/driver/hikvision/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/iot/driver/hikvision

ENV SERVER_PORT=7823

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-iot-driver-hikvision-door.jar ./app.jar
ADD ./lib ./lib

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-iot/manager", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
