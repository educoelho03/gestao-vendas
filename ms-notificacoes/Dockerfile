FROM maven:3.9.11-jdk-8 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM openjdk:8-jre

WORKDIR /app
COPY --from=build /app/target/ms-notificacoes-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
