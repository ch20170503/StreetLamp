package com.zzb.fragment.message.messageitem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zzb.bean.Alarm;
import com.zzb.sl.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MyAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    //创建Bean
    private List<Alarm> hosts = new ArrayList<>();

    private boolean b = false;
    public MyAdapter(Context context, List<Alarm> easyBeen) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        hosts = easyBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.recyclerview_item_layout_alertlist, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        Alarm host = hosts.get(position);
        int type = host.getsS_Type();
        //注册包或者分控编号
        String hostpack = host.getsS_ItemId();
        switch (type){
            case 0:
                viewHolder.al_item_content.setText("\r\r\r"+R.string.host+"\n"+hostpack);
                viewHolder.alarmType.setText(R.string.Offline);
                break;
            case 1:
                viewHolder.al_item_content.setText("\r\r\r"+R.string.host+"\n"+hostpack);
                viewHolder.alarmType.setText(R.string.Online);
                break;
            case 2:
                viewHolder.al_item_content.setText("\r\r\r"+R.string.host+"\n"+hostpack);
                viewHolder.alarmType.setText(R.string.DataException);
                break;
            case 3:
                viewHolder.al_item_content.setText("\r\r\r"+R.string.SubControl+"\n"+hostpack);
                viewHolder.alarmType.setText(R.string.DataException);
                break;
        }
        String [] arr = host.getsS_Time().split("T");
        String m = arr[1];
        String [] ms = m.split(":");
        String s = ms[0]+":"+ms[1];
        viewHolder.alarmTime.setText(arr[0]+"\n\r\r\r\r\r"+s);

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
