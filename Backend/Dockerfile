FROM maven AS builder
COPY . /backend
WORKDIR /backend
RUN mvn clean install -DskipTests

FROM openjdk:17
COPY --from=builder /backend/target/*.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]
