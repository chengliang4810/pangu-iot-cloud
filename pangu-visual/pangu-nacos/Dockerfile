FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER chengliang4810

RUN mkdir -p /pangu/nacos

WORKDIR /pangu/nacos

EXPOSE 8848

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms256m -Xmx512m"

ADD ./target/pangu-nacos.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar", "${JAVA_OPTS}"]

