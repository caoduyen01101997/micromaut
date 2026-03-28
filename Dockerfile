# Multi-stage build cho VPS Ubuntu 22.04
FROM gradle:8.4-jdk17 AS build

WORKDIR /home/gradle/app

# Copy Gradle config trước để cache dependencies
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle/wrapper ./gradle/wrapper

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source
COPY src ./src

# Build shadow JAR
RUN ./gradlew shadowJar --no-daemon

# Runtime stage - Alpine tương thích Ubuntu VPS
FROM eclipse-temurin:11-jre-alpine

# Install tools cho debug
RUN apk add --no-cache curl bash postgresql-client nmap tini

WORKDIR /app

# Copy JAR
COPY --from=build /home/gradle/app/build/libs/*-all.jar ./app.jar

EXPOSE 8080

# Healthcheck với retry
HEALTHCHECK --interval=30s --timeout=5s --start-period=120s --retries=5 \
  CMD curl -f http://localhost:8080 || exit 1

# JVM + Hikari tuning cho connect chậm
ENTRYPOINT ["/sbin/init"]

