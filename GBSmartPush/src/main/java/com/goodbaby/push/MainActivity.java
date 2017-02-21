package com.goodbaby.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.goodbaby.push.adapter.MyRecycleViewAdapter;
import com.goodbaby.push.global.Constant;
import com.goodbaby.push.global.GBApplication;
import com.goodbaby.push.model.PushMessageModel;
import com.goodbaby.push.model.ResponsePushMessageModel;
import com.goodbaby.push.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private GBApplication gbApplication;
    private OkHttpClient mOkHttpClient;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private FloatingActionButton mFab;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwiperefreshLayout;
    private RecyclerView mRecyclerView;
    private MyRecycleViewAdapter adapter;
    private ImageView iv_setting;

    private String requestUrl = Constant.URL_UNDISPOSED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gbApplication  = (GBApplication)getApplication();
        init();
        initListener();
        initNaviHeader();
    }

    private void init() {
        mProgressBar = (ProgressBar)findViewById(R.id.mProgressBar);
        mSwiperefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRreshLayout);
        mSwiperefreshLayout.setOnRefreshListener(this);
        mSwiperefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright), getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light), getResources().getColor(android.R.color.holo_red_light));
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mFab = (FloatingActionButton)findViewById(R.id.fab_edit);
        mNavigationView = (NavigationView)findViewById(R.id.main_navigation);
        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, mToolbar, R.string.action_open,
                R.string.action_close);
        mDrawerToggle.syncState();
        mDrawerlayout.setDrawerListener(mDrawerToggle);
    }

    private void initListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setCheckable(true);
                mDrawerlayout.closeDrawer(GravityCompat.START);//将Navigation隐藏
                mToolbar.setTitle(item.getTitle());//为Toolbar设置title值
                itemSelect(item);
                mProgressBar.setVisibility(View.VISIBLE);
                getMessage(requestUrl,gbApplication.mUser.getID());
                return true;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SendedMsgActivity.class);
                startActivity(intent);
            }
        });
        //startPushRequest(Constant.URL_PUSHMESSAGE, gbApplication.loginUser().getAccount());
        getMessage(Constant.URL_UNDISPOSED, gbApplication.loginUser().getID());
    }

    /**
     * 对NavigationView里面事件进行处理
     */
    private void initNaviHeader(){
        View headerView = mNavigationView.getHeaderView(0);
        //View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header);
        iv_setting = (ImageView) headerView.findViewById(R.id.iv_setting);
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 为RecyclerView赋值
     * @param context
     * @param list
     */
    private void setupRecycleView(Context context, List<PushMessageModel> list){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        adapter = new MyRecycleViewAdapter(context,list);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        myHandler.sendEmptyMessageDelayed(Constant.REQUEST_REFRESH,1000);
    }

    /**
     * 利用OkHttp进行网络请求
     * 向服务器开启推送请求
     */
    private void startPushRequest(String url, String user){
        mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("user",user).build();
        Request request = new Request.Builder().post(requestBody).url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("******", "---" + response.cacheResponse());
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.e("******", "cache---" + str);
                } else {
                    //response.body().string();
                    Log.i("***str***", "" + response.body().string());
                    String str = response.networkResponse().toString();
                    Log.e("******", "network---" + str);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 根据NavigationItem来获取数据
     * @param url
     * @param user
     */
    private void getMessage(String url, String user){
        mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                                                .add("user",user)
                                                .build();
        Request request = new Request.Builder()
                                        .url(url)
                                        .post(requestBody)
                                        .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                myHandler.sendEmptyMessage(Constant.RESPONSE_FAILURE);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
//                if (null != response.cacheResponse()) {
//                    String str = response.cacheResponse().toString();
//                    Log.e("******", "cache---" + str);
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
//                            try {
//                                String str = response.body().string();//.string()只能调用一次.否则报错
//                                Log.e("######",str);
//                                Gson gson = new Gson();
//                                List<PushMessageModel> list = gson.fromJson(str, new TypeToken<ArrayList<PushMessageModel>>(){}.getType());
//                                setupRecycleView(MainActivity.this,list);
//                            }catch (IOException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
                Message msg = Message.obtain();
                msg.obj = response.body().string();
                msg.what = Constant.RESPONSE_SUCCESS;
                myHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 根据选中的item来返回请求url
     * @param item
     * @return
     */
    private String itemSelect(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_undispose:
                requestUrl = Constant.URL_UNDISPOSED;
                break;
            case R.id.nav_dispose:
                requestUrl = Constant.URL_DISPOSED;
                break;
            case R.id.nav_send:
                requestUrl = Constant.URL_SENDED;
                break;
        }
        return requestUrl;
    }


    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.REQUEST_REFRESH:
                    getMessage(requestUrl, gbApplication.mUser.getID());
                    adapter.notifyDataSetChanged();
                    mSwiperefreshLayout.setRefreshing(false);
                    break;
                case Constant.RESPONSE_FAILURE:
                    Toast.makeText(MainActivity.this, getString(R.string.error_cannot_connection_server),Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case Constant.RESPONSE_SUCCESS:
                    List<PushMessageModel> list = new ArrayList<PushMessageModel>();
                    String resposeStr = msg.obj.toString().trim();
                    Log.e("****",resposeStr);

                    ResponsePushMessageModel responsePushMessageModel = JsonUtil.fromJson(resposeStr, ResponsePushMessageModel.class);
                    if(responsePushMessageModel.getState().equals("NO")){
                        Toast.makeText(MainActivity.this,"No Data",Toast.LENGTH_SHORT).show();
                        /**
                         * 可在此处做数据为空是的提示
                         */
                    }else{
                        list =  responsePushMessageModel.getList();
                    }
                    setupRecycleView(MainActivity.this,list);
                    mProgressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };
}
