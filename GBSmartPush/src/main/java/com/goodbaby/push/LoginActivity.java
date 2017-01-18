package com.goodbaby.push;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.goodbaby.push.JPush.JPushUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_loginName,et_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {
        et_loginName = (EditText)findViewById(R.id.input_name);
        et_password = (EditText)findViewById(R.id.input_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String strAlias = et_loginName.getText().toString().trim();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                JPushUtil.initJPushAlias(LoginActivity.this,strAlias);//设置JPush的别名,即用户登录账号
                break;
        }
    }
}
