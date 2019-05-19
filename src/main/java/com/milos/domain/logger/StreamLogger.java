package com.milos.domain.logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StreamLogger implements PrimitiveLogger, Closeable {

    private static final String DEBUG = "DEBUG";
    private static final String INFO = "INFO";
    private static final String WARN = "WARN";
    private static final String ERROR = "ERROR";

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private PrintStream out;
    private String appName;

    public StreamLogger(final PrintStream out, final String appName) {
        if (out == null) {
            throw new IllegalArgumentException("out mast not be null");
        }
        if (appName == null) {
            throw new IllegalArgumentException("appName mast not be null");
        }
        this.out = out;
        this.appName = appName;
    }

    private String currentTimeFormatted() {
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void printMessage(String level, String message) {
        String msg = new StringBuilder()
                .append("[").append(appName).append("]")
                .append("[").append(level).append("]")
                .append("[").append(currentTimeFormatted()).append("]")
                .append(message).toString();

        out.println(msg);
    }

    @Override
    public void debug(String message) {
        printMessage(DEBUG, message);
    }

    @Override
    public void info(String message) {
        printMessage(INFO, message);
    }

    @Override
    public void warn(String message) {
        printMessage(WARN, message);
    }

    @Override
    public void error(String message) {
        printMessage(ERROR, message);
    }

    @Override
    public void close() throws IOException {
        if (out != null) out.close();
    }

    @Override
    protected void finalize() throws Throwable {
        if (out != null) out.close();
        super.finalize();
    }
}
