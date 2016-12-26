package com.goodbaby.smartmanufacture.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.service.ModelService;

import java.util.HashMap;

@SuppressLint("NewApi")
public class ModelFragment extends Fragment {

	private TextView tv_deviceModel;
	private HashMap<Integer, HashMap<Integer,String>> map;
	private String[] errorMessage = new String[1];

	private String TAG = "***ModelFragment***";
	private MyReciver myReciver;
	Intent intent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_notes, null);
		tv_deviceModel = (TextView)view.findViewById(R.id.tv_devicemodel);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//动态注册广播接收器
		myReciver = new MyReciver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.MODEL_RECIVER);
		getActivity().registerReceiver(myReciver, intentFilter);

		intent = new Intent(getActivity(),ModelService.class);
		getActivity().startService(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().stopService(intent);
		getActivity().unregisterReceiver(myReciver);
	}

	public class MyReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			tv_deviceModel.setText(intent.getStringExtra("model"));

		}
	}



}
