package com.zzb.sl.deviceManagement.SubManage.subCustomView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.sl.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    //创建Bean
    private List<ControlS> hosts = new ArrayList<>();

    private boolean b = false;
    private Host host;


    public MyAdapter(Context context, List<ControlS> easyBeen) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        hosts = easyBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyViewHolder(mInflater.inflate(R.layout.recyclerview_item_layout_sublist, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        ControlS host = hosts.get(position);
        viewHolder.sublist_item_content.setText(host.getsS_FullName());
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
