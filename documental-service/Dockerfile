FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY ssoma-common ./ssoma-common
RUN cd ssoma-common && mvn clean install -DskipTests -q

COPY documental-service ./documental-service
RUN cd documental-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/documental-service/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]