package com.zzb.sl.UserBasicsInfo.OperatorActivity.opreatorCustomView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.zzb.bean.SelectUser;
import com.zzb.sl.R;
import com.zzb.util.MyViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    //创建Bean
    private List<SelectUser> listSc = new ArrayList<>();

    private boolean b = false;
    private SelectUser sceneInfo;


    public MyAdapter(Context context, List<SelectUser> easyBeen) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        listSc = easyBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyViewHolder(mInflater.inflate(R.layout.recyclerview_item_layout_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        SelectUser sceneInfo = listSc.get(position);
        viewHolder.content.setText(sceneInfo.getsS_Account());
    }

    @Override
    public int getItemCount() {
        return listSc != null ? listSc.size() : 0;
    }

    public void removeItem(int position) {
            Log.e(TAG, "这是选择数据的下标" + position);
            //删除RecyclerView列表对应item
            listSc.remove(position);
            notifyDataSetChanged();
    }

}
