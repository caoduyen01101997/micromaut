# VPS Docker Deployment Fix TODO

## Plan Steps:
### 1. ✅ Update Dockerfile (debian temurin:11-jre minimal + parse fix + no apt error + wget healthcheck)
### 2. [] Test local build: ./gradlew shadowJar &amp;&amp; docker build -t micromaut .
### 3. [] Test local run: docker run -p 8080:8080 micromaut
### 4. [] Push to GHCR: Update workflow platforms if ARM, then git push
### 5. [] VPS deploy: docker pull ghcr.io/[owner]/micromaut:latest &amp;&amp; docker run -d -p 80:8080 --name app --restart always ghcr.io/[owner]/micromaut:latest
### 6. [] Check logs: docker logs app
### 7. [ ] Verify http://[vps-ip]
