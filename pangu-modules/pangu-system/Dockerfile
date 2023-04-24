FROM java:8u111-jre-alpine

MAINTAINER chengliang4810

# 设定时区
ENV TZ=Asia/Shanghai

RUN mkdir -p /pangu/system/logs \
    /pangu/system/temp \
    /pangu/skywalking/agent

WORKDIR /pangu/system

ENV SERVER_PORT=9201

EXPOSE ${SERVER_PORT}

ADD ./target/pangu-system.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=pangu-system", \
#            "-javaagent:/pangu/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
