package com.broker.keplerq;

import android.util.Log;

import com.broker.keplerq.MqttClient.ClientInstance;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import java.util.concurrent.Callable;

import info.mqtt.android.service.MqttAndroidClient;



public class SubscribeTopic implements Callable<Boolean> {

    public static final Companion Companion = new Companion();
    private final String TAG = "Broker";
    private static String topic;
    public static MqttAndroidClient mqttAndroidClient;
    public String clientId = "ExampleAndroidClient";

    public SubscribeTopic(String topic) {
        this.topic = topic;
    }

    public static final class Companion {
        public Companion() {

        }

        public final String getSubscription() {
            return SubscribeTopic.topic;
        }
    }

    public final void UnSubscribe() {

    }

    @Override
    public Boolean call() throws Exception {
        MqttAndroidClient mqttAndroidClient = new ClientInstance().getClientInstance();
        Log.d("spacex", "call: " + this.topic);
            try {
                Log.d("tag","client.isConnected()>>>>>>>>" + mqttAndroidClient.isConnected());
                if (mqttAndroidClient.isConnected()) {
                    mqttAndroidClient.subscribe("spacex", 0);
                    mqttAndroidClient.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            Log.d("tag","message>>" + new String(message.getPayload()));
                            Log.d("tag","topic>>" + topic);
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
                }
            } catch (Exception e) {
                Log.d("tag","Error :" + e);
            }
        Log.d(this.TAG, "MQTT Broker Started");
        return true;
    }

    public void subscribeToTopic(){
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
//                    addToHistory("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    addToHistory("Failed to subscribe");
                }
            });

    }
}
