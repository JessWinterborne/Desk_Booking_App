# Use an official Maven image to build the app
FROM maven:3.8.1-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/deskbooking-1.0.0.jar deskbooking.jar

# Run the application
ENTRYPOINT ["java", "-jar", "deskbooking.jar"]
