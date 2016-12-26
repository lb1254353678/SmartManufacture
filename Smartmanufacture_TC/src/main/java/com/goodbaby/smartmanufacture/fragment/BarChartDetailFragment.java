package com.goodbaby.smartmanufacture.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 产能效率报表
 * @author XXZX_LB
 *
 */
@SuppressLint("NewApi")
public class BarChartDetailFragment extends Fragment{
	private TextView tv_title;
	private LineChart lineChart;
	private TableLayout table;
	private String[] xValue = {"08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00"};
	private HashMap<Integer, HashMap<Integer, String>> map;
	public String[] errorMessage = new String[1];
	private ProgressDialog mProgressDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_chart_detail, null);
		init(view);
		chartRender();
		//getChartDetail_debug();//test function
		getChartDetail();
		return view;
	}

	public void init(View view){
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		lineChart = (LineChart)view.findViewById(R.id.lineChart);
		table = (TableLayout)view.findViewById(R.id.table);
		//tv_title.setText(getActivity().getString(R.string.ChartDetailFragment));
		tv_title.setText(MyApplication.XBNAME + "效率");
	}

	public void chartRender(){
		lineChart.setDescription("");
		lineChart.setNoDataTextDescription("You need to provide data for the chart.");
		// enable value highlighting
		lineChart.setHighlightEnabled(true);
		// enable touch gestures
		lineChart.setTouchEnabled(true);
		lineChart.setDragDecelerationFrictionCoef(0.9f);

		// enable scaling and dragging
		lineChart.setDragEnabled(false);
		lineChart.setScaleEnabled(false);
		lineChart.setDoubleTapToZoomEnabled(false);
		lineChart.setDrawGridBackground(false);
		lineChart.setHighlightPerDragEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		lineChart.setPinchZoom(true);

		// set an alternative background color
		lineChart.setBackgroundColor(getActivity().getResources().getColor(R.color.gray));
		// add data
		//======================
		//======================
		//setData(13);

		//动画时间
		lineChart.animateX(1000);

		//Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		// get the legend (only possible after setting data)
		Legend l = lineChart.getLegend();
		l.setEnabled(true);
		l.setPosition(LegendPosition.LEFT_OF_CHART_CENTER);
		l.setTextColor(Color.WHITE);
		l.setForm(LegendForm.LINE);

		XAxis xAxis = lineChart.getXAxis();
		//xAxis.setTypeface(tf);
		xAxis.setTextSize(12f);
		xAxis.setTextColor(Color.WHITE);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(true);
		xAxis.setPosition(XAxisPosition.BOTTOM);

		YAxis leftAxis = lineChart.getAxisLeft();
		leftAxis.setTextColor(getActivity().getResources().getColor(R.color.white));
		leftAxis.setDrawGridLines(true);
		leftAxis.setDrawAxisLine(false);

		YAxis rightAxis = lineChart.getAxisRight();
		rightAxis.setTextColor(getActivity().getResources().getColor(R.color.white));
		rightAxis.setDrawGridLines(false);
		rightAxis.setDrawAxisLine(false);
		rightAxis.setAxisMaxValue(100f);
		rightAxis.setValueFormatter(new YAxisValueFormatter() {

			@Override
			public String getFormattedValue(float value, YAxis yAxis) {
				// TODO Auto-generated method stub
				return value + "%";
			}
		});
	}

	//设置图表
	private void setChartData(String[] xValue,int[] y_real,int[] y_standard,int[] y_efficiency) {
		//X轴坐标
        /*ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("08:00");
        xVals.add("09:00");
        xVals.add("10:00");
        xVals.add("11:00");
        xVals.add("12:00");
        xVals.add("13:00");
        xVals.add("14:00");
        xVals.add("15:00");
        xVals.add("16:00");
        xVals.add("17:00");
        xVals.add("18:00");
        xVals.add("19:00");
        xVals.add("20:00");*/

		//“实际”折线图数据   **红色线条**
		ArrayList<Entry> yVals_real = new ArrayList<Entry>();
		for(int i=0;i<y_real.length;i++){
			//float val = Float.valueOf(y_real[i]);
			float val = (float)y_real[i];
			yVals_real.add(new BarEntry(val, i));
		}

		LineDataSet set_real = new LineDataSet(yVals_real, getActivity().getString(R.string.real));
		set_real.setAxisDependency(AxisDependency.LEFT);
		set_real.setColor(getActivity().getResources().getColor(R.color.red));
		set_real.setCircleColor(getActivity().getResources().getColor(R.color.red));
		set_real.setLineWidth(2f);
		set_real.setCircleSize(3f);
		set_real.setFillAlpha(65);
		set_real.setFillColor(getActivity().getResources().getColor(R.color.red));
		set_real.setHighLightColor(getActivity().getResources().getColor(R.color.red));
		set_real.setDrawCircleHole(false);

		//“目标”折线图数据   **蓝色线条**
		ArrayList<Entry> yVals_standard = new ArrayList<Entry>();
		for(int i=0;i<y_standard.length;i++){
			float val = (float)y_standard[i];
			yVals_standard.add(new BarEntry(val, i));
		}

		LineDataSet set_standard = new LineDataSet(yVals_standard, getActivity().getString(R.string.target));
		set_standard.setAxisDependency(AxisDependency.LEFT);
		set_standard.setColor(getActivity().getResources().getColor(R.color.blue));
		set_standard.setCircleColor(getActivity().getResources().getColor(R.color.blue));
		set_standard.setLineWidth(2f);
		set_standard.setCircleSize(3f);
		set_standard.setFillAlpha(65);
		set_standard.setFillColor(getActivity().getResources().getColor(R.color.blue));
		set_standard.setHighLightColor(getActivity().getResources().getColor(R.color.blue));
		set_standard.setDrawCircleHole(false);

		//“效率”折线图数据  **绿色线条**
		ArrayList<Entry> yVals_efficiency = new ArrayList<Entry>();
		for(int i=0;i<y_efficiency.length;i++){
			float val = (float)y_efficiency[i];
			yVals_efficiency.add(new BarEntry(val, i));
		}

		LineDataSet set_efficiency = new LineDataSet(yVals_efficiency, getActivity().getString(R.string.efficiency));
		set_efficiency.setAxisDependency(AxisDependency.RIGHT);
		set_efficiency.setColor(getActivity().getResources().getColor(R.color.green));
		set_efficiency.setCircleColor(getActivity().getResources().getColor(R.color.green));
		set_efficiency.setLineWidth(2f);
		set_efficiency.setCircleSize(3f);
		set_efficiency.setFillAlpha(0);
		set_efficiency.setFillColor(getActivity().getResources().getColor(R.color.green));
		set_efficiency.setDrawCircleHole(false);
		set_efficiency.setHighLightColor(getActivity().getResources().getColor(R.color.green));
		//set2.setFillFormatter(new MyFillFormatter(900f));

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set_efficiency);
		dataSets.add(set_real);
		dataSets.add(set_standard);// add the datasets

		// create a data object with the datasets
		LineData data = new LineData(xValue, dataSets);
		data.setDrawValues(false);//设置不显示数值
        /*data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);*/

		// set data
		lineChart.setData(data);
		lineChart.invalidate();
	}

	public void getChartDetail(){
		errorMessage[0] = "";
		map = new HashMap<Integer, HashMap<Integer,String>>();
		final String sqlText = Constant.sql_chart_detail(MyApplication.XBID);
		//Log.v("sql", ""+sqlText);
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
					Log.v("***map***", "map:"+map);
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

	public void getChartDetail_debug(){
		myHandler.sendEmptyMessage(Constant.DEBUG);
	}

	Handler myHandler = new Handler(){
		int[] yValues_Standard = new int[13];
		int[] yValues_real = new int[13];
		int[] yValues_efficiency = new int[13];
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:
				/*map:{0={0=47, 1=Z12016090702, 2=00309D0007, 3=T1A002, 4=2016-09-12 08:00:00, 5=08:00, 6=60},
				1={0=60, 1=Z12016090702, 2=00309D0007, 3=T1A002, 4=2016-09-12 09:00:00, 5=09:00, 6=60},
				2={0=15, 1=Z12016090702, 2=00309D0007, 3=T1A002, 4=2016-09-12 10:00:00, 5=10:00, 6=60}}*/

					//只有一行数据，有可能无有效数值，有可能只有一行有效数值
					if(map.size() == 1){
						if(map.get(0).get(0).equals("")){
							for(int i=0;i<yValues_real.length;i++){
								yValues_real[i] = 0;
								yValues_Standard[i] = Integer.valueOf(map.get(0).get(6));
								yValues_efficiency[i] = 0;
							}
						}else{
							if(map.get(0).get(3).equals("08:00")){
								yValues_real[0] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[0] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[0] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("09:00")){
								yValues_real[1] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[1] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[1] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("10:00")){
								yValues_real[2] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[2] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[2] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("11:00")){
								yValues_real[3] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[3] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[3] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("12:00")){
								yValues_real[4] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[4] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[4] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("13:00")){
								yValues_real[5] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[5] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[5] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("14:00")){
								yValues_real[6] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[6] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[6] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("15:00")){
								yValues_real[7] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[7] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[7] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("16:00")){
								yValues_real[8] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[8] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[8] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("17:00")){
								yValues_real[9] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[9] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[9] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("18:00")){
								yValues_real[10] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[10] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[10] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("19:00")){
								yValues_real[11] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[11] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[11] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}else if(map.get(0).get(3).equals("20:00")){
								yValues_real[12] = Integer.valueOf(map.get(0).get(0));
								yValues_Standard[12] = Integer.valueOf(map.get(0).get(4));
								yValues_efficiency[12] = (int) ((Float.valueOf(map.get(0).get(0)) / Float.valueOf(map.get(0).get(4))) * 100);
							}
						}
					}else {
						//含有多行数据时
						for(int i=0;i<map.size();i++){
							if(map.get(i).get(3).equals("08:00")){
								yValues_real[0] =Integer.valueOf(map.get(i).get(0));
								yValues_Standard[0] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[0] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100) ;
								//Toast.makeText(getActivity(), "08:00:" + (Float.valueOf(map.get(i).get(0))/Float.valueOf(map.get(i).get(6))) * 100, Toast.LENGTH_SHORT).show();
							}else if(map.get(i).get(3).equals("09:00")){
								yValues_real[1] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[1] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[1] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
								//Toast.makeText(getActivity(), "09:00:" + (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(6))) * 100, Toast.LENGTH_SHORT).show();
							}else if(map.get(i).get(3).equals("10:00")){
								yValues_real[2] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[2] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[2] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("11:00")){
								yValues_real[3] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[3] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[3] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("12:00")){
								yValues_real[4] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[4] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[4] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("13:00")){
								yValues_real[5] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[5] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[5] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("14:00")){
								yValues_real[6] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[6] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[6] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("15:00")){
								yValues_real[7] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[7] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[7] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("16:00")){
								yValues_real[8] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[8] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[8] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("17:00")){
								yValues_real[9] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[9] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[9] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("18:00")){
								yValues_real[10] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[10] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[10] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("19:00")){
								yValues_real[11] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[11] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[11] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}else if(map.get(i).get(3).equals("20:00")){
								yValues_real[12] = Integer.valueOf(map.get(i).get(0));
								yValues_Standard[12] = Integer.valueOf(map.get(i).get(4));
								yValues_efficiency[12] = (int) (Float.valueOf(map.get(i).get(0)) / Float.valueOf(map.get(i).get(4)) * 100);
							}
						}
					}
					setChartData(xValue,yValues_real, yValues_Standard, yValues_efficiency);
					setDetail(xValue, yValues_Standard, yValues_real, yValues_efficiency);
					mProgressDialog.dismiss();
					break;

				case Constant.FAILED:
					mProgressDialog.dismiss();
					Toast.makeText(getActivity(), ""+errorMessage[0], Toast.LENGTH_SHORT).show();
					break;
				case Constant.DEBUG:
					yValues_real = new int[]{10,12,17,21,15,20,19,25,21,0,0,0,0};
					yValues_Standard = new int[]{20,20,30,30,20,20,35,35,30,0,0,0,0};
					yValues_efficiency = new int[]{50,60,85,70,75,100,54,71,70,0,0,0,0};
					setChartData(xValue,yValues_real, yValues_Standard, yValues_efficiency);
					setDetail(xValue, yValues_Standard, yValues_real, yValues_efficiency);
					break;
			}
		}
	};

	//设置表格
	private void setDetail(String[] y_date,int[] y_standard,int[] y_real,int[] y_efficy){

		TextView tv = null;
		TableRow tabrow_date = new TableRow(getActivity());
		for(int i=0;i<y_date.length + 1;i++){
			tv = new TextView(getActivity());
			if(i==0){
				tv.setText(getActivity().getString(R.string.time));
			}else{
				tv.setText(y_date[i-1]);
			}
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackground(getActivity().getResources().getDrawable(R.drawable.shape));
			tabrow_date.addView(tv);
		}

		TableRow tabrow_standard = new TableRow(getActivity());
		for(int i=0;i<y_standard.length + 1;i++){
			tv = new TextView(getActivity());
			if(i==0){
				tv.setText(getActivity().getString(R.string.standard));
			}else{
				tv.setText(y_standard[i-1] + "");
			}
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackground(getActivity().getResources().getDrawable(R.drawable.shape));
			tabrow_standard.addView(tv);
		}

		TableRow tabrow_real = new TableRow(getActivity());
		for(int i=0;i<y_real.length + 1;i++){
			tv = new TextView(getActivity());
			if(i==0){
				tv.setText(getActivity().getString(R.string.real));
			}else{
				tv.setText(y_real[i-1] + "");
			}
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackground(getActivity().getResources().getDrawable(R.drawable.shape));
			tabrow_real.addView(tv);
		}

		TableRow tabrow_efficy = new TableRow(getActivity());
		for(int i=0;i<y_efficy.length + 1;i++){
			tv = new TextView(getActivity());
			if(i==0){
				tv.setText(getActivity().getString(R.string.efficiency));
			}else{
				tv.setText(y_efficy[i-1] + "%");
			}
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackground(getActivity().getResources().getDrawable(R.drawable.shape));
			tabrow_efficy.addView(tv);
		}
		table.addView(tabrow_date);
		table.addView(tabrow_standard);
		table.addView(tabrow_real);
		table.addView(tabrow_efficy);
	}



	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		public Drawable getDrawable(String source) {
			Drawable drawable=null;
			int rId=Integer.parseInt(source);
			drawable=getResources().getDrawable(rId);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			return drawable;
		};
	};

}
