FROM eclipse-temurin:21-jdk as build

WORKDIR /app
COPY . /app

RUN chmod +x mvnw
RUN ./mvnw package -DskipTests
RUN mv -f target/*.jar app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/app.jar .

COPY src/main/resources/certs /app/certs

RUN useradd runtime
USER runtime

ENTRYPOINT ["java", "-jar", "app.jar"]
