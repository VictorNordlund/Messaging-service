###Build stage
FROM maven:3.8.5-openjdk-17 as build
COPY src src
COPY pom.xml pom.xml
RUN mvn -f pom.xml clean install


###Run stage
FROM openjdk:17-alpine

COPY --from=build target/MessagingService-1.0-SNAPSHOT.jar MessagingService-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar","MessagingService-1.0-SNAPSHOT.jar"]
EXPOSE 8080