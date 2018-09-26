package com.zzb.sl.deviceManagement.LampManage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzb.bean.Lampj;
import com.zzb.sl.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class LampInfoManageActivity extends SwipeBackActivity implements View.OnClickListener {
private Lampj lampj = new Lampj();
    private TextView lamp_info_text_name,lamp_info_text_Nname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_info_manage);
        lamp_info_text_name = (TextView) findViewById(R.id.lamp_info_text_name);
        lamp_info_text_Nname = (TextView) findViewById(R.id.lamp_info_text_Nname);
        findViewById(R.id.lamp_info_img2).setOnClickListener(this);
        Intent intent = getIntent();
        lampj = (Lampj) intent.getSerializableExtra("SELECTUSER_LAMPINFO");
        lamp_info_text_name.setText(lampj.getsS_Number()+"");
        lamp_info_text_Nname.setText(lampj.getsS_LightHD()+"");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lamp_info_img2:
                finish();
                break;
        }

    }
}
