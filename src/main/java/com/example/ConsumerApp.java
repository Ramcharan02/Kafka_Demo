package com.example;

import com.example.audit.generated.AuditRecordProto.AuditRecord;
import com.example.serialization.CustomProtobufDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerApp {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "audit-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Use OUR Custom Deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomProtobufDeserializer.class.getName());

        KafkaConsumer<String, AuditRecord> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("audit-logs"));

        System.out.println("Waiting for messages...");
        while (true) {
            ConsumerRecords<String, AuditRecord> records = consumer.poll(Duration.ofMillis(100));
            records.forEach(r -> System.out.println("Received: " + r.value().getTraceId()));
        }
    }
}