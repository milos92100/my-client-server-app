package com.milos.client;

import com.milos.config.AppConfig;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;

public class App {


    public static void main(String[] args) throws Exception {
        AppConfig appConfig = AppConfig.fromResourceBundle(ResourceBundle.getBundle("config"));

        Socket socket = null;

        try {
            socket = new Socket(appConfig.getServerIp().getValue(), appConfig.getServerPort().getValue());
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
