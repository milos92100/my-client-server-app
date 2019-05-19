package com.milos.client;

import com.milos.domain.logger.PrimitiveLogger;

import java.io.IOException;
import java.net.Socket;

public class Sync implements Runnable {
    private Sender sender;
    private Receiver receiver;

    public Sync(final Socket socket, final ClientInMemoryStore store, final PrimitiveLogger logger) throws IOException {
        this.sender = new Sender(socket.getOutputStream(), store, logger);
        this.receiver = new Receiver(socket.getInputStream(), store, logger);
    }

    @Override
    public void run() {
        receiver.start();
        sender.start();
    }
}
