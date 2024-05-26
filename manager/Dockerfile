FROM ubuntu:latest AS build

RUN apt-get update && \
    apt-get install -y openjdk-21-jdk maven

COPY . .

RUN mvn clean install

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build /target/todo-3.3.1-SNAPSHOT.jar manager.jar

ENTRYPOINT ["java", "-jar", "manager.jar"]
