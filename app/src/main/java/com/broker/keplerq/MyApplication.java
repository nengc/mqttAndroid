package com.broker.keplerq;


import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application {

    public static MyAppsNotificationManager notificationManager;
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        notificationManager = MyAppsNotificationManager.getInstance(this);
        Log.d("notificationManager",notificationManager.toString());
        notificationManager.registerNotificationChannelChannel(
                getString(R.string.channelId),
                "BackgroundService",
                "BackgroundService");
    }

    public static MyAppsNotificationManager getMyAppsNotificationManager(){
        return notificationManager;
    }
}