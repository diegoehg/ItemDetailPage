# Use Maven 3.9.11 with OpenJDK 21 as the base image
FROM maven:3.9.11-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /usr/src/mymaven

# Copy Maven configuration files
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Copy the built artifact from builder stage
COPY --from=build /usr/src/mymaven/target/online-store-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 9090

# Run the application
CMD ["java", "-jar", "app.jar"]