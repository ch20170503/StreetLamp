package com.zzb.sl.deviceManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zzb.sl.R;
import com.zzb.sl.deviceManagement.HostManage.HostManageActivity;
import com.zzb.sl.deviceManagement.LampManage.LampHostActivity;
import com.zzb.sl.deviceManagement.SubManage.SubHostActivity;
import com.maiml.library.BaseItemLayout;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class DeviceManagementActivity extends SwipeBackActivity implements View.OnClickListener {
    private BaseItemLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_management);
        layout = (BaseItemLayout) findViewById(R.id.deviceManagement_item_layout);
        findViewById(R.id.deviceManagement_item_img).setOnClickListener(this);
        setData();
    }

    private void setData(){
        //存放Text显示(基础信息)
        String hostM = this.getResources().getString(R.string.hostManage);
        String lampM = this.getResources().getString(R.string.lampManage);
        String subM = this.getResources().getString(R.string.subManage);
        final List<String> valueList = new ArrayList<>();
        valueList.add(hostM);
        valueList.add(lampM);
        valueList.add(subM);
        //存放图片
        List<Integer> resIdList = new ArrayList<>();
        resIdList.add(R.drawable.hostmanager);
        resIdList.add(R.drawable.lampmanager);
        resIdList.add(R.drawable.submanager);
        layout.setValueList(valueList) // 文字 list
                .setResIdList(resIdList) //设置图片
                .create();
        layout.setOnBaseItemClick(new BaseItemLayout.OnBaseItemClick() {
            @Override
            public void onItemClick(int position) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(DeviceManagementActivity.this, HostManageActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(DeviceManagementActivity.this, LampHostActivity.class);
                        startActivity(intent1);

                        break;
                    case 2:
                        Intent intent2 = new Intent(DeviceManagementActivity.this,SubHostActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
