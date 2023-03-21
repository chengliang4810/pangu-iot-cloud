FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

RUN mkdir -p /pangu/demo/logs  \
    /pangu/demo/temp  \
    /pangu/skywalking/agent

WORKDIR /pangu/demo

ENV SERVER_PORT=9401

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-demo.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-demo", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
