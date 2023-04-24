FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

# 设定时区
ENV TZ=Asia/Shanghai


RUN mkdir -p /pangu/auth/logs  \
    /pangu/auth/temp  \
    /pangu/skywalking/agent

WORKDIR /pangu/auth

ENV SERVER_PORT=9210

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-auth.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-auth", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
