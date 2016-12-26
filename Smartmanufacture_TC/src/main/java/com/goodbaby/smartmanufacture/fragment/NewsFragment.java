package com.goodbaby.smartmanufacture.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.service.LastNewsService;

import java.util.HashMap;

public class NewsFragment extends Fragment {
	private TextView tv_lastNews,tv_time;
	public String[] errorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> map;

	public MyLastNewsReciver myReciver;
	Intent intent;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_news, null,false);
		tv_lastNews = (TextView)view.findViewById(R.id.tv_lastnews);
		tv_time = (TextView)view.findViewById(R.id.tv_time);


		return view;
	}

	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getSysTime();
		//动态注册广播接收器
		myReciver = new MyLastNewsReciver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.LAST_NEWS_RECIVER);
		getActivity().registerReceiver(myReciver, intentFilter);
		intent = new Intent(getActivity(),LastNewsService.class);
		getActivity().startService(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(myReciver);
		getActivity().stopService(intent);
		getActivity().finish();
	}

	private class MyLastNewsReciver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Constant.LAST_NEWS_RECIVER)){
				tv_lastNews.setText(intent.getStringExtra("lastnews"));
			}
		}
	}

	private void getSysTime(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					try {
						myHandler.sendEmptyMessage(Constant.CURRENT_TIME);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case Constant.CURRENT_TIME:
					tv_time.setText(Utils.getCurSysTime());
					break;
			}
		}
	};

}
