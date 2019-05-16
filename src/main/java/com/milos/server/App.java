package com.milos.server;

import com.milos.config.AppConfig;

import java.io.IOException;
import java.util.ResourceBundle;

public class App {

    public static void main(String[] args) throws IOException {
        AppConfig appConfig = AppConfig.fromResourceBundle(ResourceBundle.getBundle("config"));

        Program program = new Program(appConfig);
        program.run();
    }
}
