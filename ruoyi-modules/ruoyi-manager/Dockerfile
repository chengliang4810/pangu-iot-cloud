FROM findepi/graalvm:java17-native

MAINTAINER chengliang4810

RUN mkdir -p /ruoyi/manager/logs \
    /ruoyi/manager/temp \
    /ruoyi/skywalking/agent

WORKDIR /ruoyi/manager

ENV SERVER_PORT=9301

EXPOSE ${SERVER_PORT}

ADD ./target/ruoyi-manager.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=ruoyi-manager", \
#            "-javaagent:/ruoyi/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
