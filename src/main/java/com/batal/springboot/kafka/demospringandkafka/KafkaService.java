package com.batal.springboot.kafka.demospringandkafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class KafkaService {

    private static Logger log = LoggerFactory.getLogger(KafkaService.class);
    private final String producerTopic;

    private KafkaTemplate<Integer, String> template;

    @Autowired
    public KafkaService(
            @Value("${spring.kafka.producer.topic}") String producerTopic,
            KafkaTemplate<Integer, String> template) {
        this.producerTopic = producerTopic;
        this.template = template;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 500)
    public void produce() {
        String value = "Data " + (LocalTime.now().toNanoOfDay() / 1000000);
        sendData(value);
    }

    public void sendData(String value) {
        log.info("sent!! " + value);
        template.send(producerTopic, value);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}")
    public void listen(ConsumerRecord<?, ?> cr) throws Exception {
        log.info("loaded!!" + cr.value());
    }
}
