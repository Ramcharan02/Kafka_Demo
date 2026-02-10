package com.example;

public class CombinedApp {
    public static void main(String[] args) {
        // 1. Start the Consumer in a separate thread
        Thread consumerThread = new Thread(() -> {
            System.out.println("Starting Consumer...");
            ConsumerApp.main(args);
        });
        consumerThread.start();

        // 2. Give the Consumer a second to warm up
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        // 3. Start the Producer in the main thread
        // Because they share the same JVM, they share the same SchemaRegistryController static map!
        System.out.println("Starting Producer...");
        ProducerApp.main(args);
    }
}