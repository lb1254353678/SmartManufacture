package com.goodbaby.smartmanufacture.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;

import java.util.HashMap;

public class LastNewsService extends Service{

	public String[] errorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> map;
	private Intent intent = new Intent(Constant.LAST_NEWS_RECIVER);
	private String mLastNew;
	private boolean flag = true;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		//return myBinder;
		return null;
	}



	//Service被创建时回调
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(!Utils.isNetworkAvailable(getApplicationContext())){
			myHandler.sendEmptyMessage(Constant.NO_NETWORK);
			return;
		}
		final String sqlText = Constant.sql_lastnews(MyApplication.XBID);
		//Log.e("********", ""+sqlText);
		errorMessage[0] = "";
		map = new HashMap<Integer, HashMap<Integer,String>>();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (flag){
					try{
						map = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, errorMessage);
						//Log.e("******","map:" + map);
						if(map != null){
							myHandler.sendEmptyMessage(Constant.SUCCESS);
						}else{
							myHandler.sendEmptyMessage(Constant.FAILED);
						}
					}catch(Exception e){
						myHandler.sendEmptyMessage(Constant.EXCEPTION);
					}

					try {
						Thread.sleep(Constant.DELAYTIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();

	}
	//Service被启动时回调
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	//Service被关闭时回调
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;

	}

	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case Constant.SUCCESS:
					try {
						mLastNew = map.get(0).get(5).toString();
					}catch (Exception e){
						mLastNew = "";
					}
					intent.putExtra("lastnews",mLastNew);
					sendBroadcast(intent);
					break;
				case Constant.FAILED:
					mLastNew = "获取最新资讯出错!";
					intent.putExtra("lastnews",mLastNew);
					sendBroadcast(intent);
					break;
				case Constant.NO_NETWORK:
					mLastNew = "暂无网络,请确认网络设置后重试。";
					intent.putExtra("lastnews",mLastNew);
					sendBroadcast(intent);
					break;

			}
		}
	};



}
