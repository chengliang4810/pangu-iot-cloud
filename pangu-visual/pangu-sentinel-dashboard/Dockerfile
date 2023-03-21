FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

RUN mkdir -p /pangu/sentinel-dashboard/logs \
    /pangu/skywalking/agent

WORKDIR /pangu/sentinel-dashboard

EXPOSE 8718

ADD ./target/pangu-sentinel-dashboard.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
#            "-Dskywalking.agent.service_name=pangu-sentinel-dashboard", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
