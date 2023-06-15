FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /pangu/monitor/logs

WORKDIR /pangu/monitor

EXPOSE 9100

ADD ./target/pangu-monitor.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
