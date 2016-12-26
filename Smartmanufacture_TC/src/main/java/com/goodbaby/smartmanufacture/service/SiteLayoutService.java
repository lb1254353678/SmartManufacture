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


public class SiteLayoutService extends Service{

	private Intent intent = new Intent(Constant.SITE_STATUS_RECIVER);
	private boolean flag = true;
	private String[] errorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> map;
	private String[] siteStatusCode = new String[11];//11个站点的状态码

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
		getSiteStatus();
		return super.onStartCommand(intent, flags, startId);
	}

	//Service被关闭时回调
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;
	}

	/**
	 * 各站点状态
	 */
	public void getSiteStatus(){
		final String sqlText = Constant.sql_site_status(MyApplication.XBID);
		//Log.v("********", ""+sqlText);
		errorMessage[0] = "";
		map = new HashMap<Integer, HashMap<Integer,String>>();
		try{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (flag){
						try{
							map = NetWorkUtil.getData( Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, errorMessage);
							//Log.v(TAG,"map:" + map);
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
					try{
						siteStatusCode[0] = map.get(0).get(4);
						siteStatusCode[1] = map.get(1).get(4);
						siteStatusCode[2] = map.get(2).get(4);
						siteStatusCode[3] = map.get(3).get(4);
						siteStatusCode[4] = map.get(4).get(4);
						siteStatusCode[5] = map.get(5).get(4);
						siteStatusCode[6] = map.get(6).get(4);
						siteStatusCode[7] = map.get(7).get(4);
						siteStatusCode[8] = map.get(8).get(4);
						siteStatusCode[9] = map.get(9).get(4);
						siteStatusCode[10] = map.get(10).get(4);

					}catch(Exception e){
						siteStatusCode[0] = "0";
						siteStatusCode[1] = "0";
						siteStatusCode[2] = "0";
						siteStatusCode[3] = "0";
						siteStatusCode[4] = "0";
						siteStatusCode[5] = "0";
						siteStatusCode[6] = "0";
						siteStatusCode[7] = "0";
						siteStatusCode[8] = "0";
						siteStatusCode[9] = "0";
						siteStatusCode[10] = "0";
					}
					intent.putExtra("siteStatusCode", siteStatusCode);
					sendBroadcast(intent);
					break;

				case Constant.FAILED:
				case Constant.EXCEPTION:
					siteStatusCode[0] = "0";
					siteStatusCode[1] = "0";
					siteStatusCode[2] = "0";
					siteStatusCode[3] = "0";
					siteStatusCode[4] = "0";
					siteStatusCode[5] = "0";
					siteStatusCode[6] = "0";
					siteStatusCode[7] = "0";
					siteStatusCode[8] = "0";
					siteStatusCode[9] = "0";
					siteStatusCode[10] = "0";
					intent.putExtra("siteStatusCode", siteStatusCode);
					sendBroadcast(intent);
					break;

				case Constant.DEBUG:
					siteStatusCode[0] = "0";
					siteStatusCode[1] = "0";
					siteStatusCode[2] = "0";
					siteStatusCode[3] = "0";
					siteStatusCode[4] = "0";
					siteStatusCode[5] = "0";
					siteStatusCode[6] = "0";
					siteStatusCode[7] = "0";
					siteStatusCode[8] = "0";
					siteStatusCode[9] = "0";
					siteStatusCode[10] = "0";
					intent.putExtra("siteStatusCode", siteStatusCode);
					sendBroadcast(intent);
					break;
			}

		}
	};



}
