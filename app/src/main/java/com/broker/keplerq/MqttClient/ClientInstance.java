package com.broker.keplerq.MqttClient;

import android.util.Log;

import com.broker.keplerq.MyApplication;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;


public class ClientInstance {
    private static final Object INSTANCE_LOCK = new Object();
    private static MqttAndroidClient clientInstance;

    public ClientInstance() {
    }

    public static MqttAndroidClient getClientInstance() {
        try {
            if (clientInstance == null) {
                synchronized (INSTANCE_LOCK) {
                    if (clientInstance == null) {
                        MqttAndroidClient client = new MqttAndroidClient(MyApplication.getContext(), "tcp://192.168.4.121","client", Ack.AUTO_ACK);
                        clientInstance = client;
                        return client;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientInstance;
    }
}
