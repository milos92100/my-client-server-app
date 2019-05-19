package com.milos.client;

import com.milos.domain.Message;

import java.util.*;

/**
 * This class represents a in memory store that should hold
 * messages that processed and await answer form server.
 */
public class ProcessingMessagesStore {

    private class ProcessingMessage {
        public Date sentAt;
        public Message message;

        public ProcessingMessage(final Date sentAt, final Message message) {
            this.sentAt = sentAt;
            this.message = message;
        }

        public UUID getId() {
            return this.message.getId();
        }
    }

    private final List<ProcessingMessage> processingMessages = Collections.synchronizedList(new ArrayList<ProcessingMessage>());

    public void appendToProcessingMessages(Message message) {
        processingMessages.add(new ProcessingMessage(new Date(), message));
    }

    public boolean removeFromProcessingMessages(UUID messageId) {
        synchronized (processingMessages) {
            Iterator<ProcessingMessage> it = processingMessages.iterator();
            while (it.hasNext()) {
                if (it.next().getId().equals(messageId)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }
    }

    public Message getProcessingMessage(UUID messageId) {
        synchronized (processingMessages) {
            Iterator<ProcessingMessage> it = processingMessages.iterator();
            while (it.hasNext()) {
                Message message = it.next().message;

                if (message.getId().equals(messageId)) {
                    return message;
                }
            }
            return null;
        }
    }
}