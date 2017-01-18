package com.goodbaby.push.JPush;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.goodbaby.push.application.GBApplication;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by goodbaby on 17/1/18.
 */

public class JPushUtil {
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAG = 1002;
    private final static Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SET_ALIAS:
                    Log.d("flo", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(GBApplication.getAppContext().getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                case MSG_SET_TAG:
                    Log.d("flo", "Set tag in handler.");
                    // 调用 JPush 接口来设置标签。
                    JPushInterface.setAliasAndTags(GBApplication.getAppContext().getApplicationContext(), null, (Set<String>) msg.obj, mTagCallBack);
                    break;
                default:
                    Log.i("flo", "Unhandled msg - " + msg.what);
            }
        }
    };

    /*
    * 初始化推送服务
    * */
    public static void initJPush(Context context) {
        JPushInterface.init(context);
        Log.d("flo", "init JPush");
    }

    /**
     * 设置设备的别名
     *
     * @param context 上下文
     * @param alias 别名
     */
    public static void initJPushAlias(final Context context, String alias) {

        initJPush(context);
        //JPushInterface.setAliasAndTags(context.getApplicationContext(), alias, null, mAliasCallback);
        JPushInterface.setAlias(context.getApplicationContext(), alias, mAliasCallback);
    }

    /**
     * 设置设备的标签
     *
     * @param context 上下文
     * @param tag 标签
     */
    public static void initJPushTag(final Context context, HashSet<String> tag) {
        if (tag == null) {
            return;
        }
        initJPush(context);
        //JPushInterface.setAliasAndTags(context, null, tag, mTagCallBack);
        JPushInterface.setTags(context, tag, mTagCallBack);
    }


    private final static TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String s, Set<String> set) {
            Log.e("flo", "call back executing....");
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("flo", logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 30s.";
                    Log.i("flo", logs);
                    myHandler.sendMessageDelayed(myHandler.obtainMessage(MSG_SET_ALIAS, s), 1000 * 30);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("flo", logs);
            }
        }
    };

    private final static TagAliasCallback mTagCallBack = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String s, Set<String> set) {
            Log.e("flo", "call back executing....");
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("flo", logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 30s.";
                    Log.i("flo", logs);
                    myHandler.sendMessageDelayed(myHandler.obtainMessage(MSG_SET_TAG, s), 1000 * 30);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("flo", logs);
            }
        }
    };



    public static void stopJPush(Context context) {
        JPushInterface.stopPush(context);
        Log.d("flo", "stop JPush");
    }

    public static void resumeJPush(Context context) {
        JPushInterface.resumePush(context);
        Log.d("flo", "resume JPush");
    }

}
