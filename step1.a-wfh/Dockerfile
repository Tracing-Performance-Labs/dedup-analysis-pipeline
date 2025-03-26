FROM gradle:7.6-jdk17 AS build

WORKDIR /app

COPY . .
RUN ./gradlew build --no-daemon -x test

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/app/build/libs/app-all.jar app.jar

ENV KAFKA_ADDRESS=broker:29092
ENV STEP_01_A_WFH_TOPIC_INPUT_NAME=step0-ingestion
ENV STEP_01_A_WFH_TOPIC_OUTPUT_NAME=step01-a-wfh

ENTRYPOINT ["java", "-jar", "app.jar"]
