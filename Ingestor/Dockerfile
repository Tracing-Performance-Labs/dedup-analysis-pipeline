FROM gradle:7.6-jdk17 AS build

WORKDIR /app

COPY . .
RUN ./gradlew build --no-daemon -x test

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/app/build/libs/app-all.jar app.jar
COPY ./data /app/data/

ENV INGESTION_TOPIC=step0-ingestion
ENV INGESTION_DATA_DIR=/app/data

ENTRYPOINT ["java", "-jar", "app.jar"]
