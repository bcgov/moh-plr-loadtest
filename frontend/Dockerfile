# Use the official Maven image as the base image
FROM maven:3.9.9-eclipse-temurin-21-alpine

WORKDIR /loadtestfrontend

# Copy the Maven project files into container
COPY pom.xml .
COPY src src/
COPY frontend frontend/

CMD ["mvn", "spring-boot:run"]

EXPOSE 9090
