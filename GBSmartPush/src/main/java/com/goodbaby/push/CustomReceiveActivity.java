package com.goodbaby.push;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by goodbaby on 17/1/24.
 */

public class CustomReceiveActivity extends AppCompatActivity {

    private TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);


//        tv = (TextView)findViewById(R.id.content_main).findViewById(R.id.tv);
//        Intent intent = getIntent();
//        if (null != intent) {
//            Bundle bundle = getIntent().getExtras();
//            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//            tv.setText("自定义activity接收处理push消息页面\nTitle : " + title + "  " + "Content : " + content);
//        }
    }
}
