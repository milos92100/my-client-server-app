package com.milos.client;

import com.milos.domain.Message;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Sender extends Thread {
    private PrintWriter writer;
    private MessageSent callback;
    private BlockingQueue<Message> messagesToSend;

    public interface MessageSent {
        void messageSent(Message message);
    }

    public Sender(final OutputStream outputStream,
                  final BlockingQueue<Message> messagesToSend,
                  final MessageSent callback) {
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream mast not be null");
        }
        if (messagesToSend == null) {
            throw new IllegalArgumentException("messagesToSend mast not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback mast not be null");
        }

        this.writer = new PrintWriter(outputStream, true);
        this.messagesToSend = messagesToSend;
        this.callback = callback;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            Message message = messagesToSend.poll();
            if (message != null) {
                writer.println(message.toJson());
                callback.messageSent(message);
            }
        }
    }
}
