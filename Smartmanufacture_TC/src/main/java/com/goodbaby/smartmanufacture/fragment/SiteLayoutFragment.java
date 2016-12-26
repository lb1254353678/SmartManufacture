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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.goodbaby.smartmanufacture.DetailActivity;
import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.service.SiteLayoutService;

import java.util.HashMap;

@SuppressLint("NewApi")
public class SiteLayoutFragment extends Fragment implements OnClickListener{

	private ImageView iv_runway,iv_u1,iv_u2,iv_u3,iv_u4,iv_u5,iv_u6,iv_u7,iv_u8,iv_z1,iv_z2,iv_z3;
	public String[] errorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> map;
	private String TAG = "NewsFragment";
	public static String site;

	private MyReciver myReciver;
	Intent intent;
	String[] siteStatusCode = new String[11];
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_site_layout, null);
		initView(view);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//动态注册广播接收器
		myReciver = new MyReciver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.SITE_STATUS_RECIVER);
		getActivity().registerReceiver(myReciver, intentFilter);

		intent = new Intent(getActivity(),SiteLayoutService.class);
		getActivity().startService(intent);
	}

	private void initView(View view) {
		iv_runway = (ImageView)view.findViewById(R.id.middle);
		iv_runway.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_runway));
		iv_u1 = (ImageView)view.findViewById(R.id.iv_u1);
		iv_u2 = (ImageView)view.findViewById(R.id.iv_u2);
		iv_u3 = (ImageView)view.findViewById(R.id.iv_u3);
		iv_u4 = (ImageView)view.findViewById(R.id.iv_u4);
		iv_u5 = (ImageView)view.findViewById(R.id.iv_u5);
		iv_u6 = (ImageView)view.findViewById(R.id.iv_u6);
		iv_u7 = (ImageView)view.findViewById(R.id.iv_u7);
		iv_u8 = (ImageView)view.findViewById(R.id.iv_u8);
		iv_z1 = (ImageView)view.findViewById(R.id.iv_z1);
		iv_z2 = (ImageView)view.findViewById(R.id.iv_z2);
		iv_z3 = (ImageView)view.findViewById(R.id.iv_z3);

		iv_u1.setOnClickListener(this);
		iv_u2.setOnClickListener(this);
		iv_u3.setOnClickListener(this);
		iv_u4.setOnClickListener(this);
		iv_u5.setOnClickListener(this);
		iv_u6.setOnClickListener(this);
		iv_u7.setOnClickListener(this);
		iv_u8.setOnClickListener(this);
		iv_z1.setOnClickListener(this);
		iv_z2.setOnClickListener(this);
		iv_z3.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
			case R.id.iv_u1:
				site = "U1";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_u2:
				site = "U2";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_u3:
				site = "U3";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_u4:
				site = "U4";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_u5:
				site = "U5";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_u6:
				site = "U6";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_u7:
				site = "U7";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_u8:
				site = "U8";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_z1:
				site = "Z1";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
			case R.id.iv_z2:
				site = "Z2";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;

			case R.id.iv_z3:
				site = "Z3";
				intent = new Intent(getActivity(),DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.SitezLayoutDetail);
				startActivity(intent);
				break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().stopService(intent);
		getActivity().unregisterReceiver(myReciver);
	}

	public class MyReciver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			siteStatusCode = intent.getStringArrayExtra("siteStatusCode");

			siteUStatus(siteStatusCode[0], iv_u1);
			siteUStatus(siteStatusCode[1], iv_u2);
			siteUStatus(siteStatusCode[2], iv_u3);
			siteUStatus(siteStatusCode[3], iv_u4);
			siteUStatus(siteStatusCode[4], iv_u5);
			siteUStatus(siteStatusCode[5], iv_u6);
			siteUStatus(siteStatusCode[6], iv_u7);
			siteUStatus(siteStatusCode[7], iv_u8);

			siteZStatus(siteStatusCode[8], iv_z1);
			siteZStatus(siteStatusCode[9], iv_z2);
			siteZStatus(siteStatusCode[10], iv_z3);

			if(siteStatusCode[0].equals("0")  && siteStatusCode[1].equals("0")
					&& siteStatusCode[2].equals("0") && siteStatusCode[3].equals("0")
					&& siteStatusCode[4].equals("0") && siteStatusCode[5].equals("0")
					&& siteStatusCode[6].equals("0") && siteStatusCode[7].equals("0")
					&& siteStatusCode[8].equals("0") && siteStatusCode[9].equals("0")
					&& siteStatusCode[10].equals("0")){
				iv_runway.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_runway));
			}else {
				Glide.with(getActivity()).load(R.drawable.ic_runway).into(iv_runway);
			}
		}

	}



	//u站点设置图片方法
	public void siteUStatus(String status,ImageView view){
		switch (Integer.valueOf(status)) {
			case 0://灰色未安排
				try{
					Glide.with(getActivity()).load(R.drawable.ic_u_1).into(view);
				}catch (Exception e) {
					// TODO: handle exception
					//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_u_1));
				}
				break;
			case 1://橙色常亮，缺物料
				try{
					Glide.with(getActivity()).load(R.drawable.ic_u_2).into(view);
				}catch (Exception e) {
					// TODO: handle exception
					//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_u_1));
				}
				break;
			case 2://绿色常亮，正常生产
				try{
					Glide.with(getActivity()).load(R.drawable.ic_u_3).into(view);
				}catch (Exception e) {
					// TODO: handle exception
					view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_u_1));
				}
				break;
			case 3://红色闪烁，产量异常
				try{
					Glide.with(getActivity()).load(R.drawable.ic_u_4).into(view);
				}catch (Exception e) {
					// TODO: handle exception
					//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_u_1));
				}
				break;
			case 4://橙色闪烁，质量异常
				try{
					Glide.with(getActivity()).load(R.drawable.ic_u_5).into(view);
				}catch (Exception e) {
					// TODO: handle exception
					//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_u_1));
				}
				break;
			case 5://绿色闪烁，节拍异常
				try{
					Glide.with(getActivity()).load(R.drawable.ic_u_6).into(view);
				}catch (Exception e) {
					// TODO: handle exception
					//Glide.with(getActivity()).load(R.drawable.ic_u_1).into(view);
					//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_u_1));
				}
				break;
			default:
				view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_u_1));
				break;

		}

	}


	public void siteZStatus(String status,ImageView view){
		if(view == iv_z1){
			switch (Integer.valueOf(status)) {
				case 0://灰色未安排
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z1_1));
						//Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
					}
					break;
				case 1://橙色常亮，缺物料
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z1_2).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z1_1));
						//Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
					}
					break;
				case 2://绿色常亮，正常生产
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z1_3).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z1_1));
					}
					break;
				case 3://红色闪烁，产量异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z1_4).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z1_1));
					}
					break;
				case 4://橙色闪烁，质量异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z1_5).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z1_1));
					}
					break;
				case 5://绿色闪烁，节拍异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z1_6).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z1_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z1_1));
					}
					break;

			}
		}else if(view == iv_z2){
			switch (Integer.valueOf(status)) {
				case 0://灰色未安排
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z2_1).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z2_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z2_1));
					}
					break;
				case 1://橙色常亮，缺物料
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z2_2).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z2_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z2_1));
					}
					break;
				case 2://绿色常亮，正常生产
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z2_3).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z2_1).into(view);
						view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z2_1));
					}
					break;
				case 3://红色闪烁，产量异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z2_4).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z2_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z2_1));
					}
					break;
				case 4://橙色闪烁，质量异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z2_5).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z2_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z2_1));
					}
					break;
				case 5://绿色闪烁，节拍异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z2_6).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z2_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z2_1));
					}
					break;

			}
		}else if(view == iv_z3){
			switch (Integer.valueOf(status)) {
				case 0://灰色未安排
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z3_1).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z3_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z3_1));
					}
					break;
				case 1://橙色常亮，缺物料
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z3_2).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z3_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z3_1));
					}
					break;
				case 2://绿色常亮，正常生产
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z3_3).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z3_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z3_1));
					}
					break;
				case 3://红色闪烁，产量异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z3_4).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z3_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z3_1));
					}
					break;
				case 4://橙色闪烁，质量异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z3_5).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z3_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z3_1));
					}
					break;
				case 5://绿色闪烁，节拍异常
					try{
						Glide.with(getActivity()).load(R.drawable.ic_z3_6).into(view);
					}catch (Exception e) {
						// TODO: handle exception
						//Glide.with(getActivity()).load(R.drawable.ic_z3_1).into(view);
						//view.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_z3_1));
					}
					break;
			}
		}
	}


}
