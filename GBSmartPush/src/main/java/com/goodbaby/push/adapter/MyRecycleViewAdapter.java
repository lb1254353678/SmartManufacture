package com.goodbaby.push.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goodbaby.push.R;
import com.goodbaby.push.model.PushMessageModel;

import java.util.List;

/**
 * Created by goodbaby on 17/2/6.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {
    private Context mContext;
    private List<PushMessageModel> mList;

    public MyRecycleViewAdapter(Context context, List<PushMessageModel> list){
        mContext = context;
        mList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView tv_from,tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tv_from = (TextView)itemView.findViewById(R.id.tv_from_user);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_message_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_from.setText(mList.get(position).getFrom_user());
        holder.tv_content.setText(mList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mList.size() == 0 ? 0 : mList.size();
    }


}
