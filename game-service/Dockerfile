FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /build
COPY . .
RUN mvn -B -q -pl game-service -am package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /build/game-service/target/game-service.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
