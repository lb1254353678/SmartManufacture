package com.goodbaby.smartmanufacture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;
import com.goodbaby.smartmanufacture.model.User;
import com.goodbaby.smartmanufacture.service.UpdateService;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import java.util.HashMap;


/**
 * 登录activity
 *
 * @author XXZX_LB
 */
public class LoginActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private MyApplication myApplication;
    private TextView tv_version;
    private EditText et_username, et_psw;
    private Button btn_login;
    private CheckBox cb_remeberpsw, cb_autoLogin;
    private ProgressBar pb;
    private boolean flag_remember = true;
    private boolean flag_autoLogin = false;
    //private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ProgressDialog mProgressDialog;

    private HashMap<Integer, HashMap<Integer, String>> map;
    public String[] errorMessage = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        myApplication = (MyApplication) getApplication();
        initView();
        //当有网络的情况下，才进行版本匹配
        if (Utils.isNetworkAvailable(this)) {
            //checkUpdate();
            checkVersion();
        }
        isAutoLogin(myApplication.mUser);
    }

    private void initView() {

        tv_version = (TextView) findViewById(R.id.tv_version);
        et_username = (EditText) findViewById(R.id.input_name);
        et_psw = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        cb_remeberpsw = (CheckBox) findViewById(R.id.cb_remeberpsw);
        cb_autoLogin = (CheckBox) findViewById(R.id.cb_autologin);

        cb_remeberpsw.setOnCheckedChangeListener(this);
        cb_autoLogin.setOnCheckedChangeListener(this);
        btn_login.setOnClickListener(this);

        initValue();

    }

    /**
     * 初始化控件值
     */
    private void initValue() {
        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tv_version.setText(getString(R.string.login_version) + getCurVersionCode());

        if (myApplication.mUser != null) {
            et_username.setText(myApplication.mUser.getTel());
            if (myApplication.mUser.isRememberPsw()) {
                et_psw.setText(myApplication.mUser.getPassword());
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_remeberpsw:
                if (isChecked) {
                    flag_remember = true;
                } else {
                    flag_remember = false;
                }
                break;
            case R.id.cb_autologin:
                if (isChecked) {
                    flag_autoLogin = true;
                } else {
                    flag_autoLogin = false;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (et_username.getText().toString().trim().equals("18912688100")
                        && et_psw.getText().toString().trim().equals("123456")) {
                    rememberLoginInfo("18912688100", "123456");
                    toGroupNameSelectedActivity();
                } else {
                    rememberLoginInfo(et_username.getText().toString().trim(), et_psw.getText().toString().trim());
                    toGroupNameSelectedActivity();
                }

                break;
        }
    }


    /**
     * 登录事件方法
     *
     * @param username
     * @param password
     */
    @SuppressLint("NewApi")
    private void login(final String username, final String password) {
        errorMessage[0] = "";
        final String sqlText = "SELECT * FROM dbo.AppUserLoginAuthorization WHERE Tel = '" + username + "' AND Password = '" + password + "'";
        if (username.isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.toast_login_name_null), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.toast_login_psw_null), Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * 设置progressDialog的基本样式
         */
        mProgressDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setMessage(getString(R.string.login_progressing));
        mProgressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //模拟1秒耗时操作，防止progressDialog的出现消失动作过快
                    Utils.spandTimeMethod();
                    if (Utils.isNetworkAvailable(LoginActivity.this)) {
                        String[] result = NetWorkUtil.login(username, password, getString(R.string.app_name), errorMessage);
                        if (result != null && result.length != 0) {
                            rememberLoginInfo(username, password);
                            myHandler.sendEmptyMessage(Constant.SUCCESS);
                        } else {
                            myHandler.sendEmptyMessage(Constant.FAILED);
                        }
                    } else {
                        myHandler.sendEmptyMessage(Constant.NO_NETWORK);
                    }
                } catch (Exception e) {
                    myHandler.sendEmptyMessage(Constant.EXCEPTION);
                }
            }


        }).start();
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_login_success), Toast.LENGTH_SHORT).show();
                    toGroupNameSelectedActivity();
                    break;
                case Constant.FAILED:
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_login_failed), Toast.LENGTH_SHORT).show();
                    break;
                case Constant.EXCEPTION:
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_login_exception), Toast.LENGTH_SHORT).show();
                    break;
                case Constant.NO_NETWORK:
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_login_no_network), Toast.LENGTH_SHORT).show();
                    break;
                case Constant.UPDATE:
                    update();
                    break;
            }
        }
    };

    /**
     * 记录登录者信息方法
     *
     * @param username
     * @param password
     */
    private void rememberLoginInfo(final String username, final String password) {
        User user = new User();
        user.setTel(username);
        user.setPassword(password);
        user.setLoginTime(Utils.getCurSysTime());
        user.setAutoLogin(flag_autoLogin);
        user.setRememberPsw(flag_remember);
        myApplication.writeLoginInfo(user);
    }

    /**
     * 判断自动登录方法
     *
     * @param user
     */
    private void isAutoLogin(User user) {
        //已登录，并且勾选自动登录
        if (user != null) {
            /**
             * 当勾选自动登录并且距离上次登录时间不超过30分钟
             */
            if (user.isAutoLogin() && myApplication.mUser.getLoginTime().compareTo(Utils.getPreHalfHourSysTime()) >= 0) {
                toGroupNameSelectedActivity();
                myApplication.mUser.setLoginTime(Utils.getCurSysTime());
            } else {
                initView();
            }
        } else {
            initView();
        }
    }

    /**
     * 跳转到MainActivity方法
     */
    private void toMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到MainViewPagerActivity方法
     */
    private void toMainViewPagerActivity() {
        Intent intent = new Intent(LoginActivity.this, MainViewPagerActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到GroupNameSelectedActivity方法
     */
    private void toGroupNameSelectedActivity() {
        Intent intent = new Intent(LoginActivity.this, GroupNameSelectedActivity.class);
        startActivity(intent);
        finish();
    }

    //利用"蒲公英"进行版本检测
    private void checkVersion() {
        PgyUpdateManager.register(LoginActivity.this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                //Toast.makeText(LoginActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateAvailable(String result) {
                final AppBean appBean = getAppBeanFromString(result);
                View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.view_popup,null);
                final AlertDialog myDialog = new AlertDialog.Builder(LoginActivity.this).create();
                myDialog.show();
                myDialog.getWindow().setContentView(view);

                pb = (ProgressBar)view.findViewById(R.id.progressBar);
                TextView tv_cancel = (TextView)view.findViewById(R.id.tv_cancel);
                TextView tv_submit = (TextView)view.findViewById(R.id.tv_submit);

                final TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
                tv_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDownloadTask(LoginActivity.this,appBean.getDownloadURL());
                        //Log.v("&&&&&&","url:" + appBean.getDownloadURL());
                        myDialog.dismiss();

                    }
                });

                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

            }
        });
    }



    /**
     * 获取当前版本号
     *
     * @return verCode
     */
    private String getCurVersionCode() {
        String verCode = "";
        try {
            verCode = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName.toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return verCode;

    }

    /**
     * 判断是否要更新
     *
     * @return
     */
//    private void checkUpdate() {
//        //当前版本号小于服务器版本号
//        final String sqlText = Constant.sql_sysVersion;
//        map = new HashMap<Integer, HashMap<Integer, String>>();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                map = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_SYSLOG, sqlText, errorMessage);
//                if (map != null && map.size() != 0) {
//                    if (getCurVersionCode() < Integer.valueOf(map.get(0).get(2))) {
//                        myHandler.sendEmptyMessage(Constant.UPDATE);
//                    }
//                }
//            }
//        }).start();
//    }

    /**
     * update APP方法
     */
    private void update() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.version_update_title))
                .setMessage(getString(R.string.version_update_message))
                .setPositiveButton(getString(R.string.version_update_cancel), null)
                .setNegativeButton(getString(R.string.version_update_submit), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(LoginActivity.this, UpdateService.class);
                        startService(intent);
                    }
                }).show();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}
