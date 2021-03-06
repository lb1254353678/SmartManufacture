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
import java.util.Timer;
import java.util.TimerTask;

/**
 * 当日累计警报次数明细表
 * @author XXZX_LB
 *
 */
@SuppressLint("NewApi")
public class WarningCountInDayDetailFragment extends Fragment{
	/*private int[] yValues_Quantity = {0,0,0,0,0,0,0,0,0,0};
	private int[] yValues_Material = {0,0,0,0,0,0,0,0,0,0};
	private int[] yValues_BeatWarning = {0,0,0,0,0,0,0,0,0,0};*/

	private BarChart mBarChart;
	private TextView tv_title;

	public String[] errorMessage = new String[1];
	private HashMap<Integer, HashMap<Integer, String>> map;
	private ProgressDialog mProgressDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_warning_count_detail, null);
		init(view);
		chartRender();
		getWarningCountInDayDetail();
		return view;
	}

	public void init(View view){
		mBarChart = (BarChart)view.findViewById(R.id.barChart);
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText(MyApplication.XBNAME + "当日累计站点异常汇总");

	}

	public void chartRender(){
		mBarChart.setDescription("");

		mBarChart.setPinchZoom(false);
		mBarChart.setDrawBarShadow(false);
		mBarChart.setDrawGridBackground(false);
		mBarChart.setDoubleTapToZoomEnabled(false);

		//设置字体
		//tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
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
	public void getChartValue(int barChartNum,int[] y_Quantity,int[] y_Material,int[] y_BeatWarning){
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
		ArrayList<BarEntry> yValsMaterial = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yValsBeatWarning = new ArrayList<BarEntry>();
		//质量异常数据
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) y_Quantity[i];
			yVals_Quantity.add(new BarEntry(val, i));
		}
		//物料异常数据
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) y_Material[i];
			yValsMaterial.add(new BarEntry(val, i));
		}
		//节拍异常数据
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) y_BeatWarning[i];
			yValsBeatWarning.add(new BarEntry(val, i));
		}

		// create 2 datasets with different types
		BarDataSet set_quantity = new BarDataSet(yVals_Quantity, getActivity().getString(R.string.QuantityWarning));
		set_quantity.setColor(getActivity().getResources().getColor(R.color.blue));
		set_quantity.setValueTextColor(getActivity().getResources().getColor(R.color.gray));
		set_quantity.setValueTextSize(8f);
		set_quantity.setValueFormatter(intValueFormatter);

		BarDataSet set_material = new BarDataSet(yValsMaterial, getActivity().getString(R.string.Materialarning));
		set_material.setColor(getActivity().getResources().getColor(R.color.red));
		set_material.setValueTextColor(getActivity().getResources().getColor(R.color.gray));
		set_material.setValueTextSize(8f);
		set_material.setValueFormatter(intValueFormatter);

		BarDataSet set_beatWarning = new BarDataSet(yValsBeatWarning, getActivity().getString(R.string.BeatWarning));
		set_beatWarning.setColor(getActivity().getResources().getColor(R.color.green));
		set_beatWarning.setValueTextColor(getActivity().getResources().getColor(R.color.gray));
		set_beatWarning.setValueTextSize(8f);
		set_beatWarning.setValueFormatter(intValueFormatter);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set_quantity);
		dataSets.add(set_material);
		dataSets.add(set_beatWarning);

		BarData data = new BarData(xVals, dataSets);

		// add space between the dataset groups in percent of bar-width
		data.setGroupSpace(80f);
		//data.setValueTypeface(tf);

		mBarChart.setData(data);
		mBarChart.invalidate();
	}


	public void getWarningCountInDayDetail(){
		errorMessage[0] = "";
		map = new HashMap<Integer, HashMap<Integer,String>>();
		final String sqlText = Constant.sql_warning_count_detail_inday(MyApplication.XBID);
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
					Log.v("***map***", "map" + map);
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

	public void getWarningCountInDayDetail_debug(){
		myHandler.sendEmptyMessage(Constant.DEBUG);
	}

	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:
					int[] yValues_Quantity = new int[11];
					int[] yValues_Material = new int[11];
					int[] yValues_BeatWarning = new int[11];

					for(int i=0;i<map.size();i++){
						//质量异常
						if(map.get(i).get(2).equals("1") ){
							//Log.v("yValues_Quantity", ""+i);
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
						}else if(map.get(i).get(2).equals("2")){//物料异常
							//Log.v("yValues_Material", ""+i);
							if(map.get(i).get(1).equals("U1")){
								yValues_Material[0] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U2")){
								yValues_Material[1] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U3")){
								yValues_Material[2] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U4")){
								yValues_Material[3] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U5")){
								yValues_Material[4] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U6")){
								yValues_Material[5] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U7")){
								yValues_Material[6] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U8")){
								yValues_Material[7] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("Z1")){
								yValues_Material[8] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("Z2")){
								yValues_Material[9] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("Z3")){
								yValues_Material[10] = Integer.valueOf(map.get(i).get(0));
							}
						}else if(map.get(i).get(2).equals("3")){//节拍异常
							if(map.get(i).get(1).equals("U1")){
								yValues_BeatWarning[0] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U2")){
								yValues_BeatWarning[1] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U3")){
								yValues_BeatWarning[2] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U4")){
								yValues_BeatWarning[3] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U5")){
								yValues_BeatWarning[4] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U6")){
								yValues_BeatWarning[5] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U7")){
								yValues_BeatWarning[6] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("U8")){
								yValues_BeatWarning[7] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("Z1")){
								yValues_BeatWarning[8] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("Z2")){
								yValues_BeatWarning[9] = Integer.valueOf(map.get(i).get(0));
							}else if(map.get(i).get(1).equals("Z3")){
								yValues_BeatWarning[10] = Integer.valueOf(map.get(i).get(0));
							}
						}
					}
					getChartValue(11, yValues_Quantity, yValues_Material, yValues_BeatWarning);
					mProgressDialog.dismiss();
					break;

				case Constant.FAILED:
					int[] yValues_Quantity1 = {0,0,0,0,0,0,0,0,0,0,0};
					int[] yValues_Material1 = {0,0,0,0,0,0,0,0,0,0,0};
					int[] yValues_BeatWarning1 = {0,0,0,0,0,0,0,0,0,0,0};
					getChartValue(11, yValues_Quantity1, yValues_Material1, yValues_BeatWarning1);
					mProgressDialog.dismiss();
					break;
				case Constant.DEBUG:
					int[] yValues_Quantity_debug = {0,0,6,12,0,20,0,5,5,0,0};
					int[] yValues_Material_debug = {0,0,9,7,0,18,0,5,0,6,0};
					int[] yValues_BeatWarning_debug = {0,0,8,3,0,8,0,6,0,7,0};
					getChartValue(11, yValues_Quantity_debug, yValues_Material_debug, yValues_BeatWarning_debug);

					break;
			}
		}
	};

	private void schedule(final long delay ){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				do {
					try {
						Thread.sleep(delay);
						getWarningCountInDayDetail();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (true);
			}
		}, delay);
	}

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
