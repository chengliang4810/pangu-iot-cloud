FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

RUN mkdir -p /pangu/data/logs \
    /pangu/data/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/data

ENV SERVER_PORT=9302

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-data.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-system", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
