package com.goodbaby.smartmanufacture;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.goodbaby.smartmanufacture.fragment.BarChartDetailFragment;
import com.goodbaby.smartmanufacture.fragment.SiteLayoutDetailFragment;
import com.goodbaby.smartmanufacture.fragment.WarningCountBeatWarningDetailFragment;
import com.goodbaby.smartmanufacture.fragment.WarningCountInDayDetailFragment;
import com.goodbaby.smartmanufacture.fragment.WarningCountLastHourDetailFragment;
import com.goodbaby.smartmanufacture.fragment.WarningCountMaterialDetailFragment;
import com.goodbaby.smartmanufacture.fragment.WarningCountQuantityDetailFragment;

@SuppressLint("NewApi")
public class DetailActivity extends Activity{

	private FragmentEnum fEnum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_detail);

		Intent intent = getIntent();
		fEnum = (FragmentEnum)intent.getSerializableExtra("enum");
		SwitchFragment(fEnum);


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

	public Fragment SwitchFragment(FragmentEnum fEnum){
		FragmentManager fm = getFragmentManager();
		Fragment fg = null;
		switch (fEnum) {
			case ChartDetail:
				fg = new BarChartDetailFragment();
				break;
			case LastHourWarning:
				fg = new WarningCountLastHourDetailFragment();
				break;
			case InDayWarning:
				fg = new WarningCountInDayDetailFragment();
				break;
			case QuantityWarning:
				fg = new WarningCountQuantityDetailFragment();
				break;
			case MaterialWarning:
				fg = new WarningCountMaterialDetailFragment();
				break;
			case BeatWarning:
				fg = new WarningCountBeatWarningDetailFragment();
				break;
			case SitezLayoutDetail:
				fg = new SiteLayoutDetailFragment();
				break;
		}
		fm.beginTransaction().replace(R.id.frame_detail, fg).commit();

		return fg;
	}

	public enum FragmentEnum{
		ChartDetail,
		LastHourWarning,
		InDayWarning,
		QuantityWarning,
		MaterialWarning,
		BeatWarning,
		SitezLayoutDetail
	}



}
