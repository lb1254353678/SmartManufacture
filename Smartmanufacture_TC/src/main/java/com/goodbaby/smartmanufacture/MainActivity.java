package com.goodbaby.smartmanufacture;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.goodbaby.smartmanufacture.fragment.BarChartFragment;
import com.goodbaby.smartmanufacture.fragment.ModelFragment;
import com.goodbaby.smartmanufacture.fragment.NewsFragment;
import com.goodbaby.smartmanufacture.fragment.SiteLayoutFragment;
import com.goodbaby.smartmanufacture.fragment.WarningCountFragment;
import com.goodbaby.smartmanufacture.global.MyApplication;


public class MainActivity extends FragmentActivity {

	private MyApplication myApplication;
	public String[] errorMessage = new String[1];
	NewsFragment newsFragment;
	BarChartFragment chartFragment;
	WarningCountFragment alarmTimesFragment;
	SiteLayoutFragment siteLayoutFragment;
	ModelFragment modelFragment;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_main);
		myApplication = (MyApplication)getApplication();

		newsFragment = new NewsFragment();//最新资讯
		chartFragment = new BarChartFragment();//产量统计
		alarmTimesFragment = new WarningCountFragment();//警报次数
		siteLayoutFragment = new SiteLayoutFragment();//产线站点
		modelFragment = new ModelFragment();//注释模块

		getSupportFragmentManager().beginTransaction().replace(R.id.frame_news,newsFragment).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.frame_chart, chartFragment).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.frame_alarm_times, alarmTimesFragment).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.frame_site_layout, siteLayoutFragment).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.frame_notes, modelFragment).commit();

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

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}



}
