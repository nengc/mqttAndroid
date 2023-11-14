package com.broker.keplerq;

import android.util.Log;

import java.io.IOException;
import java.net.BindException;
import java.util.Properties;
import java.util.concurrent.Callable;
import io.moquette.BrokerConstants;
import io.moquette.broker.Server;

public class MQTTBroker implements Callable<Boolean> {
    public static final Companion Companion = new Companion();
    private static Server server;
    private final String TAG = "Broker";
    private Properties config;

    public MQTTBroker(Properties properties) {
        this.config = properties;
    }

    public static final class Companion {
        public Companion() {

        }

        public final Server getServer() {
            return MQTTBroker.server;
        }
    }

    public final void stopServer() {
        Server server2 = server;
        if (server2 != null) {
            server2.stopServer();
        }
    }



    @Override
    public Boolean call() {
        try {
            server = new ServerInstance().getServerInstance();
            Log.d(this.TAG, "call: " + this.config);
            String str = this.TAG;
            Properties properties = this.config;
            Log.d(str, "call: " + (properties != null ? properties.get(BrokerConstants.PASSWORD_FILE_PROPERTY_NAME) : null));
            Server server2 = server;
            if (server2 != null) {
                server2.startServer(this.config);
            }
            Log.d(this.TAG, "MQTT Broker Started");
            return true;
        } catch (BindException e) {
            Log.e(this.TAG, "Address already in use. Unable to bind.");
            Log.e(this.TAG, "Error : " + e.getMessage());
        } catch (IOException e2) {
            Log.e(this.TAG, "Error : " + e2.getMessage());
            return false;
        }
        return true;

    }
}
