# Create a new image based on the builder image (slim base image for production
FROM openjdk:17-oracle

VOLUME /tmp
# Copy the compiled application from the builder stage
COPY target/*.jar backend.jar

# Set the main class (replace with your actual main class)
ENTRYPOINT ["java", "-jar", "backend.jar"]

# Expose the port your application listens on (if applicable)
EXPOSE 2026
