package com.milos.client;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientInMemoryStore {

    private class ProcessingMessage {
        private Date sentAt;
        private String message;

        public ProcessingMessage(final Date sentAt, final String message) {
            this.sentAt = sentAt;
            this.message = message;
        }
    }

    private final BlockingQueue<String> messagesToSend = new ArrayBlockingQueue<String>(100);
    private final List<ProcessingMessage> processingMessages = Collections.synchronizedList(new ArrayList<ProcessingMessage>());

    public String pollMessageToSend() {
        return messagesToSend.poll();
    }

    public void putMessageToSend(String message) throws InterruptedException {
        messagesToSend.put(message);
    }

    public void appendToProcessingMessages(String message) {
        processingMessages.add(new ProcessingMessage(new Date(), message));
    }

    public boolean removeFromProcessingMessages(String message) {
        synchronized (processingMessages) {
            Iterator<ProcessingMessage> it = processingMessages.iterator();
            while (it.hasNext()) {
                if (it.next().message.equals(message)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }
    }
}
