# =========================
# Stage 1 - Build Stage
# =========================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package

# =========================
# Stage 2 - Runtime Stage
# =========================
FROM eclipse-temurin:17

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]