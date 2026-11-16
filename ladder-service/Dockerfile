FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /build
COPY . .
RUN mvn -B -q -pl ladder-service -am package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /build/ladder-service/target/ladder-service.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
