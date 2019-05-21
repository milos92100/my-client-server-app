package com.milos.server;

import com.milos.domain.Answer;
import com.milos.domain.Message;
import com.milos.domain.logger.PrimitiveLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Receiver implements Runnable {

    public interface MessageReceived {
        Answer messageReceived(Message message);
    }

    private Socket socket;
    private MessageReceived callback;
    private PrimitiveLogger logger;

    public Receiver(final Socket socket, final MessageReceived callback, PrimitiveLogger logger) {
        this.socket = socket;
        this.callback = callback;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            logger.info("Connected: " + socket);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Message message = Message.fromJson(line);
                Answer answer = callback.messageReceived(message);
                out.println(answer.toJson());
            }
        } catch (Exception e) {
            logger.error("Error:" + socket + ";" + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            logger.error("Closed: " + socket);
        }
    }

}
