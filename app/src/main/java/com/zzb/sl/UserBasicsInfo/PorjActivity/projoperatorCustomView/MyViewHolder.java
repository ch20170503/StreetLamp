package com.zzb.sl.UserBasicsInfo.PorjActivity.projoperatorCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView projopera_ite_content;

    public LinearLayout projopera_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        projopera_ite_content = (TextView) itemView.findViewById(R.id.projopera_ite_content);

        projopera_item_layout = (LinearLayout) itemView.findViewById(R.id.projopera_item_layout);

    }
}
