FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY ssoma-common ./ssoma-common
RUN cd ssoma-common && mvn clean install -DskipTests -q

COPY empresas-service ./empresas-service
RUN cd empresas-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/empresas-service/target/*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]