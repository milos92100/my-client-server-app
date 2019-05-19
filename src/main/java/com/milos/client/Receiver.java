package com.milos.client;

import com.milos.domain.Answer;
import com.milos.domain.logger.PrimitiveLogger;

import java.io.InputStream;
import java.util.Scanner;

public class Receiver extends Thread {
    private PrimitiveLogger logger;
    private Scanner reader;
    private AnswerReceived callback;

    public interface AnswerReceived {
        void answerReceived(Answer answer);
    }

    public Receiver(final InputStream inputStream,
                    final AnswerReceived callback,
                    final PrimitiveLogger logger) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream mast not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback mast not be null");
        }
        if (logger == null) {
            throw new IllegalArgumentException("logger mast not be null");
        }

        this.reader = new Scanner(inputStream);
        this.callback = callback;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                if (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    Answer answer = Answer.fromJson(line);
                    if (answer == null) {
                        logger.error("Failed to deserialize answer: " + line);
                        continue;
                    }
                    callback.answerReceived(answer);
                }
            }
        } catch (IllegalStateException e) {
            logger.warn("Socket is closed: " + e.getMessage());
        }
    }
}
