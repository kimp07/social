FROM openjdk:8-jdk-alpine
MAINTAINER "alex.com"
VOLUME /tmp
EXPOSE 8080
COPY build/libs/social-0.0.1-SNAPSHOT.jar social-docker-0.0.1.jar
ENTRYPOINT ["java","-jar","social-docker-0.0.1.jar"]