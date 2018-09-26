package com.zzb.sl.TimeController.GroupController.GroupCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView onelamphostlist_item_content;
    public LinearLayout onelamphostlist_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        onelamphostlist_item_content = (TextView) itemView.findViewById(R.id.onelamphostlist_item_content);
        onelamphostlist_item_layout = (LinearLayout) itemView.findViewById(R.id.onelamphostlist_item_layout);

    }
}
