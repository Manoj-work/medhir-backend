# Use an official OpenJDK runtime as the base image
FROM openjdk:23-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/grpcdemo-0.0.1-SNAPSHOT.jar app.jar

# Expose the gRPC port (default gRPC port is 9090)
EXPOSE 9090

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]