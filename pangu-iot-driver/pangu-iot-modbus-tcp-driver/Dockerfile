FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

RUN mkdir -p /pangu/driver/logs  \
    /pangu/driver/temp  \
    /pangu/skywalking/agent

WORKDIR /pangu/auth

ENV SERVER_PORT=7701

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-iot-modbus-tcp-driver.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-auth", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
