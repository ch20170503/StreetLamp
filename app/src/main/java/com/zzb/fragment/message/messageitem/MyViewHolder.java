package com.zzb.fragment.message.messageitem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzb.sl.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView al_item_content;
    public TextView  alarmTime;
    public TextView  alarmType;
    public RelativeLayout alert_item_type1;

    public MyViewHolder(View itemView) {
        super(itemView);
        al_item_content = (TextView) itemView.findViewById(R.id.AlertContent);
        alarmTime = (TextView) itemView.findViewById(R.id.AlarmTime);
        alarmType = (TextView) itemView.findViewById(R.id.AlarmType);
        alert_item_type1 = (RelativeLayout) itemView.findViewById(R.id.alert_item_type1);

    }
}
