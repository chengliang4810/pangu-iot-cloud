FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

RUN mkdir -p /pangu/xxl-job-admin/logs \
    /pangu/skywalking/agent

WORKDIR /pangu/xxl-job-admin

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 9900

ADD ./target/pangu-xxl-job-admin.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
#            "-Dskywalking.agent.service_name=pangu-xxl-job-admin", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
