FROM java:8u111-jre-alpine

MAINTAINER chengliang4810
# 设定时区
ENV TZ=Asia/Shanghai


RUN mkdir -p /pangu/iot/driver/etone/logs \
    /pangu/iot/driver/etone/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/iot/driver/etone

ENV UDP_PORT=6271
ENV SERVER_PORT=7777

EXPOSE ${UDP_PORT}/udp
EXPOSE ${SERVER_PORT}

ADD ./target/pangu-iot-driver-etone-ap.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dudp.port=${UDP_PORT}", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-iot/manager", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
