FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /pangu/resource/logs \
    /pangu/resource/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/resource

ENV SERVER_PORT=9204

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-resource.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-resource", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
