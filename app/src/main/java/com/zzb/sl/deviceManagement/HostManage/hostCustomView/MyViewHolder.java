package com.zzb.sl.deviceManagement.HostManage.hostCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView hostlist_item_content;
    public TextView hostlist_item_delete;
    public LinearLayout hostlist_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        hostlist_item_content = (TextView) itemView.findViewById(R.id.hostlist_item_content);
        hostlist_item_delete = (TextView) itemView.findViewById(R.id.hostlist_item_delete);
        hostlist_item_layout = (LinearLayout) itemView.findViewById(R.id.hostlist_item_layout);

    }
}
