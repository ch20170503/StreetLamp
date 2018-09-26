package com.zzb.sl.deviceManagement.LampManage.lampCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView lamplist_item_content;
    public LinearLayout lamplist_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        lamplist_item_content = (TextView) itemView.findViewById(R.id.lamplist_item_content);
        lamplist_item_layout = (LinearLayout) itemView.findViewById(R.id.lamplist_item_layout);

    }
}
