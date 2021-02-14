# Demo Spring Boot and Kafka

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
