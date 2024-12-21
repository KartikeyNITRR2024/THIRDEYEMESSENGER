# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 as build

# Copy the project files into the container
COPY . /app

# Set the working directory
WORKDIR /app

# Build the application, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17.0.1-jdk-slim

# Copy the JAR file from the build stage
COPY --from=build /app/target/THIRDEYEMESSENGER-0.0.1-SNAPSHOT.jar THIRDEYEMESSENGER.jar

# Expose the port on which the application will run
EXPOSE 8080

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "THIRDEYEMESSENGER.jar"]


