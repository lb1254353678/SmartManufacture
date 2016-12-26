package com.goodbaby.smartmanufacture.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.goodbaby.smartmanufacture.DetailActivity;
import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.service.BarChartService;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class BarChartFragment extends Fragment implements View.OnClickListener{

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

	private BarChart productionStatisticsBarChart,achieveRateBarChart;
	private HorizontalBarChart changeLineBarChart,pphBarChart;

	private MyReciver myReciver;
	Intent intent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_chart, null);

		initView(view);

		return view;
	}

	private void initView(View view) {
		productionStatisticsBarChart = (BarChart)view.findViewById(R.id.productionStatisticsBarChart);
		achieveRateBarChart = (BarChart)view.findViewById(R.id.achieveRateBarChart);
		changeLineBarChart = (HorizontalBarChart)view.findViewById(R.id.changeLineBarChart);
		pphBarChart = (HorizontalBarChart)view.findViewById(R.id.pphBarChart);

		productionStatisticsBarChart.setOnClickListener(this);
		achieveRateBarChart.setOnClickListener(this);
		changeLineBarChart.setOnClickListener(this);
		pphBarChart.setOnClickListener(this);

		productionStatisticsChartRender();
		achieveRateChartRender();
		changeLineRender();
		pphChartRender();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//动态注册广播接收器
		myReciver = new MyReciver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.BAR_CHART_RECIVER);
		getActivity().registerReceiver(myReciver, intentFilter);

		intent = new Intent(getActivity(),BarChartService.class);
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
			productionStatisticsReal = intent.getIntArrayExtra("productionStatisticsReal");
			productionStatisticsStandard = intent.getIntArrayExtra("productionStatisticsStandard");

			achieveRateReal = intent.getIntArrayExtra("achieveRateReal");
			achieveRateStandard = intent.getIntArrayExtra("achieveRateStandard");

			changeLineReal = intent.getIntArrayExtra("changeLineReal");
			changeLineStandard = intent.getIntArrayExtra("changeLineStandard");

			pphReal = intent.getFloatArrayExtra("pphReal");
			pphStandard = intent.getFloatArrayExtra("pphStandard");

			getProductionStatisticsChartValue(3,productionStatisticsReal,productionStatisticsStandard);
			getAchieveRateChartValue(3, achieveRateReal, achieveRateStandard);
			getChangeLineChartValue(1,changeLineReal,changeLineStandard);
			getPPHChartValue(1, pphReal, pphStandard);
		}

	}


	//生产统计 chartRender
	public void productionStatisticsChartRender(){

		//verticalChart.setOnChartValueSelectedListener(this);
		productionStatisticsBarChart.setDescription("");

		productionStatisticsBarChart.setPinchZoom(false);
		productionStatisticsBarChart.setDrawBarShadow(false);
		productionStatisticsBarChart.setDrawGridBackground(false);
		productionStatisticsBarChart.setDoubleTapToZoomEnabled(false);
		//verticalChart.setNoDataTextDescription("You need to provide data for the chart.")


		//设置图例属性
		Legend l = productionStatisticsBarChart.getLegend();
		l.setEnabled(false);//设置隐藏legend
		l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
		l.setYOffset(0f);
		l.setYEntrySpace(0f);
		l.setTextSize(8f);
		l.setTextColor(getActivity().getResources().getColor(R.color.gray));

		XAxis xl = productionStatisticsBarChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setTextColor(getActivity().getResources().getColor(R.color.white));

		YAxis leftAxis = productionStatisticsBarChart.getAxisLeft();
		leftAxis.setLabelCount(3, false);
		//leftAxis.setSpaceTop(100f);
		//leftAxis.setShowOnlyMinMax(true);

		productionStatisticsBarChart.getXAxis().setDrawGridLines(false);
		productionStatisticsBarChart.getAxisLeft().setEnabled(true);
		productionStatisticsBarChart.getAxisLeft().setTextColor(getActivity().getResources().getColor(R.color.white));
		productionStatisticsBarChart.getAxisRight().setEnabled(false);

	}

	//直通率 chartRender
	public void achieveRateChartRender(){
		achieveRateBarChart.setDescription("");

		achieveRateBarChart.setPinchZoom(false);
		achieveRateBarChart.setDrawBarShadow(false);
		achieveRateBarChart.setDrawGridBackground(false);
		achieveRateBarChart.setDoubleTapToZoomEnabled(false);

		//设置图例属性
		Legend l = achieveRateBarChart.getLegend();
		l.setEnabled(false);

		XAxis xl = achieveRateBarChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setTextColor(getActivity().getResources().getColor(R.color.white));

		YAxis leftAxis = achieveRateBarChart.getAxisLeft();
		leftAxis.setLabelCount(3, false);

		achieveRateBarChart.getXAxis().setDrawGridLines(false);
		achieveRateBarChart.getAxisLeft().setEnabled(true);
		achieveRateBarChart.getAxisLeft().setTextColor(getActivity().getResources().getColor(R.color.white));
		achieveRateBarChart.getAxisRight().setEnabled(false);

	}

	//换线 chartRender
	public void changeLineRender(){

		//horizontalChart.setOnChartValueSelectedListener(this);
		changeLineBarChart.setDescription("");
		changeLineBarChart.setPinchZoom(false);
		changeLineBarChart.setDrawBarShadow(false);
		changeLineBarChart.setDrawGridBackground(false);
		changeLineBarChart.setDoubleTapToZoomEnabled(false);

        /*MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        horizontalChart.setMarkerView(mv);*/

		//设置字体
		//tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
		//设置图例属性
		Legend l = changeLineBarChart.getLegend();
		l.setEnabled(false);
		l.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
		l.setYOffset(0f);
		l.setYEntrySpace(0f);
		l.setTextSize(8f);
		l.setTextColor(getActivity().getResources().getColor(R.color.gray));

		XAxis xl = changeLineBarChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setTextColor(getActivity().getResources().getColor(R.color.white));

		YAxis rightAxis = changeLineBarChart.getAxisRight();
		rightAxis.setLabelCount(3, false);
		//rightAxis.setSpaceTop(100f);
		//rightAxis.setShowOnlyMinMax(true);

		changeLineBarChart.getXAxis().setDrawGridLines(false);
		changeLineBarChart.getAxisLeft().setEnabled(false);
		changeLineBarChart.getAxisRight().setEnabled(true);
		changeLineBarChart.getAxisRight().setTextColor(getActivity().getResources().getColor(R.color.white));

	}

	//PPH chartRender
	public void pphChartRender(){

		//horizontalChart.setOnChartValueSelectedListener(this);
		pphBarChart.setDescription("");
		pphBarChart.setPinchZoom(false);
		pphBarChart.setDrawBarShadow(false);
		pphBarChart.setDrawGridBackground(false);
		pphBarChart.setDoubleTapToZoomEnabled(false);

		//设置图例属性
		Legend l = pphBarChart.getLegend();
		l.setEnabled(false);
		l.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
		l.setYOffset(0f);
		l.setYEntrySpace(0f);
		l.setTextSize(8f);
		l.setTextColor(getActivity().getResources().getColor(R.color.gray));

		XAxis xl = pphBarChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setTextColor(getActivity().getResources().getColor(R.color.white));

		YAxis rightAxis = pphBarChart.getAxisRight();
		rightAxis.setLabelCount(3, false);
		//rightAxis.setSpaceTop(100f);
		//rightAxis.setShowOnlyMinMax(true);

		pphBarChart.getXAxis().setDrawGridLines(false);
		pphBarChart.getAxisLeft().setEnabled(false);
		pphBarChart.getAxisRight().setEnabled(true);
		pphBarChart.getAxisRight().setTextColor(getActivity().getResources().getColor(R.color.white));

	}


	/**
	 * 生产统计
	 * 为BarChart绑定值
	 * @param barChartNum
	 * @param yValues1
	 * @param yValues2
     */
	public void getProductionStatisticsChartValue(int barChartNum,int[] yValues1,int[] yValues2){
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < barChartNum; i++) {

			if(i == 0){
				//xVals.add(getActivity().getString(R.string.efficiency));
				xVals.add("计划总量");
			}else if(i == 1){
				xVals.add("当日累计");
			}else{
				xVals.add("最近小时");
			}
		}

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues1[i];
			yVals1.add(new BarEntry(val, i));
		}

		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues2[i];
			yVals2.add(new BarEntry(val, i));
		}

		// create 2 datasets with different types
		BarDataSet set1 = new BarDataSet(yVals1, "实际");
		set1.setColor(Color.parseColor("#88BA55"));
		set1.setValueTextColor(Color.parseColor("#ffffff"));
		set1.setValueTextSize(8f);
		set1.setValueFormatter(valueFormatter);

		BarDataSet set2 = new BarDataSet(yVals2, "标准");
		set2.setColor(Color.parseColor("#1EB1ED"));
		set2.setValueTextColor(Color.parseColor("#ffffff"));
		set2.setValueTextSize(8f);
		set2.setValueFormatter(valueFormatter);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);
		dataSets.add(set2);

		BarData data = new BarData(xVals, dataSets);

		// add space between the dataset groups in percent of bar-width
		//data.setGroupSpace(80f);
		//data.setValueTypeface(tf);

		productionStatisticsBarChart.setData(data);
		productionStatisticsBarChart.invalidate();
	}

	/**
	 * 直通率
	 * 为BarChart绑定值
	 * @param barChartNum
	 * @param yValues1
	 * @param yValues2
     */
	public void getAchieveRateChartValue(int barChartNum,int[] yValues1,int[] yValues2){
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < barChartNum; i++) {

			if(i == 0){
				//xVals.add(getActivity().getString(R.string.efficiency));
				xVals.add("达成率%");
			}else if(i == 1){
				xVals.add("效率%");
			}else{
				xVals.add("直通率%");
			}
		}

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues1[i];
			yVals1.add(new BarEntry(val, i));
		}

		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues2[i];
			yVals2.add(new BarEntry(val, i));
		}
		BarDataSet set1 = new BarDataSet(yVals1, "实际");
		set1.setColor(Color.parseColor("#88BA55"));
		set1.setValueTextColor(Color.parseColor("#ffffff"));
		set1.setValueTextSize(8f);
		set1.setValueFormatter(numValueFormatter);

		BarDataSet set2 = new BarDataSet(yVals2, "标准");
		set2.setColor(Color.parseColor("#1EB1ED"));
		set2.setValueTextColor(Color.parseColor("#ffffff"));
		set2.setValueTextSize(8f);
		set2.setValueFormatter(valueFormatter);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);
		dataSets.add(set2);

		BarData data = new BarData(xVals, dataSets);

		// add space between the dataset groups in percent of bar-width
		data.setGroupSpace(80f);
		//data.setValueTypeface(tf);

		achieveRateBarChart.setData(data);
		achieveRateBarChart.invalidate();
	}

	/**
	 * 翻线
	 * 为BarChart绑定值
	 * @param barChartNum
	 * @param yValues1
	 * @param yValues2
     */
	public void getChangeLineChartValue(int barChartNum,int[] yValues1,int[] yValues2){
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < barChartNum; i++) {
			if(i ==0){
				xVals.add("翻线(m)");
			}else{
				xVals.add(getActivity().getString(R.string.in_lasthour));
			}
		}

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues1[i];
			yVals1.add(new BarEntry(val, i));
		}
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues2[i];
			yVals2.add(new BarEntry(val, i));
		}

		// create 2 datasets with different types
		BarDataSet set1 = new BarDataSet(yVals1, "实际");
		set1.setColor(Color.parseColor("#88BA55"));
		set1.setValueTextColor(Color.parseColor("#ffffff"));
		set1.setValueTextSize(8f);
		set1.setHighlightEnabled(false);
		set1.setValueFormatter(numValueFormatter);

		BarDataSet set2 = new BarDataSet(yVals2, "标准");
		set2.setColor(Color.parseColor("#1EB1ED"));
		set2.setValueTextColor(Color.parseColor("#ffffff"));
		set2.setValueTextSize(8f);
		set2.setValueFormatter(numValueFormatter);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);
		dataSets.add(set2);

		BarData data = new BarData(xVals, dataSets);
		data.setGroupSpace(80f);
		changeLineBarChart.setData(data);
		changeLineBarChart.invalidate();
	}

	/**
	 * pph
	 * 为BarChart绑定值
	 * @param barChartNum
	 * @param yValues1
	 * @param yValues2
     */
	public void getPPHChartValue(int barChartNum,float[] yValues1,float[] yValues2){
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < barChartNum; i++) {
			//xVals.add((i+1990) + "");
			if(i ==0){
				xVals.add("PPH");
			}else{
				xVals.add("");
			}
		}

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues1[i];
			yVals1.add(new BarEntry(val, i));
		}
		for (int i = 0; i < barChartNum; i++) {
			float val = (float) yValues2[i];
			yVals2.add(new BarEntry(val, i));
		}

		// create 2 datasets with different types
		BarDataSet set1 = new BarDataSet(yVals1, "实际");
		set1.setColor(Color.parseColor("#88BA55"));
		set1.setValueTextColor(Color.parseColor("#FFFFFF"));
		set1.setValueTextSize(8f);
		set1.setHighlightEnabled(false);
		set1.setValueFormatter(valueFormatterForFolat);

		BarDataSet set2 = new BarDataSet(yVals2, "标准");
		set2.setColor(Color.parseColor("#1EB1ED"));
		set2.setValueTextColor(Color.parseColor("#FFFFFF"));
		set2.setValueTextSize(8f);
		set2.setValueFormatter(valueFormatterForFolat);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);
		dataSets.add(set2);

		BarData data = new BarData(xVals, dataSets);
		data.setGroupSpace(80f);
		pphBarChart.setData(data);
		pphBarChart.invalidate();
	}





	/**
	 * 定义柱状数据显示的数据格式
	 */
	ValueFormatter valueFormatter = new ValueFormatter() {

		@Override
		public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
			// TODO Auto-generated method stub
			if((int)value != 0){
				return ""+(int)value;
			}else{
				return "";
			}
		}
	};

	/**
	 * 定义直接显示数值format
	 */
	ValueFormatter numValueFormatter = new ValueFormatter() {
		@Override
		public String getFormattedValue(float value, Entry entry, int i, ViewPortHandler viewPortHandler) {
			return ""+(int)value;
		}
	};

	/**
	 * 定义柱状数据显示的数据格式 for float
	 */
	ValueFormatter valueFormatterForFolat = new ValueFormatter() {

		@Override
		public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
			// TODO Auto-generated method stub
			return ""+value;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.productionStatisticsBarChart:
			case R.id.achieveRateBarChart:
			case R.id.changeLineBarChart:
			case R.id.pphBarChart:
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra("enum", DetailActivity.FragmentEnum.ChartDetail);
				startActivity(intent);
				break;
		}
	}
}
