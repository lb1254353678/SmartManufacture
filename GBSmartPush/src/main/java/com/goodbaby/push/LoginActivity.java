package com.goodbaby.push;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodbaby.push.JPush.JPushUtil;
import com.goodbaby.push.custom.GBProgressDialog;
import com.goodbaby.push.global.Constant;
import com.goodbaby.push.global.GBApplication;
import com.goodbaby.push.model.ResponseUser;
import com.goodbaby.push.model.User;
import com.goodbaby.push.util.JsonUtil;
import com.goodbaby.push.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private GBApplication gbApplication;
    private OkHttpClient mOkHttpClient;

    private EditText et_loginName,et_password;
    private Button btn_login;
    private TextView tv_link_error_login;
    private CheckBox cb_remeberPsw,cb_autoLogin;
    private boolean flag_remeber = true;
    private boolean flag_autoLogin = false;
    private GBProgressDialog dialog;

    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gbApplication = (GBApplication)getApplication();
        isAutoLogin(gbApplication.loginUser());

        init();
    }

    private void init() {
        et_loginName = (EditText)findViewById(R.id.input_name);
        et_password = (EditText)findViewById(R.id.input_password);
        cb_remeberPsw = (CheckBox)findViewById(R.id.cb_remeberpsw);
        cb_autoLogin = (CheckBox)findViewById(R.id.cb_autologin);
        btn_login = (Button)findViewById(R.id.btn_login);
        tv_link_error_login = (TextView)findViewById(R.id.link_error_login);
        tv_link_error_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_link_error_login.getPaint().setAntiAlias(true);
        cb_remeberPsw.setOnCheckedChangeListener(this);
        cb_autoLogin.setOnCheckedChangeListener(this);
        btn_login.setOnClickListener(this);
        tv_link_error_login.setOnClickListener(this);
        initEditTextValue();

//        if(checkVersion()){
//            downloadLastApp();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                account = et_loginName.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(!Utils.isNetworkAvailable(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this, getString(R.string.error_no_network),Toast.LENGTH_SHORT).show();
                }else{
                    if(validate(account, password)){
                        dialog = new GBProgressDialog(LoginActivity.this,R.style.gbProgressDialog);
                        dialog.show();
                        login(account, password);
                    }
                }
                break;
            case R.id.link_error_login:
                Intent intent = new Intent(LoginActivity.this, LoginErrorDescActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_remeberpsw:
                if(isChecked){
                    flag_remeber = true;
                }else{
                    flag_remeber = false;
                }
                break;
            case R.id.cb_autologin:
                if(isChecked){
                    flag_autoLogin = true;
                }else{
                    flag_autoLogin = false;
                }
                break;
        }
    }

    /**
     * 检测版本是否需要更新
     * @return
     */
    private boolean checkVersion(){
        boolean flag = false;
        if(Utils.isNetworkAvailable(LoginActivity.this)){
            if(Utils.getCurVersionCode(LoginActivity.this) == 0){
                flag = false;
            }else{
                if(Utils.getCurVersionCode(LoginActivity.this) > 1){
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 检验用户名和密码是否为空
     * @param account
     * @param password
     * @return
     */
    private boolean validate(String account, String password){
        boolean flag = true;
        if(account.isEmpty()){
            Toast.makeText(LoginActivity.this, getString(R.string.toast_login_name_null),Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(password.isEmpty()){
            Toast.makeText(LoginActivity.this, getString(R.string.toast_login_psw_null),Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    /**
     * 将用户名和RegistractionID写到DB保存
     * @param userName
     * @param registractionID
     */
    private void connUserNameAndRegistractionID(String userName, String registractionID){
        mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("user_name",userName)
                                                        .add("registractionID",registractionID)
                                                        .build();
        Request request = new Request.Builder().url(Constant.URL_CONNUSERNAMEANDREGISTRACTIONID)
                                                .post(requestBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    /**
     * 下载最新版本App
     */
    private void downloadLastApp(){
        mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("").build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                FileOutputStream fos = null;
                fos = new FileOutputStream(new File("sdcard/GBProject"));//输出位置
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer))!= -1){
                    fos.write(buffer,0,len);
                }
                fos.flush();
            }
        });
    }

    /**
     * 登录
     * 登录成功时,记录登录状态
     * @param account
     * @param password
     */
    private void login(final String account, final String password){

        mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("account",account)
                .add("password",Utils.MD5(password))
                .build();
        Request request = new Request.Builder().url(Constant.URL_LOGIN)
                .post(requestBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                myHandler.sendEmptyMessage(Constant.RESPONSE_FAILURE);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Message msg = Message.obtain();
                msg.obj =response.body().string();
                msg.what = Constant.RESPONSE_SUCCESS;
                myHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 判断是否自动登录
     * @param user
     */
    private void isAutoLogin(User user){
        //已登录，并且勾选自动登录
        if(user != null){
            //自动登录
            if(user.isAutoLogin()){
                toMainActivity();
            }else{
                init();
            }
        }else{
            init();
        }
    }

    /**
     * 初始化用户名和密码控件
     */
    private void initEditTextValue(){
        if(gbApplication.mUser != null){
            et_loginName.setText(gbApplication.mUser.getID());
            if(gbApplication.mUser.isRem_psw()){
                et_password.setText(gbApplication.mUser.getPassword());
            }
        }
    }

    /**
     * 跳转到MainActivity
     */
    private void toMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.RESPONSE_FAILURE:
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.error_cannot_connection_server), Toast.LENGTH_SHORT).show();
                break;
                case Constant.RESPONSE_SUCCESS:
                    dialog.dismiss();
                    String responseStr = msg.obj.toString().trim();
                    Log.e("**jsonStr**",""+responseStr);

                    ResponseUser responseUser = JsonUtil.fromJson(responseStr, ResponseUser.class);
                    if(responseUser.getState().equals("NO")){
                        Toast.makeText(LoginActivity.this, getString(R.string.error_login_fail),Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                        //设置JPush的别名,即用户登录账号对应的EmpID作为别名(因为ERP账号不一定对应EmpID)
                        JPushUtil.initJPushAlias(LoginActivity.this,responseUser.getUser().getEmpID());
                        User mUser = new User();
                        mUser.setID(account);
                        mUser.setPassword(password);
                        mUser.setRem_psw(flag_remeber);
                        mUser.setAutoLogin(flag_autoLogin);
                        gbApplication.initLoginUser(mUser);

                    }
                break;
            }
        }
    };


}