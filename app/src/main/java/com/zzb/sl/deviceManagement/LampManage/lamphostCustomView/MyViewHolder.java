package com.zzb.sl.deviceManagement.LampManage.lamphostCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView lamphostlist_item_content;
    public LinearLayout lamphostlist_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        lamphostlist_item_content = (TextView) itemView.findViewById(R.id.lamphostlist_item_content);
        lamphostlist_item_layout = (LinearLayout) itemView.findViewById(R.id.lamphostlist_item_layout);

    }
}
