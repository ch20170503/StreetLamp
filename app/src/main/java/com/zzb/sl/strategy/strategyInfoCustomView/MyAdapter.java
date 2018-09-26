package com.zzb.sl.strategy.strategyInfoCustomView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zzb.bean.Strategy;
import com.zzb.sl.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    //创建Bean
    private List<Strategy> hosts = new ArrayList<>();

    private boolean b = false;
    private Strategy host;


    public MyAdapter(Context context, List<Strategy> easyBeen) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        hosts = easyBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyViewHolder(mInflater.inflate(R.layout.recyclerview_item_layout_strategylist, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        Strategy host = hosts.get(position);
        viewHolder.strategy_item_content.setText(host.getsS_Number()+"");
        viewHolder.strategy_item_content2.setText(host.getsS_FullName());
    }
    @Override
    public int getItemCount() {
        return hosts != null ? hosts.size() : 0;
    }
    public void removeItem(int position) {
        Log.e(TAG, "这是选择数据的下标" + position);
        //删除RecyclerView列表对应item
        hosts.remove(position);
        notifyDataSetChanged();
    }
}
