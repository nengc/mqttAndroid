package com.broker.keplerq;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.moquette.BrokerConstants;

public class MqttService extends IntentService {

    public MqttService(){
        super(MqttService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        startForeground(1,  getNotification());
        ExecutorService executor = Executors.newFixedThreadPool(1);
        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new MQTTBroker(getConfig()));
        executor.execute(futureTask);
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }

    private Notification getNotification(){
        return MyApplication.getMyAppsNotificationManager().getNotif(MainActivity.class,
                "BackgroundService running",
                1,
                false,
                1);
    }

    private final Properties getConfig() {
        Properties properties = new Properties();
        properties.setProperty("port", "1883");
        properties.setProperty(BrokerConstants.HOST_PROPERTY_NAME, "192.168.4.121");
        properties.setProperty(BrokerConstants.WEB_SOCKET_PORT_PROPERTY_NAME, "8080");
        return properties;
    }

}
