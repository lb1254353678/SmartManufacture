package com.goodbaby.smartmanufacture.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;

import java.util.HashMap;

public class ModelService extends Service{
	private Intent intent = new Intent(Constant.MODEL_RECIVER);
	private boolean flag = true;
	private String[] errorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> map;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	//Service被创建时回调
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	//Service被启动时回调
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		getDeviceModel();
		return super.onStartCommand(intent, flags, startId);
	}

	//Service被关闭时回调
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;
	}

	public void getDeviceModel(){
		map = new HashMap<Integer, HashMap<Integer,String>>();
		//final String sqlText = "SELECT * FROM dbo.DSXML WHERE ValueName = 'DeviceModel'";
		final String sqlText = Constant.sql_devicemodel(MyApplication.XBID);
		errorMessage[0] = "";
		try{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (flag){
						try{
							map = NetWorkUtil.getData( Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, errorMessage);
							//Log.v(TAG,"map:" + map.get(0).get(4));
							if(map != null){
								myHandler.sendEmptyMessage(Constant.SUCCESS);
							}else{
								myHandler.sendEmptyMessage(Constant.FAILED);
							}
						}catch (Exception e){
							e.printStackTrace();
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:
					String model;
					try {
						model = map.get(0).get(4);
					}catch (Exception e){
						model = "NA";
					}
					intent.putExtra("model",model);
					sendBroadcast(intent);
					break;
				case Constant.FAILED:
					intent.putExtra("model","NA");
					sendBroadcast(intent);
					break;
				case Constant.DEBUG:
					intent.putExtra("model","NA");
					sendBroadcast(intent);
					break;
			}
		}
	};

}
