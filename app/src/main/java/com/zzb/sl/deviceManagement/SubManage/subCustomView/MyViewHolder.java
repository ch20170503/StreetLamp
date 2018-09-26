package com.zzb.sl.deviceManagement.SubManage.subCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView sublist_item_content;
    public TextView sublist_item_delete;
    public LinearLayout sublist_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        sublist_item_content = (TextView) itemView.findViewById(R.id.sublist_item_content);
        sublist_item_delete = (TextView) itemView.findViewById(R.id.sublist_item_delete);
        sublist_item_layout = (LinearLayout) itemView.findViewById(R.id.sublist_item_layout);

    }
}
