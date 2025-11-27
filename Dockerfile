FROM eclipse-temurin:25-jdk-alpine

VOLUME /tmp

# Before i forget. Always run 'mvn clean package' first before this line under runs
COPY app.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]