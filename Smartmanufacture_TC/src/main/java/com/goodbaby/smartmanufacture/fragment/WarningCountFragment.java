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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodbaby.smartmanufacture.DetailActivity;
import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.service.WarningCountService;



import java.util.HashMap;


@SuppressLint("NewApi")
public class WarningCountFragment extends Fragment implements OnClickListener{
	private TextView tv_lasthour_hundred,tv_lasthour_ten,tv_lasthour_units,//最近小时百位、十位、个位
			tv_inday_hundred,tv_inday_ten,tv_inday_units,//当日累计百位、十位、个位
			tv_QuantityWarning_hundred,tv_QuantityWarning_ten,tv_QuantityWarning_units,//质量异常百位、十位、个位
			tv_MaterialWarning_hundred,tv_MaterialWarning_ten,tv_MaterialWarning_units,//物料异常百位、十位、个位
			tv_BeatWarning_hundred,tv_BeatWarning_ten,tv_BeatWarning_units;//节拍异常百位、十位、个位
	private LinearLayout linear_lastHour,linear_inDay,linear_quantity,linear_material,linear_beatwarning;
	private HashMap<Integer, HashMap<Integer,String>> map;
	private String[] errorMessage = new String[1];
	private String TAG = "WarningCountFragment";
	private Animation animation_hundred,animation_ten,animation_units;

