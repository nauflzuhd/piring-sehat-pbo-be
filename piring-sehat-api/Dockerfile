# =====================================================================
# DOCKERFILE - PIRING SEHAT API (Spring Boot + Maven)
# =====================================================================
# Multi-stage build: Stage 1 build JAR, Stage 2 run JAR
# Digunakan untuk deploy ke Render.com (Web Service - Docker)
# =====================================================================

# ─── STAGE 1: Build ───
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Maven wrapper dan konfigurasi
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies terlebih dahulu (memanfaatkan Docker layer caching)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src/ src/

# Build JAR tanpa menjalankan tests
RUN ./mvnw package -DskipTests -B

# ─── STAGE 2: Run ───
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy JAR dari stage builder
COPY --from=builder /app/target/*.jar app.jar

# Expose port (Render akan mengarahkan traffic ke port ini)
EXPOSE 8080

# Jalankan aplikasi Spring Boot dengan profil "production"
# Profil ini akan membaca file application-production.properties
# yang HANYA berisi referensi ${ENV_VAR} (tanpa kredensial hardcoded)
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]
