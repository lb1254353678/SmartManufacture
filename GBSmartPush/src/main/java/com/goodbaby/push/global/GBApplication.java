package com.goodbaby.push.global;

import android.app.Application;
import android.content.SharedPreferences;

import com.goodbaby.push.R;
import com.goodbaby.push.model.User;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by goodbaby on 17/1/18.
 */
public class GBApplication extends Application {
    private static GBApplication mInstance;
    public User mUser;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        initJPush();
    }


    public static GBApplication getAppContext() {
        return mInstance;
    }

    /*
    * 初始化JPush
    * */
    public void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
    }

    public void initLoginUser(User user){
        String account = user.getID();
        String password = user.getPassword();
        boolean remPsw = user.isRem_psw();
        boolean autoLogin = user.isAutoLogin();

        editor = sharedPreferences.edit();
        editor.putString(Constant.ACCOUNT,account);
        editor.putString(Constant.PASSWORD,password);
        editor.putBoolean(Constant.REM_PSW,remPsw);
        editor.putBoolean(Constant.AUTO_LOGIN,autoLogin);
        editor.commit();
    }

    public User loginUser(){
        String account = sharedPreferences.getString(Constant.ACCOUNT,null);
        String password = sharedPreferences.getString(Constant.PASSWORD,null);
        boolean remeberPsw = sharedPreferences.getBoolean(Constant.REM_PSW, false);
        boolean autoLogin = sharedPreferences.getBoolean(Constant.AUTO_LOGIN,false);
        if(account != null){
            mUser = new User();
            mUser.setID(account);
            mUser.setPassword(password);
            mUser.setRem_psw(remeberPsw);
            mUser.setAutoLogin(autoLogin);
        }
        return mUser;
    }



}

