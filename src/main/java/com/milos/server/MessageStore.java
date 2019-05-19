package com.milos.server;

import com.milos.domain.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageStore {

    private final BlockingQueue<Message> accountMessagesQueue;
    private final BlockingQueue<Message> userMessagesQueue;

    public MessageStore() {
        accountMessagesQueue = new ArrayBlockingQueue<Message>(100);
        userMessagesQueue = new ArrayBlockingQueue<Message>(100);
    }

    public BlockingQueue<Message> getAccountMessagesQueue() {
        return accountMessagesQueue;
    }

    public BlockingQueue<Message> getUserMessagesQueue() {
        return userMessagesQueue;
    }
}
