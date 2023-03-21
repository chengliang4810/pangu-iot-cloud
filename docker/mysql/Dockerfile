
FROM mysql:latest

MAINTAINER chengliang4810 <chengliang4810@163.com>

ENV MYSQL_ROOT_PASSWORD 123456

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

COPY ./iot-dc3.sql /docker-entrypoint-initdb.d
