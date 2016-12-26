package com.goodbaby.smartmanufacture;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.goodbaby.smartmanufacture.adapter.MyTabPageAdapter;
import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.fragment.MainFragment;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainViewPagerActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private List<Fragment> list;

    private MyTabPageAdapter adapter;

    private boolean isSelect;//是否选中

    private List<String> titles = new ArrayList<String>();

    private HashMap<Integer, HashMap<Integer,String>> map;
    public static ArrayList<String> groupIDList,groupNameList;
    private String[] errorMessage = new String[1];
    private ProgressDialog mProgressDialog;
    private MyApplication myApplication;

    private List<String> xbid;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewpager);

        myApplication = (MyApplication) getApplication();
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        getGroupName();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    private void init() {
        list = new ArrayList<>();

        list.clear();

        for (int i = 0; i < titles.size(); i++) {
            MainFragment fragment = new MainFragment();
            list.add(fragment);
        }
        myApplication.XBID = groupIDList.get(0);
        myApplication.XBNAME = groupNameList.get(0);

        //添加Tab标题
        for (int i = 0; i < titles.size(); i++) {

            if (i == 0) {

                isSelect = true;

            } else {

                isSelect = false;
            }

            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)), isSelect);

            Log.i("**Android**", titles.get(i));

        }


        if (null == adapter)
            adapter = new MyTabPageAdapter(getSupportFragmentManager(), list, titles);

        viewPager.setAdapter(adapter);

        //设置缓存View的个数
        //viewPager.setOffscreenPageLimit(titles.size() - 1);

        //关联TabLayout和ViewPager
        tabLayout.setupWithViewPager(viewPager);

        //Tab设置适配器
        tabLayout.setTabsFromPagerAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myApplication.XBID = groupIDList.get(tab.getPosition());
                myApplication.XBNAME = groupNameList.get(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void getGroupName() {
        final String sqlText = Constant.sql_getGroupName;
        errorMessage[0] = "";
        /**
         * 设置progressDialog的基本样式
         */
        mProgressDialog = new ProgressDialog(MainViewPagerActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setMessage(getString(R.string.load_progressing));
        mProgressDialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Utils.spandTimeMethod(); //模拟耗时操作，防止progressDialog消失过快
                map = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, errorMessage);
                if(map != null){
                    myHandler.sendEmptyMessage(Constant.SUCCESS);
                }else{
                    myHandler.sendEmptyMessage(Constant.FAILED);
                };
            }
        }).start();
    }

    Handler myHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    groupIDList = new ArrayList<String>();
                    groupNameList = new ArrayList<String>();
                    for(int i=0;i<map.size();i++){
                        groupIDList.add(map.get(i).get(0));
                        groupNameList.add(map.get(i).get(1));
                        titles.add(map.get(i).get(1));
                    }
                    if(titles.size() <= 4){
                        tabLayout.setTabMode(TabLayout.MODE_FIXED);
                    }else{
                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    }

                    init();

                    mProgressDialog.dismiss();
                    break;

                case Constant.FAILED:
                    mProgressDialog.dismiss();
                    Toast.makeText(MainViewPagerActivity.this, "暂无线别", Toast.LENGTH_SHORT).show();
                    break;

            }
        };
    };

    public List<String> getXbid() {
        return xbid;
    }

    public void setXbid(List<String> xbid) {
        this.xbid = xbid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}