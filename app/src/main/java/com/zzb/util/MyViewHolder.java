package com.zzb.util;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView content;
    public TextView delete;
    public TextView item_content;
    public LinearLayout layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        content = (TextView) itemView.findViewById(R.id.item_content);
        delete = (TextView) itemView.findViewById(R.id.item_delete);
        item_content = (TextView) itemView.findViewById(R.id.pro);
        item_content.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        layout = (LinearLayout) itemView.findViewById(R.id.item_layout);

    }
}
