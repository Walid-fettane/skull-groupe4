FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /build
COPY . .
RUN mvn -B -q -pl player-service -am package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /build/player-service/target/player-service.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
