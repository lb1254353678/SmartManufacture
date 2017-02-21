package com.goodbaby.push.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;

/**
 * Created by goodbaby on 17/2/8.
 */

public class OkHttpUtils  {
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public Executor getDelivery() {
        //return mPlatform.defaultCallbackExecutor();
        return null;
    }


}
