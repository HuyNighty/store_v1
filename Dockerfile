# STAGE 1: build
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace

# cache dependencies
COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline

# copy project
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

RUN if [ -f "./mvnw" ]; then chmod +x ./mvnw; fi
RUN ./mvnw -B -DskipTests package

# STAGE 2: runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# copy bất kỳ jar nào trong target --> đặt tên chung là app.jar
COPY --from=builder /workspace/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
