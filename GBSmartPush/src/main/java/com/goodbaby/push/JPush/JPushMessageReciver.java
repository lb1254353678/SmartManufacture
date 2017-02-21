package com.goodbaby.push.JPush;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.goodbaby.push.CustomReceiveActivity;
import com.goodbaby.push.MainActivity;
import com.goodbaby.push.R;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by goodbaby on 17/1/18.
 */

public class JPushMessageReciver extends BroadcastReceiver {

    private String TAG = "*JPushMessageReciver*";
    private NotificationManager nm;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
        if(JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())){
//            功能描述:
//            SDK 向 JPush Server 注册所得到的注册 ID 。
//            一般来说，可不处理此广播信息。
//            要深入地集成极光推送，开发者想要自己保存App用户与JPush 用户关系时，则接受此广播，取得 Registration ID 并保存与App uid 的关系到开发者自己的应用服务器上。
//            使用极光推送提供的别名与标签功能，是更加简单轻便的绑定App用户与JPush用户的方式
            String regID = intent.getExtras().getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regID);
            //在此处做regID和UserID进行对应

        }else if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){
//            功能描述:
//            收到了自定义消息 Push 。
//            SDK 对自定义消息，只是传递，不会有任何界面上的展示。
//            如果开发者想推送自定义消息 Push，则需要在 AndroidManifest.xml 里配置此 Receiver action，并且在自己写的 BroadcastReceiver 里接收处理。
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Log.e("######","title:" + title +",message:" + message + ",extras" + extras);
//            try {
//                JSONObject object = new JSONObject(extras);
//                String lightColor = object.getString("lightcolor");
//                Log.e("######","lightColor:" + lightColor);
//            }catch (JSONException e){
//                Log.e("######","error" + e.getMessage());
//            }

            processCustomMessage(context, title, message, extras);

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

            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Log.e(TAG, "title:" + title +",message:" + message );

            //点击跳转到自定义的activity里进行处理
//            Intent i = new Intent(context, CustomReceiveActivity.class);
//            i.putExtras(bundle);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);

        }else if(JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION.equals(intent.getAction())){
//            用户点击了通知栏中自定义的按钮。(SDK 3.0.0 以上版本支持)
//            使用普通通知的开发者不需要配置此 receiver action。
//            只有开发者使用了 MultiActionsNotificationBuilder 构建携带按钮的通知栏的通知时，可通过该 action 捕获到用户点击通知栏按钮的操作，并自行处理。

        }else if(JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())){

            Log.e(TAG, "ACTION_RICHPUSH_CALLBACK");
        }else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())){
//            JPush 服务的连接状态发生变化。（注：不是指 Android 系统的网络连接状态。）
            Log.e(TAG, "ACTION_CONNECTION_CHANGE");
        }
    }

    /**
     * 自定义消息处理方法
     *
     * 为了显示通知，标题、文字、小图标是强制性必须要设置的
     * 当没有使用 setLargeIcon 方法，而使用了 setSmallIcon 方法，默认情况下，使用圆形图片
     * 当使用了 setLargeIcon 方法时，要用圆形图片时则要手动设置,否则将显示大方图 + 右下角小圆图
     * @param context
     * @param title
     * @param message
     */
    private void processCustomMessage(Context context,String title, String message ,String extras){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.ic_logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_logo));
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        builder.setCategory(Notification.CATEGORY_CALL);
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("lightColor", parseExtras(extras));
        PendingIntent pIntent = PendingIntent.getActivity(context, 1, intent, 0);
        builder.setContentIntent(pIntent);
        //这句是重点
        builder.setFullScreenIntent(pIntent,true);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        nm.notify(6,notification);
    }

    /**
     * 解析服务器传过来的Extras的值
     * @param extras
     * @return
     */
    private String parseExtras(String extras){
        String lightColor = "";
        try {
            JSONObject object = new JSONObject(extras);
            lightColor = object.getString("lightColor");
        }catch (JSONException e){
            lightColor = "";
        }
        return lightColor;
    }
}
