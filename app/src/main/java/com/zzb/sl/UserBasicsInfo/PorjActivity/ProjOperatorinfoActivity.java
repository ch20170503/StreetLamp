package com.zzb.sl.UserBasicsInfo.PorjActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zzb.bean.Organ;
import com.zzb.sl.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ProjOperatorinfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private TextView orgczy_text_name,orgczy_text_lxr,orgczy_text_dh,orgczy_text_dz,orgczy_text_xmsm;
    private Organ organ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj_operatorinfo);
        orgczy_text_name = (TextView) findViewById(R.id.orgczy_text_name);
        orgczy_text_lxr= (TextView) findViewById(R.id.orgczy_text_lxr);
        orgczy_text_dh = (TextView) findViewById(R.id.orgczy_text_dh);
        orgczy_text_dz = (TextView) findViewById(R.id.orgczy_text_dz);
        orgczy_text_xmsm= (TextView) findViewById(R.id.orgczy_text_xmsm);
        findViewById(R.id.operatorczy_info_img2).setOnClickListener(this);
        getData();
    }

    private void getData(){
        //获取Intent中传递对对象
        Intent intent = this.getIntent();
        organ = (Organ) intent.getSerializableExtra("SELECTUSER_ORGOPEN");
        orgczy_text_name.setText(organ.getsS_FullName());
        orgczy_text_lxr.setText(organ.getsS_ManagerId());
        orgczy_text_dh.setText(organ.getsS_TelePhone());
        orgczy_text_dz.setText(organ.getsS_Address());
        orgczy_text_xmsm.setText(organ.getsS_Description());

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
