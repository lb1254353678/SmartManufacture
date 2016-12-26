package com.goodbaby.smartmanufacture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.goodbaby.smartmanufacture.data.NetWorkUtil;
import com.goodbaby.smartmanufacture.data.Utils;
import com.goodbaby.smartmanufacture.global.Constant;
import com.goodbaby.smartmanufacture.global.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupNameSelectedActivity extends Activity{
	private MyApplication myApplication;
	private ListView lv_groupname;
	private LinearLayout no_network,no_data;
	private HashMap<Integer, HashMap<Integer,String>> map;
	private ArrayList<String> groupIDList,groupNameList;
	private String[] errorMessage = new String[1];
	private ProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_groupname_selected);
		myApplication = (MyApplication)getApplication();

		lv_groupname = (ListView)findViewById(R.id.lv_groupname);
		no_data = (LinearLayout)findViewById(R.id.no_data);
		no_network = (LinearLayout)findViewById(R.id.no_network);

		getGroupName();
		lv_groupname.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GroupNameSelectedActivity.this,MainActivity.class);
				myApplication.XBNAME = adapterView.getAdapter().getItem(position).toString();
				myApplication.XBID = groupIDList.get(position).toString();
				startActivity(intent);
				//以TabLayout模式时,调用下面代码
//				Intent intent = new Intent(GroupNameSelectedActivity.this,MainViewPagerActivity.class);
//				intent.putExtra("ID", position);
//				intent.putStringArrayListExtra("LIST", groupIDList);
//				startActivity(intent);
			}
		});

		no_network.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getGroupName();
			}
		});
	}



	private void getGroupName() {
		final String sqlText = Constant.sql_getGroupName;
		errorMessage[0] = "";

		if(!Utils.isNetworkAvailable(GroupNameSelectedActivity.this)){
			no_network.setVisibility(View.VISIBLE);
			lv_groupname.setVisibility(View.GONE);
			return;
		}
		/**
		 * 设置progressDialog的基本样式
		 */
		mProgressDialog = new ProgressDialog(GroupNameSelectedActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
		mProgressDialog.setMessage(getString(R.string.load_progressing));
		mProgressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Utils.spandTimeMethod(); //模拟耗时操作，防止progressDialog消失过快
				map = NetWorkUtil.getData(Constant.SERVERNAME, Constant.INITIALCATALOG_ADX, sqlText, errorMessage);
				if(map != null){
					myHandler.sendEmptyMessage(Constant.SUCCESS);
				}else{
					myHandler.sendEmptyMessage(Constant.FAILED);
				};
			}
		}).start();
	}

	Handler myHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Constant.SUCCESS:
					no_network.setVisibility(View.GONE);
					lv_groupname.setVisibility(View.VISIBLE);

					groupIDList = new ArrayList<String>();
					groupNameList = new ArrayList<String>();
					for(int i=0;i<map.size();i++){
						groupIDList.add(map.get(i).get(0));
						groupNameList.add(map.get(i).get(1));
					}
					lv_groupname.setAdapter(new MyAdapter(groupNameList));
					mProgressDialog.dismiss();
					break;

				case Constant.FAILED:
					mProgressDialog.dismiss();
					lv_groupname.setVisibility(View.GONE);
					no_data.setVisibility(View.VISIBLE);
					Toast.makeText(GroupNameSelectedActivity.this, "暂无线别", Toast.LENGTH_SHORT).show();
					break;

			}
		};
	};



	class MyAdapter extends BaseAdapter{
		private List<String> mList;

		public MyAdapter(List<String> list){
			this.mList = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size()==0 ? 0 : mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int id, View view, ViewGroup viewGroup) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(view == null){
				holder = new ViewHolder();
				view = LayoutInflater.from(GroupNameSelectedActivity.this).inflate(R.layout.item_textview, null);
				holder.tv_groupname = (TextView)view.findViewById(R.id.tv_groupname);
				view.setTag(holder);
			}else{
				holder = (ViewHolder)view.getTag();
			}
			holder.tv_groupname.setText(mList.get(id));


			return view;
		}

		class ViewHolder{
			TextView tv_groupname;
		}

	}

}
