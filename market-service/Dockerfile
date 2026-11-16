FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /build
COPY . .
RUN mvn -B -q -pl market-service -am package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /build/market-service/target/market-service.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
