package com.goodbaby.smartmanufacture.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;

import java.util.HashMap;

public class BarChartService extends Service{

	private Intent intent = new Intent(Constant.BAR_CHART_RECIVER);
	private boolean flag = true;

	int[] achieveRateReal = new int[3];//实际
	int[] achieveRateStandard = new int[3];//标准
	public String[] achieveRateErrorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> achieveRateMap;

	int[] productionStatisticsReal = new int[3];//实际
	int[] productionStatisticsStandard = new int[3];//标准
	public String[] productionStatisticsErrorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> productionStatisticsMap;

	int[] changeLineReal = new int[1];//实际
	int[] changeLineStandard = new int[1];//标准
	public String[] changeLineErrorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> changeLineMap;

	float[] pphReal = new float[1];//实际
	float[] pphStandard = new float[1];//标准
	public String[] pphErrorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> pphMap;


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
		//getWainingCount();
		getProductionStatistics();
		getEfficiencyStatistics();
		getChangeLine();
		getPPH();
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
	 * 获取生产统计的值
	 */
	public void getProductionStatistics(){
		final String sqlText = Constant.sql_ProductionStatistics(MyApplication.XBID);
		//Log.v("********", ""+sqlText);
		productionStatisticsErrorMessage[0] = "";
		productionStatisticsMap = new HashMap<Integer, HashMap<Integer,String>>();
		try{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (flag){
						try{
							productionStatisticsMap = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, productionStatisticsErrorMessage);
							//Log.v("*************","map:" + productionStatisticsMap);
							if(productionStatisticsMap != null){
								//myProductionStatisticsHandler.sendEmptyMessage(Constant.SUCCESS);
								myHandler.sendEmptyMessage(Constant.SUCCESS);
							}else{
								//myProductionStatisticsHandler.sendEmptyMessage(Constant.FAILED);
								myHandler.sendEmptyMessage(Constant.FAILED);
							}
						}catch(Exception e){
							//myProductionStatisticsHandler.sendEmptyMessage(Constant.EXCEPTION);
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

	/**
	 * 获取效率统计的值
	 */
	public void getEfficiencyStatistics(){
		final String sqlText = Constant.sql_EfficiencyStatistics(MyApplication.XBID);
		//Log.v("********", ""+sqlText);
		achieveRateErrorMessage[0] = "";
		achieveRateMap = new HashMap<Integer, HashMap<Integer,String>>();
		try{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (flag){
						try{
							achieveRateMap = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, achieveRateErrorMessage);
							//Log.v(TAG,"map:" + map);
							if(achieveRateMap != null){
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

	/**
	 * 获取changeLine的值
	 */
	public void getChangeLine(){
		final String sqlText = Constant.sql_ChangeLine(MyApplication.XBID);
		//Log.v("********", ""+sqlText);
		changeLineErrorMessage[0] = "";
		changeLineMap = new HashMap<Integer, HashMap<Integer,String>>();
		try{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					while (flag){
						try{
							changeLineMap = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, changeLineErrorMessage);
							//Log.v(TAG,"map:" + map);
							if(changeLineMap != null){
								//myChangeLineHandler.sendEmptyMessage(Constant.SUCCESS);
								myHandler.sendEmptyMessage(Constant.SUCCESS);
							}else{
								//myChangeLineHandler.sendEmptyMessage(Constant.FAILED);
								myHandler.sendEmptyMessage(Constant.FAILED);
							}
						}catch(Exception e){
							//myChangeLineHandler.sendEmptyMessage(Constant.EXCEPTION);
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

	/**
	 * 获取pph的值
	 */
	public void getPPH(){
		final String sqlText = Constant.sql_PPH(MyApplication.XBID);
		Log.v("********", ""+sqlText);
		pphErrorMessage[0] = "";
		pphMap = new HashMap<Integer, HashMap<Integer,String>>();
		try{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (flag){
						try{
							pphMap = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, pphErrorMessage);
							//Log.v(TAG,"map:" + map);
							if(pphMap != null){
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
			switch (msg.what){
				case Constant.SUCCESS:
					/**
					 * 计划数量
					 * 备用
					 * 产量统计-实际生产-当日累计
					 * 产量统计-实际生产-最近一小时累计
					 * 产量统计-标准-当日累计
					 * 产量统计-标准-最近一小时累计
					 */
					try{
						productionStatisticsReal[0] = Integer.valueOf(productionStatisticsMap.get(0).get(4));
						productionStatisticsReal[1] = Integer.valueOf(productionStatisticsMap.get(2).get(4));
						productionStatisticsReal[2] = Integer.valueOf(productionStatisticsMap.get(3).get(4));
						productionStatisticsStandard[0] = Integer.valueOf(productionStatisticsMap.get(1).get(4));
						productionStatisticsStandard[1] = Integer.valueOf(productionStatisticsMap.get(4).get(4));
						productionStatisticsStandard[2] = Integer.valueOf(productionStatisticsMap.get(5).get(4));
					}catch(Exception e){
						productionStatisticsReal[0] = 0;
						productionStatisticsReal[1] = 0;
						productionStatisticsReal[2] = 0;
						productionStatisticsStandard[0] = 0;
						productionStatisticsStandard[1] = 0;
						productionStatisticsStandard[2] = 0;
						//Toast.makeText(getActivity(),"产量统计图表数据解析出现异常",Toast.LENGTH_SHORT).show();
					}

					/**
					 * 生产效率统计-效率-当日累计
					 * 生产效率统计-效率-最近小时累计
					 * 计划达成率
					 * 直通率
					 */
					try{
						achieveRateReal[0] = Integer.valueOf(achieveRateMap.get(2).get(4));//达成率
						achieveRateReal[1] = Integer.valueOf(achieveRateMap.get(0).get(4));//效率
						achieveRateReal[2] = Integer.valueOf(achieveRateMap.get(3).get(4));//直通率
						achieveRateStandard[0] = 0;
						achieveRateStandard[1] = 0;
						achieveRateStandard[2] = 0;
					}catch(Exception e){
						achieveRateReal[0] = 0;//达成率
						achieveRateReal[1] = 0;//效率
						achieveRateReal[2] = 0;//直通率
						achieveRateStandard[0] = 0;
						achieveRateStandard[1] = 0;
						achieveRateStandard[2] = 0;
						//Toast.makeText(getActivity(),"效率统计图表数据解析出现异常",Toast.LENGTH_SHORT).show();
					}
					/**
					 * 标准翻线时间
					 * 实际翻线时间
					 */
					try{
						changeLineReal[0] = Integer.valueOf(changeLineMap.get(1).get(4));
						changeLineStandard[0] = Integer.valueOf(changeLineMap.get(0).get(4));
					}catch (Exception e) {
						// TODO: handle exception
						changeLineReal[0] = 0;
						changeLineStandard[0] = 0;
						//Toast.makeText(getActivity(),"翻线图表数据解析出现异常",Toast.LENGTH_SHORT).show();
					}
					/**
					 * 生产效率统计-PPH-当日累计
					 * 生产效率统计-PPH-最近小时
					 */
					try{
						pphReal[0] = Float.valueOf(pphMap.get(1).get(4)) / 100;
						pphStandard[0] = Float.valueOf(pphMap.get(0).get(4)) / 100;
					}catch(Exception e){
						pphReal[0] = (float) 0.0;
						pphStandard[0] = (float) 0.0;
						//Toast.makeText(getActivity(),"PPH统计图表数据解析出现异常",Toast.LENGTH_SHORT).show();
					}

					intent.putExtra("productionStatisticsReal",productionStatisticsReal);
					intent.putExtra("productionStatisticsStandard",productionStatisticsStandard);

					intent.putExtra("achieveRateReal",achieveRateReal);
					intent.putExtra("achieveRateStandard",achieveRateStandard);

					intent.putExtra("changeLineReal",changeLineReal);
					intent.putExtra("changeLineStandard",changeLineStandard);

					intent.putExtra("pphReal",pphReal);
					intent.putExtra("pphStandard",pphStandard);

					sendBroadcast(intent);
					break;
				case Constant.FAILED:
				case Constant.EXCEPTION:
					productionStatisticsReal[0] = 0;
					productionStatisticsReal[1] = 0;
					productionStatisticsReal[2] = 0;
					productionStatisticsStandard[0] = 0;
					productionStatisticsStandard[1] = 0;
					productionStatisticsStandard[2] = 0;

					achieveRateReal[0] = 0;
					achieveRateReal[1] = 0;
					achieveRateReal[2] = 0;
					achieveRateStandard[0] = 0;
					achieveRateStandard[1] = 0;
					achieveRateStandard[2] = 0;

					changeLineReal[0] = 0;
					changeLineStandard[0] = 0;

					pphReal[0] = (float) 0.0;
					pphStandard[0] = (float) 0.0;

					intent.putExtra("productionStatisticsReal",productionStatisticsReal);
					intent.putExtra("productionStatisticsStandard",productionStatisticsStandard);

					intent.putExtra("achieveRateReal",achieveRateReal);
					intent.putExtra("achieveRateStandard",achieveRateStandard);

					intent.putExtra("changeLineReal",changeLineReal);
					intent.putExtra("changeLineStandard",changeLineStandard);

					intent.putExtra("pphReal",pphReal);
					intent.putExtra("pphStandard",pphStandard);

					sendBroadcast(intent);
					break;
				case Constant.DEBUG:
					productionStatisticsReal[0] = (int) Math.random();
					productionStatisticsReal[1] = (int) Math.random();
					productionStatisticsReal[2] = (int) Math.random();
					productionStatisticsStandard[0] = (int) Math.random();
					productionStatisticsStandard[1] = (int) Math.random();
					productionStatisticsStandard[2] = (int) Math.random();

					achieveRateReal[0] = (int) Math.random();
					achieveRateReal[1] = (int) Math.random();
					achieveRateReal[2] = (int) Math.random();
					achieveRateStandard[0] = (int) Math.random();
					achieveRateStandard[1] = (int) Math.random();
					achieveRateStandard[2] = (int) Math.random();

					changeLineReal[0] = (int) Math.random();
					changeLineStandard[0] = (int)Math.random();

					pphReal[0] = (float) Math.random();
					pphStandard[0] = (float)Math.random();

					intent.putExtra("productionStatisticsReal",productionStatisticsReal);
					intent.putExtra("productionStatisticsStandard",productionStatisticsStandard);

					intent.putExtra("achieveRateReal",achieveRateReal);
					intent.putExtra("achieveRateStandard",achieveRateStandard);

					intent.putExtra("changeLineReal",changeLineReal);
					intent.putExtra("changeLineStandard",changeLineStandard);

					intent.putExtra("pphReal",pphReal);
					intent.putExtra("pphStandard",pphStandard);

					sendBroadcast(intent);
					break;
			}
		}
	};



	Handler myProductionStatisticsHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:

					/**
					 * 计划数量
					 * 备用
					 * 产量统计-实际生产-当日累计
					 * 产量统计-实际生产-最近一小时累计
					 * 产量统计-标准-当日累计
					 * 产量统计-标准-最近一小时累计
					 */
					try{
						productionStatisticsReal[0] = Integer.valueOf(productionStatisticsMap.get(0).get(4));
						productionStatisticsReal[1] = Integer.valueOf(productionStatisticsMap.get(2).get(4));
						productionStatisticsReal[2] = Integer.valueOf(productionStatisticsMap.get(3).get(4));
						productionStatisticsStandard[0] = Integer.valueOf(productionStatisticsMap.get(1).get(4));
						productionStatisticsStandard[1] = Integer.valueOf(productionStatisticsMap.get(4).get(4));
						productionStatisticsStandard[2] = Integer.valueOf(productionStatisticsMap.get(5).get(4));
					}catch(Exception e){
						productionStatisticsReal[0] = 0;
						productionStatisticsReal[1] = 0;
						productionStatisticsReal[2] = 0;
						productionStatisticsStandard[0] = 0;
						productionStatisticsStandard[1] = 0;
						productionStatisticsStandard[2] = 0;
						//Toast.makeText(getActivity(),"产量统计图表数据解析出现异常",Toast.LENGTH_SHORT).show();
					}

					//getProductionStatisticsChartValue(3,productionStatisticsReal,productionStatisticsStandard);
					intent.putExtra("productionStatisticsReal",productionStatisticsReal);
					intent.putExtra("productionStatisticsStandard",productionStatisticsStandard);
					sendBroadcast(intent);

					break;

				case Constant.FAILED:
				case Constant.EXCEPTION:
					productionStatisticsReal[0] = 0;
					productionStatisticsReal[1] = 0;
					productionStatisticsReal[2] = 0;
					productionStatisticsStandard[0] = 0;
					productionStatisticsStandard[1] = 0;
					productionStatisticsStandard[2] = 0;

					//getProductionStatisticsChartValue(3,productionStatisticsReal,productionStatisticsStandard);
					intent.putExtra("productionStatisticsReal",productionStatisticsReal);
					intent.putExtra("productionStatisticsStandard",productionStatisticsStandard);
					sendBroadcast(intent);

					break;

				case Constant.DEBUG:

					productionStatisticsReal[0] = (int) Math.random();
					productionStatisticsReal[1] = (int) Math.random();
					productionStatisticsReal[2] = (int) Math.random();
					productionStatisticsStandard[0] = (int) Math.random();
					productionStatisticsStandard[1] = (int) Math.random();
					productionStatisticsStandard[2] = (int) Math.random();

					//getProductionStatisticsChartValue(3,productionStatisticsReal,productionStatisticsStandard);
					intent.putExtra("productionStatisticsReal",productionStatisticsReal);
					intent.putExtra("productionStatisticsStandard",productionStatisticsStandard);
					sendBroadcast(intent);

					break;
			}

		}
	};

	Handler myAchieveRateHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:
					/**
					 * 生产效率统计-效率-当日累计
					 * 生产效率统计-效率-最近小时累计
					 * 计划达成率
					 * 直通率
					 */
					try{
						achieveRateReal[0] = Integer.valueOf(achieveRateMap.get(2).get(4));//达成率
						achieveRateReal[1] = Integer.valueOf(achieveRateMap.get(0).get(4));//效率
						achieveRateReal[2] = Integer.valueOf(achieveRateMap.get(3).get(4));//直通率
						achieveRateStandard[0] = 0;
						achieveRateStandard[1] = 0;
						achieveRateStandard[2] = 0;
					}catch(Exception e){
						achieveRateReal[0] = 0;//达成率
						achieveRateReal[1] = 0;//效率
						achieveRateReal[2] = 0;//直通率
						achieveRateStandard[0] = 0;
						achieveRateStandard[1] = 0;
						achieveRateStandard[2] = 0;
						//Toast.makeText(getActivity(),"效率统计图表数据解析出现异常",Toast.LENGTH_SHORT).show();
					}
					//getAchieveRateChartValue(3, achieveRateReal, achieveRateStandard);
					intent.putExtra("achieveRateReal",achieveRateReal);
					intent.putExtra("achieveRateStandard",achieveRateStandard);
					sendBroadcast(intent);

					break;

				case Constant.FAILED:
				case Constant.EXCEPTION:
					achieveRateReal[0] = 0;
					achieveRateReal[1] = 0;
					achieveRateReal[2] = 0;
					achieveRateStandard[0] = 0;
					achieveRateStandard[1] = 0;
					achieveRateStandard[2] = 0;

					//getAchieveRateChartValue(3, achieveRateReal, achieveRateStandard);
					intent.putExtra("achieveRateReal",achieveRateReal);
					intent.putExtra("achieveRateStandard",achieveRateStandard);
					sendBroadcast(intent);
					break;

				case Constant.DEBUG:
					achieveRateReal[0] = (int) Math.random();
					achieveRateReal[1] = (int) Math.random();
					achieveRateReal[2] = (int) Math.random();
					achieveRateStandard[0] = (int) Math.random();
					achieveRateStandard[1] = (int) Math.random();
					achieveRateStandard[2] = (int) Math.random();

					//getAchieveRateChartValue(3, achieveRateReal, achieveRateStandard);
					intent.putExtra("achieveRateReal",achieveRateReal);
					intent.putExtra("achieveRateStandard",achieveRateStandard);
					sendBroadcast(intent);
					break;
			}

		}
	};

	Handler myChangeLineHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:
					/**
					 * 标准翻线时间
					 * 实际翻线时间
					 */
					try{
						changeLineReal[0] = Integer.valueOf(changeLineMap.get(1).get(4));
						changeLineStandard[0] = Integer.valueOf(changeLineMap.get(0).get(4));
					}catch (Exception e) {
						// TODO: handle exception
						changeLineReal[0] = 0;
						changeLineStandard[0] = 0;
						//Toast.makeText(getActivity(),"翻线图表数据解析出现异常",Toast.LENGTH_SHORT).show();
					}
					intent.putExtra("changeLineReal",changeLineReal);
					intent.putExtra("changeLineStandard",changeLineStandard);
					sendBroadcast(intent);

					break;

				case Constant.FAILED:
				case Constant.EXCEPTION:
					changeLineReal[0] = 0;
					changeLineStandard[0] = 0;
					intent.putExtra("changeLineReal",changeLineReal);
					intent.putExtra("changeLineStandard",changeLineStandard);
					sendBroadcast(intent);

					break;

				case Constant.DEBUG:
					changeLineReal[0] = (int) Math.random();
					changeLineStandard[0] = (int)Math.random();

					intent.putExtra("changeLineReal",changeLineReal);
					intent.putExtra("changeLineStandard",changeLineStandard);
					sendBroadcast(intent);
					break;
			}

		}
	};

	Handler myPPHHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:

					/**
					 * 生产效率统计-PPH-当日累计
					 * 生产效率统计-PPH-最近小时
					 */
					try{
						pphReal[0] = Float.valueOf(pphMap.get(1).get(4)) / 100;
						pphStandard[0] = Float.valueOf(pphMap.get(0).get(4)) / 100;
					}catch(Exception e){
						pphReal[0] = (float) 0.0;
						pphStandard[0] = (float) 0.0;
						//Toast.makeText(getActivity(),"PPH统计图表数据解析出现异常",Toast.LENGTH_SHORT).show();
					}

					intent.putExtra("pphReal",pphReal);
					intent.putExtra("pphStandard",pphStandard);
					sendBroadcast(intent);

					break;

				case Constant.FAILED:
				case Constant.EXCEPTION:

					pphReal[0] = (float) 0.0;
					pphStandard[0] = (float) 0.0;

					intent.putExtra("pphReal",pphReal);
					intent.putExtra("pphStandard",pphStandard);
					sendBroadcast(intent);
					break;

				case Constant.DEBUG:

					pphReal[0] = (float) Math.random();
					pphStandard[0] = (float)Math.random();

					intent.putExtra("pphReal",pphReal);
					intent.putExtra("pphStandard",pphStandard);
					sendBroadcast(intent);
					break;
			}

		}
	};




}
