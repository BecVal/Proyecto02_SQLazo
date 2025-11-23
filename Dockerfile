FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=build /app/target/proyecto02-jar-with-dependencies.jar app.jar
COPY data ./data

CMD ["java", "-jar", "app.jar"]
