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
import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.R;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * U、Z站站点信息
 * @author XXZX_LB
 *
 */
@SuppressLint("NewApi")
public class SiteLayoutDetailFragment extends Fragment{

	private TextView tv_title;
	private LineChart lineChart;
	private TableLayout table;
	private HashMap<Integer, HashMap<Integer, String>> map_real_standard,map_exception,map_standard;
	private String[] xValue = {"08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00"};
	public String[] errorMessage = new String[1];
	private ProgressDialog mProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_site_layout_detail, null);
		init(view);
		chartRender();
		getSiteLayoutDetail(SiteLayoutFragment.site);
		return view;
	}

	public void init(View view){
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		lineChart = (LineChart)view.findViewById(R.id.lineChart);
		table = (TableLayout)view.findViewById(R.id.table);
		tv_title.setText(MyApplication.XBNAME + SiteLayoutFragment.site + getActivity().getString(R.string.SiteLayoutDetailFragment_2));
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

		//动画时间
		lineChart.animateX(1000);


		// get the legend (only possible after setting data)
		Legend l = lineChart.getLegend();
		l.setEnabled(true);
		l.setPosition(LegendPosition.LEFT_OF_CHART_CENTER);
		l.setTextColor(Color.WHITE);
		l.setForm(LegendForm.LINE);

		XAxis xAxis = lineChart.getXAxis();
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
		rightAxis.setDrawGridLines(true);
		rightAxis.setDrawAxisLine(false);

	}

	private void setChartData(String[] xValue,int[] y_exception,int[] y_real,int[] y_standard) {
		//“实际”折线图数据   **红色线条**
		ArrayList<Entry> yVals_real = new ArrayList<Entry>();
		for(int i=0;i<y_real.length;i++){
			float val = (float)y_real[i];
			yVals_real.add(new BarEntry(val, i));
		}

		LineDataSet set_real = new LineDataSet(yVals_real, getActivity().getString(R.string.count_real));
		set_real.setAxisDependency(AxisDependency.LEFT);
		set_real.setColor(getActivity().getResources().getColor(R.color.red));
		set_real.setCircleColor(getActivity().getResources().getColor(R.color.red));
		set_real.setDrawCircles(false);
		set_real.setLineWidth(2f);
		set_real.setCircleSize(3f);
		set_real.setFillAlpha(65);
		set_real.setFillColor(getActivity().getResources().getColor(R.color.red));
		set_real.setHighLightColor(getActivity().getResources().getColor(R.color.red));
		set_real.setDrawCircleHole(false);

		//“标准”折线图数据   **蓝色线条**
		ArrayList<Entry> yVals_standard = new ArrayList<Entry>();
		for(int i=0;i<y_standard.length;i++){
			float val = (float)y_standard[i];
			yVals_standard.add(new BarEntry(val, i));
		}

		LineDataSet set_standard = new LineDataSet(yVals_standard, getActivity().getString(R.string.count_target));
		set_standard.setAxisDependency(AxisDependency.LEFT);
		set_standard.setColor(getActivity().getResources().getColor(R.color.blue));
		set_standard.setCircleColor(getActivity().getResources().getColor(R.color.blue));
		set_standard.setDrawCircles(false);
		set_standard.setLineWidth(2f);
		set_standard.setCircleSize(3f);
		set_standard.setFillAlpha(65);
		set_standard.setFillColor(getActivity().getResources().getColor(R.color.blue));
		set_standard.setHighLightColor(getActivity().getResources().getColor(R.color.blue));
		set_standard.setDrawCircleHole(false);

		//“异常”折线图数据  **绿色线条**
		ArrayList<Entry> yVals_exception = new ArrayList<Entry>();
		for(int i=0;i<y_exception.length;i++){
			float val = (float)y_exception[i];
			yVals_exception.add(new BarEntry(val, i));
		}

		LineDataSet set_exception = new LineDataSet(yVals_exception, getActivity().getString(R.string.count_exception));
		set_exception.setAxisDependency(AxisDependency.LEFT);
		set_exception.setColor(getActivity().getResources().getColor(R.color.green));
		set_exception.setCircleColor(getActivity().getResources().getColor(R.color.green));
		set_exception.setDrawCircles(false);
		set_exception.setLineWidth(2f);
		set_exception.setCircleSize(3f);
		set_exception.setFillAlpha(0);
		set_exception.setFillColor(getActivity().getResources().getColor(R.color.green));
		set_exception.setDrawCircleHole(false);
		set_exception.setHighLightColor(getActivity().getResources().getColor(R.color.green));

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set_exception);
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

	public void getSiteLayoutDetail(final String siteName){

		errorMessage[0] = "";
		map_real_standard = new HashMap<Integer, HashMap<Integer,String>>();
		map_exception = new HashMap<Integer, HashMap<Integer,String>>();

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
					map_real_standard = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, Constant.sql_site_real_standard_detail(MyApplication.XBID, siteName), errorMessage);
					map_exception = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, Constant.sql_site_exception_detail(MyApplication.XBID, siteName), errorMessage);
					//Log.v("***map_real_standard***", ""+map_real_standard);
					Log.v("***map_exception***", ""+map_exception);
					if(map_real_standard != null && map_exception != null){
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

		int[] yValues_real = new int[13];
		int[] yValues_Standard = new int[13];
		int[] yValues_exception = new int[13];
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case Constant.SUCCESS:
					for(int i=0;i<map_real_standard.size();i++){
						if(map_real_standard.get(i).get(2).equals("08:00")){
							yValues_real[0] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[0] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("09:00")){
							yValues_real[1] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[1] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("10:00")){
							yValues_real[2] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[2] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("11:00")){
							yValues_real[3] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[3] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("12:00")){
							yValues_real[4] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[4] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("13:00")){
							yValues_real[5] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[5] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("14:00")){
							yValues_real[6] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[6] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("15:00")){
							yValues_real[7] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[7] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("16:00")){
							yValues_real[8] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[8] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("17:00")){
							yValues_real[9] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[9] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("18:00")){
							yValues_real[10] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[10] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("19:00")){
							yValues_real[11] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[11] = Integer.valueOf(map_real_standard.get(i).get(3));
						}else if(map_real_standard.get(i).get(2).equals("20:00")){
							yValues_real[12] = Integer.valueOf(map_real_standard.get(i).get(1));
							yValues_Standard[12] = Integer.valueOf(map_real_standard.get(i).get(3));
						}
					}

					//{0={0=14, 1=U8, 2=08:00}, 1={0=12, 1=U8, 2=09:00}}
					for(int i=0;i<map_exception.size();i++){
						if(map_exception.get(i).get(1).equals("08:00")){
							yValues_exception[0] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("09:00")){
							yValues_exception[1] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("10:00")){
							yValues_exception[2] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("11:00")){
							yValues_exception[3] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("12:00")){
							yValues_exception[4] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("13:00")){
							yValues_exception[5] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("14:00")){
							yValues_exception[6] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("15:00")){
							yValues_exception[7] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("16:00")){
							yValues_exception[8] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("17:00")){
							yValues_exception[9] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("18:00")){
							yValues_exception[10] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("19:00")){
							yValues_exception[11] = Integer.valueOf(map_exception.get(i).get(0));
						}else if(map_exception.get(i).get(1).equals("20:00")){
							yValues_exception[12] = Integer.valueOf(map_exception.get(i).get(0));
						}
					}

					setChartData(xValue,yValues_exception, yValues_real, yValues_Standard);
					setDetail(xValue,yValues_exception, yValues_real, yValues_Standard);

					mProgressDialog.dismiss();
					break;

				case Constant.FAILED:
					mProgressDialog.dismiss();
					Toast.makeText(getActivity(), ""+errorMessage[0], Toast.LENGTH_SHORT).show();
					break;

				case Constant.DEBUG:

					int[] yValues_exception_debug = {0,6,4,2,8,1,5,5,0,0,0,0,0};
					int[] yValues_real_debug = {10,18,20,20,18,30,35,30,0,0,0,0,0};
					int[] yValues_Standard_debug = {20,20,30,30,35,30,36,30,0,0,0,0,0};
					setChartData(xValue,yValues_exception_debug, yValues_real_debug, yValues_Standard_debug);
					setDetail(xValue,yValues_exception_debug, yValues_real_debug, yValues_Standard_debug);
					break;
			}
		}
	};
	/**
	 * 为表格赋值
	 * @param y_exception
	 * @param y_real
	 * @param y_standard
	 */
	private void setDetail(String[] y_date,int[] y_exception,int[] y_real,int[] y_standard){

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

		TableRow tabrow_real = new TableRow(getActivity());
		for(int i=0;i<y_real.length + 1;i++){
			tv = new TextView(getActivity());
			if(i==0){
				tv.setText(getActivity().getString(R.string.count_real));
			}else{
				tv.setText(y_real[i-1] + "");
			}
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackground(getActivity().getResources().getDrawable(R.drawable.shape));
			tabrow_real.addView(tv);
		}

		TableRow tabrow_standard = new TableRow(getActivity());
		for(int i=0;i<y_standard.length + 1;i++){
			tv = new TextView(getActivity());
			if(i==0){
				tv.setText(getActivity().getString(R.string.count_target));
			}else{
				tv.setText(y_standard[i-1] + "");
			}
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackground(getActivity().getResources().getDrawable(R.drawable.shape));
			tabrow_standard.addView(tv);
		}

		TableRow tabrow_exception = new TableRow(getActivity());
		for(int i=0;i<y_exception.length + 1;i++){
			tv = new TextView(getActivity());
			if(i==0){
				tv.setText(getActivity().getString(R.string.count_exception));
			}else{
				tv.setText(y_exception[i-1] + "");
			}
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackground(getActivity().getResources().getDrawable(R.drawable.shape));
			tabrow_exception.addView(tv);
		}
		table.addView(tabrow_date);
		table.addView(tabrow_exception);
		table.addView(tabrow_real);
		table.addView(tabrow_standard);

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
