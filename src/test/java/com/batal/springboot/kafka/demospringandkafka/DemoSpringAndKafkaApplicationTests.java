package com.batal.springboot.kafka.demospringandkafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {
            "listeners=PLAINTEXT://localhost:39092", 
            "port=39092", "log.dir=target/logs"
        }, topics = {"my-test-topic"}
)
class DemoSpringAndKafkaApplicationTests {

    static {
        System.setProperty("java.io.tmpdir", "target/tmp");
        new File("target/tmp").mkdirs();
    }

    private static String TOPIC = "my-test-topic";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void checkSendData() {
        String data = UUID.randomUUID().toString();

        KafkaService service = new KafkaService(TOPIC, getTemplate(embeddedKafkaBroker));
        service.sendData(data);
        assertEquals(data, getRecord(embeddedKafkaBroker, TOPIC).value());
    }

    private KafkaTemplate<Integer, String> getTemplate(EmbeddedKafkaBroker broker) {
        return new KafkaTemplate<Integer, String>(
                new DefaultKafkaProducerFactory(
                        KafkaTestUtils.producerProps(broker)));
    }

    private ConsumerRecord<Integer, String> getRecord(EmbeddedKafkaBroker broker, String topic) {
        Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<>(
                new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", broker)),
                new IntegerDeserializer(),
                new StringDeserializer()).createConsumer();
        consumer.subscribe(singleton(topic));
        return KafkaTestUtils.getSingleRecord(consumer, topic);
    }
}
