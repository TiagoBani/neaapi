################################
# Maven builder
################################
FROM maven:3-adoptopenjdk-11 as builder

USER root

ENV APP_DIR /app
WORKDIR $APP_DIR

COPY pom.xml $APP_DIR/

COPY . $APP_DIR

RUN mvn clean install -DskipTests

###############################
# App
###############################
FROM openjdk:17-alpine3.14

ARG SERVER_PORT=8080

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

RUN apk update && rm -rf /var/cache/apk/*

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