	private MyReciver myReciver;
	Intent intent;
	String[] warningCount = new String[5];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_warning_count, null);
		initView(view);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//动态注册广播接收器
		myReciver = new MyReciver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.WARNING_COUNT_RECIVER);
		getActivity().registerReceiver(myReciver, intentFilter);

		intent = new Intent(getActivity(),WarningCountService.class);
		getActivity().startService(intent);
	}

	public void initView(View view){
		linear_lastHour = (LinearLayout)view.findViewById(R.id.linear_lasthour);
		linear_inDay = (LinearLayout)view.findViewById(R.id.linear_inday);
		linear_quantity = (LinearLayout)view.findViewById(R.id.linear_quantity);
		linear_material = (LinearLayout)view.findViewById(R.id.linear_material);
		linear_beatwarning = (LinearLayout)view.findViewById(R.id.linear_beatwarning);

		tv_lasthour_hundred = (TextView)view.findViewById(R.id.tv_lasthour_hundred);
		tv_lasthour_ten = (TextView)view.findViewById(R.id.tv_lasthour_ten);
		tv_lasthour_units = (TextView)view.findViewById(R.id.tv_lasthour_units);

		tv_inday_hundred = (TextView)view.findViewById(R.id.tv_inday_hundred);
		tv_inday_ten = (TextView)view.findViewById(R.id.tv_inday_ten);
		tv_inday_units = (TextView)view.findViewById(R.id.tv_inday_units);

		tv_QuantityWarning_hundred = (TextView)view.findViewById(R.id.tv_QuantityWarning_hundred);
		tv_QuantityWarning_ten = (TextView)view.findViewById(R.id.tv_QuantityWarning_ten);
		tv_QuantityWarning_units = (TextView)view.findViewById(R.id.tv_QuantityWarning_units);

		tv_MaterialWarning_hundred = (TextView)view.findViewById(R.id.tv_MaterialWarning_hundred);
		tv_MaterialWarning_ten = (TextView)view.findViewById(R.id.tv_MaterialWarning_ten);
		tv_MaterialWarning_units = (TextView)view.findViewById(R.id.tv_MaterialWarning_units);

		tv_BeatWarning_hundred = (TextView)view.findViewById(R.id.tv_BeatWarning_hundred);
		tv_BeatWarning_ten = (TextView)view.findViewById(R.id.tv_BeatWarning_ten);
		tv_BeatWarning_units = (TextView)view.findViewById(R.id.tv_BeatWarning_units);

		linear_lastHour.setOnClickListener(this);
		linear_inDay.setOnClickListener(this);
		linear_quantity.setOnClickListener(this);
		linear_material.setOnClickListener(this);
		linear_beatwarning.setOnClickListener(this);

		initAnimation();


	}

	/**
	 * init animation
	 */
	private void initAnimation() {
		animation_hundred = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);
		animation_hundred.setDuration(100);
		animation_hundred.setFillBefore(true);

		animation_ten = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);
		animation_ten.setDuration(200);
		animation_ten.setFillBefore(true);

		animation_units = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);
		animation_units.setDuration(300);
		animation_units.setFillBefore(true);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().stopService(intent);
		getActivity().unregisterReceiver(myReciver);
		super.onDestroy();
	}



	public class MyReciver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			warningCount = intent.getStringArrayExtra("warningCount");
			tv_lasthour_hundred.startAnimation(animation_hundred);
			tv_inday_hundred.startAnimation(animation_hundred);
			tv_QuantityWarning_hundred.startAnimation(animation_hundred);
			tv_MaterialWarning_hundred.startAnimation(animation_hundred);
			tv_BeatWarning_hundred.startAnimation(animation_hundred);

			animation_hundred.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					/**
					 * map值顺序：
					 * 警告次数-节拍异常
					 * 警告次数-当日累计
					 * 警告次数-最近一小时累计
					 * 警告次数-物料异常
					 * 警告次数-质量异常
					 */
					//最近小时
					tv_lasthour_hundred.setBackground(getActivity().getResources().getDrawable(num2BigDrawable(combinationData(warningCount[0])[0])));
					//当日累计
					tv_inday_hundred.setBackground(getActivity().getResources().getDrawable(num2BigDrawable(combinationData(warningCount[1])[0])));
					//质量异常
					tv_QuantityWarning_hundred.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[2])[0])));
					//物料异常
					tv_MaterialWarning_hundred.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[3])[0])));
					//节拍异常
					tv_BeatWarning_hundred.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[4])[0])));

					tv_lasthour_ten.startAnimation(animation_ten);
					tv_inday_ten.startAnimation(animation_ten);
					tv_QuantityWarning_ten.startAnimation(animation_ten);
					tv_MaterialWarning_ten.startAnimation(animation_ten);
					tv_BeatWarning_ten.startAnimation(animation_ten);

				}
			});

			animation_ten.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					tv_lasthour_ten.setBackground(getActivity().getResources().getDrawable(num2BigDrawable(combinationData(warningCount[0])[1])));
					tv_inday_ten.setBackground(getActivity().getResources().getDrawable(num2BigDrawable(combinationData(warningCount[1])[1])));
					tv_QuantityWarning_ten.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[2])[1])));
					tv_MaterialWarning_ten.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[3])[1])));
					tv_BeatWarning_ten.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[4])[1])));

					tv_lasthour_units.startAnimation(animation_units);
					tv_inday_units.startAnimation(animation_units);
					tv_QuantityWarning_units.startAnimation(animation_units);
					tv_MaterialWarning_units.startAnimation(animation_units);
					tv_BeatWarning_units.startAnimation(animation_units);
				}

			});

			animation_units.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					tv_lasthour_units.setBackground(getActivity().getResources().getDrawable(num2BigDrawable(combinationData(warningCount[0])[2])));
					tv_inday_units.setBackground(getActivity().getResources().getDrawable(num2BigDrawable(combinationData(warningCount[1])[2])));
					tv_QuantityWarning_units.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[2])[2])));
					tv_MaterialWarning_units.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[3])[2])));
					tv_BeatWarning_units.setBackground(getActivity().getResources().getDrawable(num2SmallDrawable(combinationData(warningCount[4])[2])));
				}
			});
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
			case R.id.linear_lasthour:
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.LastHourWarning);
				startActivity(intent);
				break;

			case R.id.linear_inday:
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.InDayWarning);
				startActivity(intent);
				break;

			case R.id.linear_quantity:
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.QuantityWarning);
				startActivity(intent);
				break;

			case R.id.linear_material:
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.MaterialWarning);
				startActivity(intent);
				break;

			case R.id.linear_beatwarning:
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.BeatWarning);
				startActivity(intent);
				break;
		}
	}


	/**
	 * 对数据进行结合
	 * @param a
	 * @return
	 */
	public Integer[] combinationData(String a){
		Integer[] array = new Integer[3];
		if(Integer.valueOf(a) < 10){
			array[0] = 0;
			array[1] = 0;
			array[2] = Integer.valueOf(a);
			return array;
		}else if(Integer.valueOf(a)>=10 && Integer.valueOf(a) < 100){
			array[0] = 0;
			array[1] = (Integer.valueOf(a) / 10) % 10;
			array[2] = Integer.valueOf(a) % 10;
			return array;
		}else{
			array[0] = (Integer.valueOf(a) / 100) % 100;
			array[1] = (Integer.valueOf(a) / 10) % 10;
			array[2] = Integer.valueOf(a) % 10;
			return array;
		}
	}

	/**
	 * 数字转换大图片
	 * @param num
	 * @return
	 */
	public int num2BigDrawable(int num){
		int drawable = R.drawable.big_num_0;
		if(num == 0){
			drawable = R.drawable.big_num_0;
		}else if(num == 1){
			drawable = R.drawable.big_num_1;
		}else if(num == 2){
			drawable = R.drawable.big_num_2;
		}else if(num == 3){
			drawable = R.drawable.big_num_3;
		}else if(num == 4){
			drawable = R.drawable.big_num_4;
		}else if(num == 5){
			drawable = R.drawable.big_num_5;
		}else if(num == 6){
			drawable = R.drawable.big_num_6;
		}else if(num == 7){
			drawable = R.drawable.big_num_7;
		}else if(num == 8){
			drawable = R.drawable.big_num_8;
		}else if(num == 9){
			drawable = R.drawable.big_num_9;
		}
		return drawable;
	}

	/**
	 * 数字转换小图片
	 * @param num
	 * @return
	 */
	public int num2SmallDrawable(int num){
		int drawable = R.drawable.small_num_0;
		if(num == 0){
			drawable = R.drawable.small_num_0;
		}else if(num == 1){
			drawable = R.drawable.small_num_1;
		}else if(num == 2){
			drawable = R.drawable.small_num_2;
		}else if(num == 3){
			drawable = R.drawable.small_num_3;
		}else if(num == 4){
			drawable = R.drawable.small_num_4;
		}else if(num == 5){
			drawable = R.drawable.small_num_5;
		}else if(num == 6){
			drawable = R.drawable.small_num_6;
		}else if(num == 7){
			drawable = R.drawable.small_num_7;
		}else if(num == 8){
			drawable = R.drawable.small_num_8;
		}else if(num == 9){
			drawable = R.drawable.small_num_9;
		}
		return drawable;
	}




}
