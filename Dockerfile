# ===== Build Stage =====
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Maven wrapper & config
COPY library/mvnw .
COPY library/.mvn .mvn

# Copy pom.xml
COPY library/pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY library/src src

# Build the application
RUN ./mvnw package -DskipTests


# ===== Run Stage =====
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
