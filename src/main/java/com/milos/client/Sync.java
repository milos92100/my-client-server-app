package com.milos.client;

import com.milos.domain.Answer;
import com.milos.domain.logger.PrimitiveLogger;
import com.milos.domain.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import static com.milos.domain.Answer.Type.ACKNOWLEDGED;
import static com.milos.domain.Answer.Type.REJECTED;

public class Sync implements Runnable, Receiver.AnswerReceived, Sender.MessageSent {
    private Sender sender;
    private Receiver receiver;
    private PrimitiveLogger logger;
    private ProcessingMessagesStore store;
    private BlockingQueue<Message> queueToSync;

    public Sync(final Socket socket,
                final BlockingQueue<Message> queueToSync,
                final ProcessingMessagesStore store,
                final PrimitiveLogger logger) throws IOException {
        this.queueToSync = queueToSync;
        this.sender = new Sender(socket.getOutputStream(), queueToSync, this);
        this.receiver = new Receiver(socket.getInputStream(), this, logger);
        this.logger = logger;
        this.store = store;
    }

    @Override
    public void run() {
        receiver.start();
        sender.start();
    }

    @Override
    public void answerReceived(Answer answer) {
        logger.info("answer: " + answer + "");
        if (answer.messageWas(ACKNOWLEDGED)) {
            //TODO evaluate steps for acknowledged messages, maybe do nothing
            store.removeFromProcessingMessages(answer.getMessageId());
        } else if (answer.messageWas(REJECTED)) {
            //TODO save rejected messages to some persistent store or notify other parts of the system
            store.removeFromProcessingMessages(answer.getMessageId());
        } else {
            logger.error("Unknown answer type: " + answer.getType());
        }
    }

    @Override
    public void messageSent(Message message) {
        logger.info("sent: " + message);
        store.appendToProcessingMessages(message);
    }
}
