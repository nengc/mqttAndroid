package com.broker.keplerq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

//import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {


    private Intent serviceIntent;
    private Intent subscriptionIntent;
    public static final String TAG = "TAG";
    MqttAndroidClient client = new MqttAndroidClient(MyApplication.getContext(), "tcp://192.168.4.121:1883","client", Ack.AUTO_ACK);
    private WebSocketClient mWebSocketClient;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Button button= new Button (this);
        Button subButton= new Button (this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 1000;
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        FrameLayout.LayoutParams subparams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        subparams.topMargin = 1200;
        subparams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        button.setText("Start Mqtt Broker");
        subButton.setText("Subscribe To Topic");

        addContentView(button, params);
        addContentView(subButton, subparams);
        // setContentView(tv);
        button.setOnClickListener(this);
        button.setId(1);
        subButton.setOnClickListener(this);
        subButton.setId(2);


        serviceIntent = new Intent(getApplicationContext(), MqttService.class);
        subscriptionIntent = new Intent(getApplicationContext(), SubscriptionService.class);

    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://192.168.4.121:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                new Thread(() -> Log.d(TAG, message));
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        if (view.getId() == 1) {
            ContextCompat.startForegroundService(this,serviceIntent);
        }
        else {
//            connectWebSocket();
//            connetx();
            try {
                MqttClient sampleClient = new MqttClient("ws://192.168.4.121:8080", "clientId", new MemoryPersistence());
                sampleClient.setCallback(new MqttCallback()
                {
                    @Override
                    public void connectionLost(Throwable cause) {
                        System.out.println("Lost connection");
                        cause.printStackTrace(); //EOFException thrown here within a few seconds
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        System.out.println(message.getPayload());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                    }
                });

                System.out.println("Connecting to broker: ");
                sampleClient.connect();
                System.out.println("Connected");
            } catch(MqttException me) {
                me.printStackTrace();
            }
        }

    }

    private void connetx() {
        IMqttToken token = client.connect();
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG, "successful");
                sub();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d(TAG, "failure");
            }
        });

    }

    private void sub() {
        client.subscribe("spacex",0);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG,new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }
}
