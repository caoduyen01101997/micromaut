# Stage 1: Build the application
FROM gradle:7.5.1-jdk11 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle shadowJar --no-daemon

# Stage 2: Create the final image
FROM --platform=linux/amd64 openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
