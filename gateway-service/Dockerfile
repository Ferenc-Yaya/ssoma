FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY gateway-service ./gateway-service
RUN cd gateway-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/gateway-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
