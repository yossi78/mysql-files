# Use the official OpenJDK base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the target directory into the container
COPY  users-managment-0.0.1-SNAPSHOT.jar  app.jar

# Expose port 8080 (or adjust the port if your application uses a different one)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]