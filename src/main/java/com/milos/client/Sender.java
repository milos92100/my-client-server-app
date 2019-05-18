package com.milos.client;

import com.milos.domain.logger.PrimitiveLogger;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Sender extends Thread {
    private PrimitiveLogger logger;
    private PrintWriter writer;
    private BlockingQueue<String> messageQueue;

    public Sender(final OutputStream out, BlockingQueue<String> messageQueue, final PrimitiveLogger logger) {
        this.logger = logger;
        this.writer = new PrintWriter(out, true);
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                String msg = messageQueue.poll(); //TODO evaluate take() method instead of poll();
                if (msg != null) {
                    writer.println(msg);
                    logger.info("sent: '" + msg + "'");
                }
                Thread.sleep(0);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
