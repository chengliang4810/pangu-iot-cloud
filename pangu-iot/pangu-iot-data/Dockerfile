FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

RUN mkdir -p /pangu/iot/manager/logs \
    /pangu/iot/manager/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/iot/manager

ENV SERVER_PORT=9201

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-iot-manager.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-iot/manager", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
