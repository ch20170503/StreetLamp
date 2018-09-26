package com.zzb.fragment.sc.stController;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView sthost_item_content;
    public LinearLayout sthost_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        sthost_item_content = (TextView) itemView.findViewById(R.id.sthost_item_content);
        sthost_item_layout = (LinearLayout) itemView.findViewById(R.id.sthost_item_layout);

    }
}
