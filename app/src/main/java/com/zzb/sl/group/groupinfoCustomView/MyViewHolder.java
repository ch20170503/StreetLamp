package com.zzb.sl.group.groupinfoCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView grouplist_item_content;
    public LinearLayout grouplist_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        grouplist_item_content = (TextView) itemView.findViewById(R.id.grouplist_item_content);
        grouplist_item_layout = (LinearLayout) itemView.findViewById(R.id.grouplist_item_layout);

    }
}
