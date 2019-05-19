package com.milos.client;

import com.milos.domain.logger.PrimitiveLogger;

import java.io.OutputStream;
import java.io.PrintWriter;

public class Sender extends Thread {
    private PrimitiveLogger logger;
    private PrintWriter writer;
    private ClientInMemoryStore store;

    public Sender(final OutputStream out, final ClientInMemoryStore store, final PrimitiveLogger logger) {
        this.logger = logger;
        this.writer = new PrintWriter(out, true);
        this.store = store;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            String message = store.pollMessageToSend();
            if (message != null) {
                writer.println(message);
                //store.appendToProcessingMessages(message);
                logger.info("sent: '" + message + "'");
            }
        }
    }
}
