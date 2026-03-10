# ------ STAGE 1: BUILD Stage ---------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

#COPY pom.xml first (for layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

#Now copy source code and build
COPY src ./src
RUN mvn package -DskipTests

# --------- STAGE 2: RUN TIME STAGE ---------
FROM maven:3.9.9-eclipse-temurin-21
WORKDIR /app

#COPY only the compiled JAR from Stage 1
COPY --from=builder /app/target/product-service.jar app.jar

#Create non-root user for security
#RUN addgroup --system appgroup && adduser --system --ingroup appgorup appuser
#USER appuser

EXPOSE 8081

#Healthcheck
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8081/actuator/health || exit
