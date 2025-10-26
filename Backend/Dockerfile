FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /backend
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:17
WORKDIR /app
EXPOSE 8080
COPY --from=builder /backend/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.profiles.active=prod"]
