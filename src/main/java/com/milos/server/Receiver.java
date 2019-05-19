package com.milos.server;

import com.milos.domain.Answer;
import com.milos.domain.Message;

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

    public Receiver(final Socket socket, final MessageReceived callback) {
        this.socket = socket;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Message message = Message.fromJson(line);
                Answer answer = callback.messageReceived(message);
                out.println(answer.toJson());
            }
        } catch (Exception e) {
            System.out.println("Error:" + socket);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Closed: " + socket);
        }
    }

}
