package com.milos.client;

import com.milos.domain.logger.PrimitiveLogger;

import java.io.InputStream;
import java.util.Scanner;

public class Receiver extends Thread {
    private PrimitiveLogger logger;
    private Scanner reader;
    private ClientInMemoryStore store;

    public Receiver(final InputStream in, final ClientInMemoryStore store, final PrimitiveLogger logger) {
        this.reader = new Scanner(in);
        this.store = store;
        this.logger = logger;

    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                if (reader.hasNextLine()) {
                    String answer = reader.nextLine();
                    //store.removeFromProcessingMessages(answer);
                    logger.info("answer: '" + answer + "'");
                }
            }
        } catch (IllegalStateException e) {
            logger.warn("Socket is closed: " + e.getMessage());
        }
    }
}
