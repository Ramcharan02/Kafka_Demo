package com.example.serialization;

import com.example.audit.generated.AuditRecordProto.AuditRecord;
import com.example.registry.SchemaRegistryController;
import org.apache.kafka.common.serialization.Deserializer;
import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;

public class CustomProtobufDeserializer implements Deserializer<AuditRecord> {
    @Override
    public AuditRecord deserialize(String topic, byte[] data) {
        if (data == null) return null;

        ByteBuffer buffer = ByteBuffer.wrap(data);

        // 1. Read the ID
        int id = buffer.getInt();

        // 2. Validate against Registry
        String schemaName = SchemaRegistryController.getSchema(id);
        if (schemaName == null) {
            throw new RuntimeException("Unknown Schema ID: " + id);
        }

        // 3. Read the rest of the data
        byte[] actualData = new byte[data.length - 4];
        buffer.get(actualData);

        // 4. Reconstruct Object
        try {
            return AuditRecord.parseFrom(actualData);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Parse error", e);
        }
    }
}