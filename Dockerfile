# Stage 1: Build
FROM gradle:7.5.1-jdk11 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle shadowJar --no-daemon

# Stage 2: Run (Debian base - VPS universal compatibility)
FROM eclipse-temurin:11-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

# Install fonts for headless JVM (VPS common issue)
RUN apt-get update &amp;&amp; apt-get install -y fontconfig &amp;&amp; rm -rf /var/lib/apt/lists/*

EXPOSE 8080

# Non-root user for security
RUN adduser --disabled-password --gecos '' appuser &amp;&amp; chown -R appuser:appuser /app
USER appuser

# Fix entropy + tối ưu JVM
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

ENTRYPOINT ["java", \
 "-XX:+UseContainerSupport", \
 "-XX:MaxRAMPercentage=75.0", \
 "-Djava.awt.headless=true", \
 "-Djava.security.egd=file:/dev/./urandom", \
 "-jar", "app.jar"]
