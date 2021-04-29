# Demo Spring Boot and Kafka

[![Build](https://github.com/abatalev/demo-spring-and-kafka/actions/workflows/build.yml/badge.svg)](https://github.com/abatalev/demo-spring-and-kafka/actions/workflows/build.yml)  [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=abatalev_demo-spring-and-kafka&metric=alert_status)](https://sonarcloud.io/dashboard?id=abatalev_demo-spring-and-kafka)

## launch kafka and create topics

```sh
docker-compose up -d
docker-compose exec broker kafka-topics --create \
   --topic my-topic \
   --bootstrap-server broker:9092 \
   --replication-factor 1 \
   --partitions 1
```

## Build and launch

```sh
mvn clean install
java -jar target/app.jar
```
