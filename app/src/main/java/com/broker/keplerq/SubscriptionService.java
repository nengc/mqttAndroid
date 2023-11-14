package com.broker.keplerq;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class SubscriptionService extends IntentService {

    public SubscriptionService(){
        super(SubscriptionService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        startForeground(2, getNotification());
        ExecutorService executor = Executors.newFixedThreadPool(1);
        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new SubscribeTopic("spacex"));
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
                2
        );
    }
}
