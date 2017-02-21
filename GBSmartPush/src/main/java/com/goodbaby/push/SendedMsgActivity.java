package com.goodbaby.push;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.goodbaby.push.global.Constant;
import com.goodbaby.push.global.GBApplication;
import com.goodbaby.push.model.ResponseUpdateModel;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendedMsgActivity extends AppCompatActivity implements View.OnClickListener{

    private GBApplication gbApplication;
    private OkHttpClient mOkHttpClient;
    private Toolbar mToolbar;
    private EditText et_to_user,et_content;
    private Button btn_send;
    private ProgressBar mProgressBar;
    private String errorStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

        gbApplication = (GBApplication)getApplication();
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        et_to_user = (EditText) findViewById(R.id.et_to_user);
        et_content = (EditText) findViewById(R.id.et_content);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("SendedMsgActivity");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                String to_user = et_to_user.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                mProgressBar.setVisibility(View.VISIBLE);
                sendMsg(gbApplication.mUser.getID(), to_user, content);
                break;
        }
    }

    private void sendMsg(String from_user, String to_user, String message){
        if(to_user.isEmpty()){
            Toast.makeText(SendedMsgActivity.this,getString(R.string.send_msg_to_user_null),Toast.LENGTH_SHORT).show();
            return;
        }
        if(message.isEmpty()){
            Toast.makeText(SendedMsgActivity.this,getString(R.string.send_msg_content_null),Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("from_user",from_user)
                                                        .add("to_user",to_user)
                                                        .add("message",message).build();
        Request request = new Request.Builder().url(Constant.URL_SENDTO).post(requestBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                myHandler.sendEmptyMessageDelayed(Constant.RESPONSE_FAILURE,1000);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Message msg = Message.obtain();
                msg.obj = response.body().string();
                msg.what = Constant.RESPONSE_SUCCESS;
                myHandler.sendMessage(msg);
            }
        });
    }

     Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.RESPONSE_FAILURE:
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(SendedMsgActivity.this, getString(R.string.error_cannot_connection_server),Toast.LENGTH_SHORT).show();
                    break;
                case Constant.RESPONSE_SUCCESS:
                    mProgressBar.setVisibility(View.GONE);
                    String responseStr = msg.obj.toString().trim();
                    Log.e("*****",responseStr);
                    Gson gson = new Gson();
                    ResponseUpdateModel responseUpdateModel = gson.fromJson(responseStr, ResponseUpdateModel.class);
                    if(responseUpdateModel.getState().equals("NO")){
                        Toast.makeText(SendedMsgActivity.this, getString(R.string.error_send_failed),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), getString(R.string.send_success), Toast.LENGTH_SHORT).show();
                        et_to_user.setText("");
                        et_content.setText("");
                    }
                    break;
            }
        }
    };
}
