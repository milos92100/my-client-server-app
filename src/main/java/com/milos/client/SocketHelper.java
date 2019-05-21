package com.milos.client;

import com.milos.config.AppConfig;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketHelper {

    private static final int CLOSE_RETRY_MAX_CNT = 3;

    public static Socket createFromAppConfig(AppConfig appConfig) throws IOException {
        return new Socket(appConfig.getServerIp().getValue(), appConfig.getServerPort().getValue());
    }

    public static void closeWithRetry(Socket socket) {
        if (socket == null) {
            return;
        }
        int retryCnt = 0;

        while (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException exception) {
                if (retryCnt == CLOSE_RETRY_MAX_CNT) {
                    exception.printStackTrace();
                    break;
                }
                retryCnt++;
            }
        }
    }
}
