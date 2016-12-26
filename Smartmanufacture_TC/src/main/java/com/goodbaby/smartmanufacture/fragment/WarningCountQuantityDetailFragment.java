package com.goodbaby.smartmanufacture.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * 质量异常数明细表
 * @author XXZX_LB
 *
 */
@SuppressLint("NewApi")
public class WarningCountQuantityDetailFragment extends Fragment{
	private int[] init_yValues_Quantity = {0,0,0,0,0,0,0,0,0,0,0};

	private BarChart mBarChart;
	private TextView tv_title;
	private HashMap<Integer, HashMap<Integer, String>> map;
	public String[] errorMessage = new String[1];
	private ProgressDialog mProgressDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_warning_count_detail, null);
		init(view);
		getWarningCountQuantityDetail();
		return view;
	}

	public void init(View view){
		mBarChart = (BarChart)view.findViewById(R.id.barChart);
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText(MyApplication.XBNAME + getActivity().getString(R.string.WarningCountQuantityDetailFragment));
		chartRender();
		getChartValue(10,init_yValues_Quantity);
	}

	public void chartRender(){
		mBarChart.setDescription("");

		mBarChart.setPinchZoom(false);
		mBarChart.setDrawBarShadow(false);
		mBarChart.setDrawGridBackground(false);
		mBarChart.setDoubleTapToZoomEnabled(false);

		//设置图例属性
		Legend l = mBarChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_CENTER);
		l.setYOffset(0f);
		l.setYEntrySpace(0f);
		l.setTextSize(8f);
		l.setTextColor(getActivity().getResources().getColor(R.color.gray));

		XAxis xl = mBarChart.getXAxis();
		xl.setPosition(XAxisPosition.BOTTOM);
		xl.setTextColor(getActivity().getResources().getColor(R.color.gray));

		YAxis leftAxis = mBarChart.getAxisLeft();
		leftAxis.setLabelCount(3, false);

		mBarChart.getXAxis().setDrawGridLines(false);
		mBarChart.getAxisLeft().setEnabled(true);
		mBarChart.getAxisLeft().setTextColor(getActivity().getResources().getColor(R.color.gray));
		mBarChart.getAxisRight().setEnabled(false);

	}
	public void getChartValue(int barChartNum, int[] yValues_Quantity){
		ArrayList<String> xVals = new ArrayList<String>();
		xVals.add("U1");
		xVals.add("U2");
		xVals.add("U3");
		xVals.add("U4");
		xVals.add("U5");
		xVals.add("U6");
		xVals.add("U7");
		xVals.add("U8");
		xVals.add("Z1");
		xVals.add("Z2");
		xVals.add("Z3");

		ArrayList<BarEntry> yVals_Quantity = new ArrayList<BarEntry>();
		//质量异常数据
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues_Quantity[i];
			yVals_Quantity.add(new BarEntry(val, i));
		}

		// create 2 datasets with different types
		BarDataSet set_quantity = new BarDataSet(yVals_Quantity, getActivity().getString(R.string.QuantityWarning));
		set_quantity.setColor(getActivity().getResources().getColor(R.color.blue));
		set_quantity.setValueTextColor(getActivity().getResources().getColor(R.color.gray));
		set_quantity.setValueTextSize(8f);
		set_quantity.setValueFormatter(intValueFormatter);


		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set_quantity);

		BarData data = new BarData(xVals, dataSets);

		// add space between the dataset groups in percent of bar-width
		data.setGroupSpace(80f);
		//data.setValueTypeface(tf);

		mBarChart.setData(data);
		mBarChart.invalidate();
	}

	public void getWarningCountQuantityDetail(){
		errorMessage[0] = "";
		map = new HashMap<Integer, HashMap<Integer,String>>();
		final String sqlText = Constant.sql_warning_count_detail_quantity(MyApplication.XBID);
		//Log.v("***sqlText***", ""+sqlText);
		/**
		 * 设置progressDialog的基本样式
		 */
		mProgressDialog = new ProgressDialog(getActivity(),ProgressDialog.THEME_HOLO_LIGHT);
		mProgressDialog.setMessage(getString(R.string.load_progressing));
		mProgressDialog.show();
		try{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Utils.spandTimeMethod();
					map = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, errorMessage);
					Log.v("***map***", "map:" + map);
					if(map != null){
						myHandler.sendEmptyMessage(Constant.SUCCESS);
					}else{
						myHandler.sendEmptyMessage(Constant.FAILED);
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
					int[] yValues_Quantity = new int[11];
					for(int i=0;i<map.size();i++){
						//质量异常
						if(map.get(i).get(1).equals("U1")){
							yValues_Quantity[0] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("U2")){
							yValues_Quantity[1] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("U3")){
							yValues_Quantity[2] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("U4")){
							yValues_Quantity[3] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("U5")){
							yValues_Quantity[4] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("U6")){
							yValues_Quantity[5] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("U7")){
							yValues_Quantity[6] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("U8")){
							yValues_Quantity[7] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("Z1")){
							yValues_Quantity[8] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("Z2")){
							yValues_Quantity[9] = Integer.valueOf(map.get(i).get(0));
						}else if(map.get(i).get(1).equals("Z3")){
							yValues_Quantity[10] = Integer.valueOf(map.get(i).get(0));
						}
					}
					getChartValue(11, yValues_Quantity);
					mProgressDialog.dismiss();
					break;

				case Constant.FAILED:
					mProgressDialog.dismiss();
					Toast.makeText(getActivity(), ""+errorMessage[0], Toast.LENGTH_SHORT).show();
					break;
				case Constant.DEBUG:

					int[] yValues_Quantity_debug = {0,6,12,0,20,0,5,5,0,0};
					getChartValue(11, yValues_Quantity_debug);
					break;
			}
		}

	};

	/**
	 * 定义柱状数据显示的数据格式
	 */
	ValueFormatter intValueFormatter = new ValueFormatter() {

		@Override
		public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
			// TODO Auto-generated method stub
			return ""+(int)value;
		}
	};


}
