FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

RUN mkdir -p /pangu/gateway/logs  \
    /pangu/gateway/temp  \
    /pangu/skywalking/agent

WORKDIR /pangu/gateway

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-gateway.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-gateway", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
