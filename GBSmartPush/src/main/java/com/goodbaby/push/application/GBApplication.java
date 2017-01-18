package com.goodbaby.push.application;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by goodbaby on 17/1/18.
 */
public class GBApplication extends Application {
    private static GBApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        initJPush();
    }

    public static GBApplication getAppContext() {
        return mInstance;
    }

    /*
    * 初始化JPush
    * */
    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
    }

}

