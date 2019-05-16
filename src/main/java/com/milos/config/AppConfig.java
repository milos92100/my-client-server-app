package com.milos.config;

import com.sun.istack.internal.NotNull;

import java.util.ResourceBundle;

public class AppConfig {
    private static final String SERVER_PORT = "serverPort";
    private static final String SERVER_IP = "serverIp";

    private ServerPort serverPort;
    private ServerIp serverIp;

    /**
     * Creates a application configuration instance. It may throw an IllegalStateException
     * or IllegalArgumentException weather some required configuration is missing or is not valid.
     *
     * @param bundle ResourceBundle
     * @return AppConfig
     * @throws IllegalStateException    if a property missing
     * @throws IllegalArgumentException if a property is not valid
     */
    public static AppConfig fromResourceBundle(@NotNull ResourceBundle bundle) throws IllegalStateException, IllegalArgumentException {
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle must not be null");
        }

        throwIfMissing(bundle, SERVER_PORT);
        throwIfMissing(bundle, SERVER_IP);

        return new AppConfig(
                new ServerPort(Integer.valueOf(bundle.getString(SERVER_PORT))),
                new ServerIp(bundle.getString(SERVER_IP))
        );
    }

    private static void throwIfMissing(ResourceBundle bundle, String key) {
        if (!bundle.containsKey(key)) {
            throw new IllegalStateException(key + " is missing from configuration");
        }
    }

    public AppConfig(final ServerPort serverPort, final ServerIp serverIp) {
        this.serverPort = serverPort;
        this.serverIp = serverIp;
    }

    public ServerPort getServerPort() {
        return serverPort;
    }

    public ServerIp getServerIp() {
        return serverIp;
    }
}
