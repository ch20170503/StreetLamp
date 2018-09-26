package com.zzb.sl.deviceManagement.SubManage.subhostCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView subhostlist_item_content;
    public LinearLayout subhostlist_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        subhostlist_item_content = (TextView) itemView.findViewById(R.id.subhostlist_item_content);
        subhostlist_item_layout = (LinearLayout) itemView.findViewById(R.id.subhostlist_item_layout);

    }
}
