FROM java:8u111-jre-alpine

MAINTAINER chengliang4810

# 设定时区
ENV TZ=Asia/Shanghai


RUN mkdir -p /pangu/iot/driver/modbus-tcp/logs \
    /pangu/iot/driver/modbus-tcp/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/iot/driver/modbus-tcp

ENV SERVER_PORT=7701

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-iot-modbus-tcp-driver.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-iot/manager", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
