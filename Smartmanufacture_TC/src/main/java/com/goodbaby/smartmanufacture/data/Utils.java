package com.goodbaby.smartmanufacture.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

/**
 * Created by XXZX_LB on 2016/6/17.
 */
public class Utils {

    public Utils(){

    }

    /**
     * 模拟耗时操作1秒
     */
    public static void spandTimeMethod() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否有网络异常
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    /**
     * 获取当前系统时间
     * @return
     */
    public static String getCurSysTime(){
    	long lsysTime = System.currentTimeMillis();
        CharSequence curSysTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", lsysTime);
        return ""+curSysTime;
    }
    
    /**
     * 获取半小时前系统时间
     * @return
     */
    public static String getPreHalfHourSysTime(){
    	long lsysTime = System.currentTimeMillis() - (30 * 60 * 1000);
        CharSequence preSysTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", lsysTime);
        return ""+preSysTime;
    }


}
