package com.milos.client;

import com.milos.domain.Message;
import com.milos.domain.util.TimeUtil;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * Sender class is used poll data from the queue to publish(send) serialized
 * Messages the the given stream.It will notify the subscriber for every sent
 * message if it gets idle.
 *
 * @author Milos Stojanovic
 * @version 1.0
 * @see com.milos.domain.Message
 */
public class Sender implements Runnable {
    private static final long IDLE_TIMEOUT_MILLIS = 3000;
    private static final long IDLE_REPORT_FREQ_IN_MILLIS = 2000;

    private PrintWriter writer;
    private Actions callback;
    private BlockingQueue<Message> messagesToSend;

    private Date lastCheck;
    private Date lastReportedIdle;

    public interface Actions {
        void messageSent(Message message);

        void idle();
    }

    public Sender(final OutputStream outputStream,
                  final BlockingQueue<Message> messagesToSend,
                  final Actions callback) {
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

    protected boolean awaitingMessages() {
        return lastCheck == null;
    }

    protected boolean isIdle() {
        if (awaitingMessages()) {
            return false;
        }

        return (TimeUtil.diffInMillis(new Date(), lastCheck) > IDLE_TIMEOUT_MILLIS);
    }

    protected void reportIdle() {
        if (lastReportedIdle == null) {
            lastReportedIdle = new Date();
            callback.idle();
            return;
        }

        if (TimeUtil.diffInMillis(new Date(), lastReportedIdle) > IDLE_REPORT_FREQ_IN_MILLIS) {
            lastReportedIdle = new Date();
            callback.idle();
        }
    }

    protected boolean isInterrupted() {
        return Thread.currentThread().isInterrupted();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            Message message = messagesToSend.poll();
            if (message != null) {
                writer.println(message.toJson());
                callback.messageSent(message);
                lastCheck = new Date();
            }

            if (isIdle()) reportIdle();
        }
    }
}
