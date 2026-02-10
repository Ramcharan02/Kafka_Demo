package com.example.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SchemaRegistryController {
    // Simulating a database in memory
    private static final Map<Integer, String> ID_TO_SCHEMA = new ConcurrentHashMap<>();
    private static final Map<String, Integer> SCHEMA_TO_ID = new ConcurrentHashMap<>();
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

    // Producer calls this
    public static int registerSchema(String schemaName) {
        if (SCHEMA_TO_ID.containsKey(schemaName)) {
            return SCHEMA_TO_ID.get(schemaName);
        }
        int id = ID_COUNTER.getAndIncrement();
        ID_TO_SCHEMA.put(id, schemaName);
        SCHEMA_TO_ID.put(schemaName, id);
        System.out.println("[Registry] Registered Schema: " + schemaName + " as ID: " + id);
        return id;
    }

    // Consumer calls this
    public static String getSchema(int id) {
        return ID_TO_SCHEMA.get(id);
    }
}