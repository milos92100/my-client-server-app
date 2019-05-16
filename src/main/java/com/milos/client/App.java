package com.milos.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }

        Socket socket = null;

        try {
            socket = new Socket(args[0], 59898);
            System.out.println("Enter lines of text then Ctrl+D or Ctrl+C to quit");

            Scanner scanner = new Scanner(System.in);
            Scanner in = new Scanner(socket.getInputStream());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
                System.out.println(in.nextLine());
            }
        } finally {
            if (socket != null) {
                socket.close();
            }

        }
    }


}
