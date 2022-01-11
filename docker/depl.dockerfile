ARG VERSION="0.0.1-SNAPSHOT"

FROM maven:3-openjdk-11-slim as dependencies

WORKDIR /usr/share/app
COPY pom.xml .
RUN mvn clean package

FROM dependencies as builder

WORKDIR /usr/share/app
COPY pom.xml .
COPY src src
RUN mvn clean package -Dmaven.test.skip=true


FROM openjdk:11-jre-slim-buster

ARG VERSION
ENV JAR="iudx.gis.interface-cluster-0.0.1-SNAPSHOT-fat.jar"

WORKDIR /usr/share/app
COPY --from=builder /target/${JAR} ./fatjar.jar
