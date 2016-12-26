package com.goodbaby.smartmanufacture.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 节拍异常数明细表
 * @author XXZX_LB
 *
 */
@SuppressLint("NewApi")
public class WarningCountBeatWarningDetailFragment extends Fragment{
	private int[] init_yValues_BeatWarning = {0,0,0,0,0,0,0,0,0,0,0};

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
		getWarningCountBeatWarningDetail();
		return view;
	}

	public void init(View view){
		mBarChart = (BarChart)view.findViewById(R.id.barChart);
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText(MyApplication.XBNAME + getActivity().getString(R.string.WarningCountBeatWarningDetailFragment));
		chartRender();
		getChartValue(10,init_yValues_BeatWarning);
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

		ArrayList<BarEntry> yVals_BeatWarning = new ArrayList<BarEntry>();
		//质量异常数据
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues_Quantity[i];
			yVals_BeatWarning.add(new BarEntry(val, i));
		}

		// create 2 datasets with different types
		BarDataSet set_BeatWarning = new BarDataSet(yVals_BeatWarning, getActivity().getString(R.string.BeatWarning));
		set_BeatWarning.setColor(getActivity().getResources().getColor(R.color.green));
		set_BeatWarning.setValueTextColor(getActivity().getResources().getColor(R.color.gray));
		set_BeatWarning.setValueTextSize(8f);
		set_BeatWarning.setValueFormatter(intValueFormatter);


		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set_BeatWarning);

		BarData data = new BarData(xVals, dataSets);

		// add space between the dataset groups in percent of bar-width
		data.setGroupSpace(80f);
		//data.setValueTypeface(tf);

		mBarChart.setData(data);
		mBarChart.invalidate();
	}

	public void getWarningCountBeatWarningDetail(){
		errorMessage[0] = "";
		map = new HashMap<Integer, HashMap<Integer,String>>();
		final String sqlText = Constant.sql_warning_count_detail_beatwarning(MyApplication.XBID);
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
					//Log.v("***map***", "map:" + map);
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
		int[] yValues_BeatWarning = new int[11];
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:
					for(int i=0;i<map.size();i++){
						//节拍异常
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
					getChartValue(11, yValues_BeatWarning);
					mProgressDialog.dismiss();
					break;

				case Constant.FAILED:
					//getChartValue(11, yValues_BeatWarning);
					Toast.makeText(getActivity(), ""+errorMessage[0], Toast.LENGTH_SHORT).show();
					mProgressDialog.dismiss();
					break;
				case Constant.DEBUG:
					int[] yValues_BeatWarning_debug = {0,8,3,0,8,0,6,0,7,0,0};
					getChartValue(11, yValues_BeatWarning_debug);
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
