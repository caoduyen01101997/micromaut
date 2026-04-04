# Stage 1: Build
FROM gradle:7.5.1-jdk11 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle shadowJar --no-daemon

# Stage 2: Run (Debian minimal - VPS stable)
FROM eclipse-temurin:11-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

# Non-root user
RUN adduser --disabled-password --gecos '' appuser && chown -R appuser:appuser /app
USER appuser

HEALTHCHECK --interval=30s --timeout=3s CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.awt.headless=true", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
