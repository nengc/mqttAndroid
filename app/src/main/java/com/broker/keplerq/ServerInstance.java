package com.broker.keplerq;

import io.moquette.broker.Server;

public class ServerInstance {
    private static final Object INSTANCE_LOCK = new Object();
    private static Server serverInstance;

    public ServerInstance() {
    }

    public static Server getServerInstance() {
        try {
            if (serverInstance == null) {
                synchronized (INSTANCE_LOCK) {
                    if (serverInstance == null) {
                        Server server2 = new Server();
                        serverInstance = server2;
                        return server2;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverInstance;
    }
}
