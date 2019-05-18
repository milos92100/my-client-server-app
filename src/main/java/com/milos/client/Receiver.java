package com.milos.client;

import com.milos.domain.logger.PrimitiveLogger;

import java.io.InputStream;
import java.util.Scanner;

public class Receiver extends Thread {
    private PrimitiveLogger logger;
    private Scanner reader;

    public Receiver(final InputStream in, final PrimitiveLogger logger) {
        this.logger = logger;
        this.reader = new Scanner(in);
    }

    @Override
    public void run() {
        try {

            while (!isInterrupted()) {
                if (reader.hasNextLine()) {
                    String answer = reader.nextLine();
                    logger.info("answer: '" + answer + "'");
                } else {

                }

                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }

            }
        } catch (IllegalStateException e) {
            logger.warn("Socket is closed: " + e.getMessage());
        }
    }
}
