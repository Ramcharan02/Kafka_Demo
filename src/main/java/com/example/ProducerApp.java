package com.example;

import com.example.audit.generated.AuditRecordProto.AuditRecord;
import com.example.serialization.CustomProtobufSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class ProducerApp {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Use OUR Custom Serializer
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomProtobufSerializer.class.getName());

        KafkaProducer<String, AuditRecord> producer = new KafkaProducer<>(props);

        AuditRecord record = AuditRecord.newBuilder()
                .setTraceId("TRACE-505")
                .setServiceName("OrderService")
                .setPayload("Order Created")
                .setTimestamp(System.currentTimeMillis())
                .build();

        producer.send(new ProducerRecord<>("audit-logs", "key", record));
        System.out.println("Message sent successfully!");
        producer.close();
    }
}