package com.zzb.sl.TimeController.GroupController;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zzb.CustomView.LampControlView;
import com.zzb.bean.Group;
import com.zzb.http.GroupHttp;
import com.zzb.sl.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GroupControllerInfoActivity extends SwipeBackActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "GroupControllerInfoActivity";
    private Group group = new Group();
    private WheelView wv;
    private SeekBar lv;
    private TextView groupCont_info_list_textName,groupCont_info_list_textNumb,groupCont_info_list_textNumb3,huadongoneGroup;
    private Button button1,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_controller_info);
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("SELECTUSER_ALLGROUPINFOCONTR");
        wv = (WheelView) findViewById(R.id.groupCont_info_list_wv);
        lv = (SeekBar) findViewById(R.id.groupCont_info_list_tempg_control);
        lv.setOnSeekBarChangeListener(this);
        huadongoneGroup = (TextView) findViewById(R.id.huadongoneGroup);
        groupCont_info_list_textName = (TextView) findViewById(R.id.groupCont_info_list_textName);
        groupCont_info_list_textNumb = (TextView) findViewById(R.id.groupCont_info_list_textNumb);
        groupCont_info_list_textNumb3 = (TextView) findViewById(R.id.groupCont_info_list_textNumb3);
        button1 = (Button) findViewById(R.id.btn_1_closeG);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.btn_1_openG);
        button2.setOnClickListener(this);
        findViewById(R.id.groupCont_info_list_img2).setOnClickListener(this);
        List<String> valueList = new ArrayList<>();
        valueList.add("1");
        valueList.add("2");
        valueList.add("3");
        wv.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wv.setSkin(WheelView.Skin.Common); // common皮肤
        wv.setWheelData(valueList);  // 数据集合.
        setData();
    }

    private void setData(){
        groupCont_info_list_textName.setText(getString(R.string.Group)+group.getsS_Number());
        groupCont_info_list_textNumb.setText(group.getsS_Number()+"");
        String aa = group.getsS_GroupContent();
        String[] a = aa.split(",");
        String b = "";
        for (int i = 0; i < a.length; i++) {
            if(a[i].equals("1")){
                b+=(i+1)+",";
            }
        }
        groupCont_info_list_textNumb3.setText(b);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.groupCont_info_list_img2:
                finish();
                break;
            case R.id.btn_1_closeG:
                huadongoneGroup.setText(""+0);
                lv.setProgress(0);
                String S_LightHD = wv.getSelectionItem().toString();
                //注册包
                String S_RegPackage = group.getsSL_HostBase_S_RegPackage();
                //项目
                String org = group.getsSL_Organize_S_Id();
                String S_numb =  group.getsS_Number()+"";
                final String value = S_RegPackage+","+S_LightHD+","+"0"+","+S_numb+","+org;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GroupHttp.sendOneGroup(value);
                    }
                }).start();
                break;
            case R.id.btn_1_openG:
                huadongoneGroup.setText(""+100);
                lv.setProgress(100);
                String S_LightHD2 = wv.getSelectionItem().toString();
                //注册包
                String S_RegPackage2 = group.getsSL_HostBase_S_RegPackage();
                //项目
                String org2 = group.getsSL_Organize_S_Id();
                String S_numb2 =  group.getsS_Number()+"";
                final String value2 = S_RegPackage2+","+S_LightHD2+","+"100"+","+S_numb2+","+org2;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GroupHttp.sendOneGroup(value2);
                    }
                }).start();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        huadongoneGroup.setText(""+progress);
        Log.e(TAG, "progress:"+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "开始滑动");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        final String temp = huadongoneGroup.getText().toString();
        String S_LightHD = wv.getSelectionItem().toString();
        //注册包
        String S_RegPackage = group.getsSL_HostBase_S_RegPackage();
        //项目
        String org = group.getsSL_Organize_S_Id();
        String S_numb =  group.getsS_Number()+"";
        final String value = S_RegPackage+","+S_LightHD+","+temp+","+S_numb+","+org;
        new Thread(new Runnable() {
            @Override
            public void run() {
                GroupHttp.sendOneGroup(value);
            }
        }).start();
    }
    }
