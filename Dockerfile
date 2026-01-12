# ---------- BUILD STAGE ----------
FROM gradle:8.7-jdk21 AS build
WORKDIR /raflms

# Copy everything (multi-module needs full project)
COPY . .

# Build ONLY the Spring Boot module
RUN gradle :serverapi:bootJar --no-daemon

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre
WORKDIR /raflms

# Copy module jar
COPY --from=build /raflms/serverapi/build/libs/*.jar raflms-serverapi.jar

EXPOSE 8092
ENTRYPOINT ["java","-jar","raflms-serverapi.jar"]

