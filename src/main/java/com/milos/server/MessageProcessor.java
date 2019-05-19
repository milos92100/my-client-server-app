package com.milos.server;

import com.milos.domain.Answer;
import com.milos.domain.Message;
import com.milos.domain.logger.PrimitiveLogger;

import static com.milos.domain.Answer.Type.ACKNOWLEDGED;
import static com.milos.domain.Answer.Type.NOT_PROCESSED_DUE_FAILURE;
import static com.milos.domain.Answer.Type.REJECTED;
import static com.milos.domain.Message.Type.ACCOUNT_CHANGE;
import static com.milos.domain.Message.Type.USER_CHANGE;

public class MessageProcessor implements Receiver.MessageReceived {

    private PrimitiveLogger logger;
    private MessageStore messageStore;

    public MessageProcessor(PrimitiveLogger logger, MessageStore messageStore) {
        this.logger = logger;
        this.messageStore = messageStore;
    }

    protected void process(Message message) throws InterruptedException {
        if (message.typeOf(ACCOUNT_CHANGE)) {
            messageStore.getAccountMessagesQueue().put(message);
        }

        if (message.typeOf(USER_CHANGE)) {
            messageStore.getUserMessagesQueue().put(message);
        }
    }

    protected boolean invalid(Message message) {
        return !message.typeOf(ACCOUNT_CHANGE) && !message.typeOf(USER_CHANGE);
    }

    @Override
    public Answer messageReceived(Message message) {
        if (invalid(message)) {
            return new Answer(message.getId(), REJECTED);
        }
        try {
            process(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new Answer(message.getId(), NOT_PROCESSED_DUE_FAILURE);
        }
        return new Answer(message.getId(), ACKNOWLEDGED);
    }
}
