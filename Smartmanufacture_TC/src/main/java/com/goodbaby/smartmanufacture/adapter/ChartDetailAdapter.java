package com.goodbaby.smartmanufacture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import com.goodbaby.smartmanufacture.R;

import java.util.List;

public class ChartDetailAdapter extends BaseAdapter{
	private List<List<String>> mList;
	private Context mContext;

	public ChartDetailAdapter(Context context,List<List<String>> list){
		mContext = context;
		mList = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mList.size() != 0){
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return mList.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		TextView tv = null;
		List<String> lineList = mList.get(position);
		if(view == null){
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.view_linear, null);
			holder.layout_data = (LinearLayout)view.findViewById(R.id.linear);
			LinearLayout layout = new LinearLayout(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layout.setLayoutParams(params);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setPadding(50, 20, 20, 20);
			for(int k=0;k<lineList.size();k++){
				tv = new TextView(mContext);
				tv.setText(lineList.get(k));
				layout.addView(tv, params);
				
			}
			
			holder.layout_data.addView(layout, params);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}

		
		return view;
	}
	
	class ViewHolder{
		LinearLayout layout_data;
	}

}
