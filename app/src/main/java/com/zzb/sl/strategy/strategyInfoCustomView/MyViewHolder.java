package com.zzb.sl.strategy.strategyInfoCustomView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView strategy_item_content;
    public TextView strategy_item_delete;
    public TextView strategy_item_content2;
    public LinearLayout strategy_item_layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        strategy_item_content2 = (TextView) itemView.findViewById(R.id.strategy_item_content2);
        strategy_item_content = (TextView) itemView.findViewById(R.id.strategy_item_content);
        strategy_item_delete = (TextView) itemView.findViewById(R.id.strategy_item_delete);
        strategy_item_layout = (LinearLayout) itemView.findViewById(R.id.strategy_item_layout);

    }
}
