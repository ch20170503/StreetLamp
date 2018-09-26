package com.zzb.sl.group.grouphostCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView grouphostlist_item_content;
    public LinearLayout grouphostlist_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        grouphostlist_item_content = (TextView) itemView.findViewById(R.id.grouphostlist_item_content);
        grouphostlist_item_layout = (LinearLayout) itemView.findViewById(R.id.grouphostlist_item_layout);

    }
}
