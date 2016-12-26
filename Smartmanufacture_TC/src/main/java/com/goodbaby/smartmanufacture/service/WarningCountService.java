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

public class WarningCountService extends Service{
	private Intent intent = new Intent(Constant.WARNING_COUNT_RECIVER);
	private boolean flag = true;
	private String[] errorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> map;
	private String[] warningCount = new String[5];//实际


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	//Service被创建时回调
	@Override
	public void onCreate() {
		// TODO Auto-generated method stubs
		super.onCreate();
	}
	//Service被启动时回调
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		getWainingCount();
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
	 * 获取异常统计
	 */
	public void getWainingCount(){
		final String sqlText = Constant.sql_warning_count(MyApplication.XBID);
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
							map = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, errorMessage);
							//Log.v("#########","map:" + map);
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
					/**
					 * map值顺序：
					 * 警告次数-质量异常次数--节拍异常
					 * 警告次数-当日累计
					 * 警告次数-最近一小时累计
					 * 警告次数-质量异常次数--物料异常
					 * 警告次数-质量异常次数--质量异常
					 */

					try{
						warningCount[0] = map.get(2).get(4);//最近小时
						warningCount[1] = map.get(1).get(4);//当日累计
						warningCount[2] = map.get(4).get(4);//质量异常
						warningCount[3] = map.get(3).get(4);//物料异常
						warningCount[4] = map.get(0).get(4);//节拍异常

					}catch(Exception e){
						warningCount[0] = "0";//最近小时
						warningCount[1] = "0";//当日累计
						warningCount[2] = "0";//质量异常
						warningCount[3] = "0";//物料异常
						warningCount[4] = "0";//节拍异常
					}
					intent.putExtra("warningCount", warningCount);
					sendBroadcast(intent);
					break;

				case Constant.FAILED:
				case Constant.EXCEPTION:
					warningCount[0] = "0";//最近小时
					warningCount[1] = "0";//当日累计
					warningCount[2] = "0";//质量异常
					warningCount[3] = "0";//物料异常
					warningCount[4] = "0";//节拍异常
					intent.putExtra("warningCount", warningCount);
					sendBroadcast(intent);
					break;

				case Constant.DEBUG:
					warningCount[0] = "0";//最近小时
					warningCount[1] = "0";//当日累计
					warningCount[2] = "0";//质量异常
					warningCount[3] = "0";//物料异常
					warningCount[4] = "0";//节拍异常
					intent.putExtra("warningCount", warningCount);
					sendBroadcast(intent);
					break;
			}

		}
	};



}
