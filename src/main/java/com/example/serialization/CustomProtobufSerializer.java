package com.example.serialization;

import com.example.audit.generated.AuditRecordProto.AuditRecord;
import com.example.registry.SchemaRegistryController;
import org.apache.kafka.common.serialization.Serializer;
import java.nio.ByteBuffer;

public class CustomProtobufSerializer implements Serializer<AuditRecord> {
    @Override
    public byte[] serialize(String topic, AuditRecord data) {
        if (data == null) return null;

        // 1. Get Schema Name (In real life, this would be the full schema definition)
        String schemaName = data.getDescriptorForType().getName();

        // 2. Get ID from Registry
        int id = SchemaRegistryController.registerSchema(schemaName);

        // 3. Convert Object to Bytes
        byte[] protoBytes = data.toByteArray();

        // 4. Pack: [4 Bytes for ID] + [Protobuf Bytes]
        ByteBuffer buffer = ByteBuffer.allocate(4 + protoBytes.length);
        buffer.putInt(id);      // Write ID
        buffer.put(protoBytes); // Write Data

        return buffer.array();
    }
}