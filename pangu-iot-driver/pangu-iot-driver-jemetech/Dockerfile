FROM java:8u111-jre-alpine

MAINTAINER chengliang4810
# 设定时区
ENV TZ=Asia/Shanghai


RUN mkdir -p /pangu/iot/driver/jemetech/logs \
    /pangu/iot/driver/jemetech/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/iot/driver/jemetech

ENV SERVER_PORT=7702

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-iot-driver-jemetech.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-iot/manager", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
