package com.milos.client;

import com.milos.domain.Answer;
import com.milos.domain.logger.PrimitiveLogger;
import com.milos.domain.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import static com.milos.domain.Answer.Type.ACKNOWLEDGED;
import static com.milos.domain.Answer.Type.REJECTED;

public class Sync implements Runnable, Receiver.AnswerReceived, Sender.Actions {
    private String name;
    private Sender sender;
    private Receiver receiver;
    private PrimitiveLogger logger;
    private BlockingQueue<Message> queueToSync;
    private LifeCycle callback;

    private long sentCount = 0;
    private long receivedCount = 0;

    private Thread senderThread;
    private Thread receiveThread;

    public interface LifeCycle {
        void finished();
    }

    public Sync(final String name,
                final OutputStream outputStream,
                final InputStream inputStream,
                final BlockingQueue<Message> queueToSync,
                final LifeCycle callback,
                final PrimitiveLogger logger
    ) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream mast not be null");
        }
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream mast not be null");
        }
        if (queueToSync == null) {
            throw new IllegalArgumentException("queueToSync mast not be null");
        }
        if (logger == null) {
            throw new IllegalArgumentException("logger mast not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback mast not be null");
        }

        this.name = name;
        this.queueToSync = queueToSync;
        this.sender = new Sender(outputStream, queueToSync, this);
        this.receiver = new Receiver(inputStream, this, logger);
        this.logger = logger;
        this.callback = callback;
    }

    @Override
    public void run() {
        receiveThread = new Thread(receiver);
        receiveThread.setName(name + "-ReceiverThread");
        receiveThread.start();

        senderThread = new Thread(sender);
        senderThread.setName(name + "-SenderThread");
        senderThread.start();
    }

    private boolean awaitsAnswers() {
        return sentCount > receivedCount;
    }

    @Override
    public void answerReceived(Answer answer) {
        receivedCount++;
        if (answer.messageWas(ACKNOWLEDGED)) {
            //TODO evaluate steps for acknowledged messages, maybe do nothing
        } else if (answer.messageWas(REJECTED)) {
            //TODO save rejected messages to some persistent store or notify other parts of the system
        } else {
            logger.error("Unknown answer type: " + answer.getType());
        }
    }

    @Override
    public void messageSent(Message message) {
        sentCount++;
    }

    @Override
    public void idle() {
        //TODO implement logic to await delayed answers from server or mark them as expired
        logger.info(name + " is idle; " + sentCount + "/" + receivedCount);

        if (awaitsAnswers()) {
            return;
        }

        receiveThread.interrupt();
        senderThread.interrupt();
        callback.finished();
    }
}
