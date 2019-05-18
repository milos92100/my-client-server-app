package com.milos.server;

import com.milos.config.AppConfig;
import com.milos.domain.logger.PrimitiveLogger;
import com.milos.domain.logger.StreamLogger;

import java.io.IOException;
import java.util.ResourceBundle;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        AppConfig appConfig = AppConfig.fromResourceBundle(ResourceBundle.getBundle("config"));
        PrimitiveLogger logger = new StreamLogger(System.out, "server");

        Program program = new Program(appConfig, logger);
        program.run();
    }
}
