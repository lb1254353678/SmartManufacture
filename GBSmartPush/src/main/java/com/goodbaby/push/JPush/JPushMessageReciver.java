package com.goodbaby.push.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by goodbaby on 17/1/18.
 */

public class JPushMessageReciver extends BroadcastReceiver {

    private String TAG = "***JPushMessageReciver***";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //Log.d(TAG,"[MyReciver] onReceiver -" + intent.getAction() + ",extras:" + bundle.get(bundle));
        if(JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())){
//            功能描述:
//            SDK 向 JPush Server 注册所得到的注册 ID 。
//            一般来说，可不处理此广播信息。
//            要深入地集成极光推送，开发者想要自己保存App用户与JPush 用户关系时，则接受此广播，取得 Registration ID 并保存与App uid 的关系到开发者自己的应用服务器上。
//            使用极光推送提供的别名与标签功能，是更加简单轻便的绑定App用户与JPush用户的方式

            String regID = intent.getExtras().getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regID);
            //在此处做redID和UserID进行对应
            //。。。

        }else  if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){
//            功能描述:
//            收到了自定义消息 Push 。
//            SDK 对自定义消息，只是传递，不会有任何界面上的展示。
//            如果开发者想推送自定义消息 Push，则需要在 AndroidManifest.xml 里配置此 Receiver action，并且在自己写的 BroadcastReceiver 里接收处理。
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Toast.makeText(context,"title:" + title + ",Message:" + message, Toast.LENGTH_SHORT).show();


        }else if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
//            功能描述:
//            收到了通知 Push。
//            如果通知的内容为空，则在通知栏上不会展示通知。
//            但是，这个广播 Intent 还是会有。开发者可以取到通知内容外的其他信息。

        }else if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
//            功能描述:
//            用户点击了通知。 一般情况下，用户不需要配置此 receiver action。
//            如果开发者在 AndroidManifest.xml 里未配置此 receiver action，那么，SDK 会默认打开应用程序的主 Activity，相当于用户点击桌面图标的效果。
//            如果开发者在 AndroidManifest.xml 里配置了此 receiver action，那么，当用户点击通知时，SDK 不会做动作。开发者应该在自己写的 BroadcastReceiver 类里处理，比如打开某 Activity 。

        }else if(JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())){


        }else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())){
//            JPush 服务的连接状态发生变化。（注：不是指 Android 系统的网络连接状态。）

        }


//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//            //send the Registration Id to your server...
//
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
////            processCustomMessage(context, bundle);
////            handleMessage(context, bundle);
//        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
////            handleMessage(context, bundle);
//
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
//
//        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//
//        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
//        } else {
//            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//        }
    }
}
