FROM openjdk:17-alpine

MAINTAINER 2023_3SOAT-Dev_Rise_G8

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]