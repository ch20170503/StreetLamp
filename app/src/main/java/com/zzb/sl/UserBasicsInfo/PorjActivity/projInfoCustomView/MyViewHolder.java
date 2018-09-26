package com.zzb.sl.UserBasicsInfo.PorjActivity.projInfoCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView proj_ite_content;
    public TextView proj_item_delete;
    public TextView item_content;
    public LinearLayout proj_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        proj_ite_content = (TextView) itemView.findViewById(R.id.proj_ite_content);
        proj_item_delete = (TextView) itemView.findViewById(R.id.proj_item_delete);
        proj_item_layout = (LinearLayout) itemView.findViewById(R.id.proj_item_layout);

    }
}
