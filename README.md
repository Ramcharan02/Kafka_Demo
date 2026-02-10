1. The Problem: Heavy & Fragile Messages
Sending raw JSON messages (like {"id": "123", "name": "Alice"}) is easy to start with but problematic at scale:
Wasteful (Bandwidth): We send field names like "id" and "name" with every single message, repeating text unnecessarily.
Fragile (Breaking Changes): There is no contract. If the Producer changes "name" to "fullName", the Consumer code fails with a runtime error because it can't find the field it expects.

2. The Solution: Registry
Instead of sending the schema (field names) with every message, we store the schema once in a central "Phonebook" (the Registry) and assign it a unique ID (e.g., 1).
Producer: Registers the schema, gets ID: 1, and sends [ID: 1] + [Compressed Binary Data].
Consumer: Sees ID: 1, looks up the "Phonebook" to retrieve the schema, and uses it to decode the binary data back into a readable object.

3. How It Works (The Code Components)
This project simulates this architecture using four key components:

    • AuditRecord.proto (The Contract): A Protocol Buffer file that strictly defines the data structure (e.g., trace_id, payload). Both Producer and Consumer agree on this file.

    • SchemaRegistryController (The Phonebook): A simulated in-memory store that maps a Schema Definition to a unique Integer ID.

    • CustomProtobufSerializer (The Packer): Intercepts the object before it goes to Kafka. It talks to the Registry to get the ID and packs the message as [4-byte ID] + [Data].

    • CustomProtobufDeserializer (The Unpacker): Intercepts the message from Kafka. It reads the ID, fetches the correct Schema, and reconstructs the Java object.

5. How to Run
start the local Kafka broker
Build: Run mvn clean install to auto-generate the Java classes from the .proto file.
Run: Execute the CombinedApp class
Note: We run a CombinedApp to simulate a shared registry in memory for demonstration purposes.
In a real production environment, you would run the Producer and Consumer as separate applications and
connect them to a standalone Schema Registry Service (like Confluent Schema Registry) over the network.

Result: You will see the Producer send a message with ID: 1, and the Consumer successfully read and print TRACE-505.
