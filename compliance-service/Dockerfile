FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY ssoma-common ./ssoma-common
RUN cd ssoma-common && mvn clean install -DskipTests -q

COPY compliance-service ./compliance-service
RUN cd compliance-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/compliance-service/target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]