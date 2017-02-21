package com.goodbaby.smartmanufacture.global;


import android.app.Application;
import android.content.SharedPreferences;

import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.model.User;
import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.bugly.crashreport.CrashReport;


/**
 *
 * @author XXZX_LB
 *
 */
public class MyApplication extends Application{
    public static String XBID;
    public static String XBNAME;
    public SharedPreferences mSharePreferences;
    public SharedPreferences.Editor editor;
    public User mUser = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mSharePreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        lastLoginInfo();

        CrashReport.initCrashReport(getApplicationContext(), "fbc984938f", false);//腾讯Bugly Crash模块
        PgyCrashManager.register(getApplicationContext());//蒲公英Crash模块

    }

    public void lastLoginInfo(){
        String tel = mSharePreferences.getString(Constant.TEL,null);
        String password = mSharePreferences.getString(Constant.PASSWORD,null);
        String appname = mSharePreferences.getString(Constant.APPNAME,null);
        String userid = mSharePreferences.getString(Constant.USERID,null);
        String isvalid = mSharePreferences.getString(Constant.ISVALID, null);
        String remark = mSharePreferences.getString(Constant.REMARK,null);
        String logintime = mSharePreferences.getString(Constant.LOGINTIME,null);

        boolean autologin = mSharePreferences.getBoolean(Constant.AUTOLOGIN, false);
        boolean rememberpsw = mSharePreferences.getBoolean(Constant.REMEMBERPSW, false);

        if(tel != null){
            mUser = new User();
            mUser.setTel(tel);
            mUser.setPassword(password);
            mUser.setUserid(userid);
            mUser.setAppname(appname);
            mUser.setIsvalid(isvalid);
            mUser.setRemark(remark);
            mUser.setLoginTime(logintime);
            mUser.setAutoLogin(autologin);
            mUser.setRememberPsw(rememberpsw);
        }
    }

    /**
     * 记录登录信息
     * @param user
     */
    public void writeLoginInfo(User user){
        mUser = user;
        editor = mSharePreferences.edit();
        editor.putString(Constant.TEL, mUser.getTel());
        editor.putString(Constant.PASSWORD, mUser.getPassword());
        editor.putString(Constant.APPNAME, mUser.getAppname());
        editor.putString(Constant.USERID, mUser.getUserid());
        editor.putString(Constant.ISVALID, mUser.getIsvalid());
        editor.putString(Constant.REMARK, mUser.getRemark());
        editor.putString(Constant.LOGINTIME, mUser.getLoginTime());
        editor.putBoolean(Constant.AUTOLOGIN, mUser.isAutoLogin());
        editor.putBoolean(Constant.REMEMBERPSW, mUser.isRememberPsw());
        editor.commit();
    }


}
