# ===============================
# Stage 1: Build (Java 21)
# ===============================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy project files
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# ===============================
# Stage 2: Runtime (Java 21)
# ===============================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
