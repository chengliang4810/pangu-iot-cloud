FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /pangu/job/logs \
    /pangu/job/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/job

ENV SERVER_PORT=9203

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-job.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-job", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
