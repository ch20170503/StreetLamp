package com.zzb.sl.TimeController.LoopController;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzb.bean.HostDataSet;
import com.zzb.http.LoopHttp;
import com.zzb.sl.R;
import com.zzb.util.GsonUtil;
import com.suke.widget.SwitchButton;


import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class LoopInfoControllerActivity extends SwipeBackActivity implements SwitchButton.OnCheckedChangeListener, View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "LoopInfoControllerActivity";
    private HostDataSet hostDataSet = new HostDataSet();
    private TextView loop_info_hostname, loop_info_hostIntnet,
            loop_info_hostdianya, loop_info_hostdianliu,
            loop_info_hostgonglu, loop_info_hostwendu, loop_info_hosttime;
    private SwitchButton switch_button_all, switch_button_loop1, switch_button_loop2,
            switch_button_loop3, switch_button_loop4, switch_button_loop5,
            switch_button_loop6, switch_button_loop7, switch_button_loop8;
    private List<SwitchButton> li;
    private RelativeLayout loop_info_swichbutton1;
    private boolean bs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_info_controller);
        Intent intent = getIntent();
        hostDataSet = (HostDataSet) intent.getSerializableExtra("SELECTUSER_HOSTDATASET");
        loop_info_hostname = (TextView) findViewById(R.id.loop_info_hostname);
        loop_info_hostIntnet = (TextView) findViewById(R.id.loop_info_hostIntnet);
        loop_info_hostdianya = (TextView) findViewById(R.id.loop_info_hostdianya);
        loop_info_hostdianliu = (TextView) findViewById(R.id.loop_info_hostdianliu);
        loop_info_hostgonglu = (TextView) findViewById(R.id.loop_info_hostgonglu);
        loop_info_hostwendu = (TextView) findViewById(R.id.loop_info_hostwendu);
        loop_info_hosttime = (TextView) findViewById(R.id.loop_info_hosttime);
        switch_button_all = (SwitchButton) findViewById(R.id.switch_button_all);
        switch_button_loop1 = (SwitchButton) findViewById(R.id.switch_button_loop1);

        switch_button_loop2 = (SwitchButton) findViewById(R.id.switch_button_loop2);
        switch_button_loop3 = (SwitchButton) findViewById(R.id.switch_button_loop3);
        switch_button_loop4 = (SwitchButton) findViewById(R.id.switch_button_loop4);
        switch_button_loop5 = (SwitchButton) findViewById(R.id.switch_button_loop5);
        switch_button_loop6 = (SwitchButton) findViewById(R.id.switch_button_loop6);
        switch_button_loop7 = (SwitchButton) findViewById(R.id.switch_button_loop7);
        switch_button_loop8 = (SwitchButton) findViewById(R.id.switch_button_loop8);
        switch_button_all.setOnCheckedChangeListener(this);
        switch_button_loop1.setOnCheckedChangeListener(this);
        switch_button_loop2.setOnCheckedChangeListener(this);
        switch_button_loop3.setOnCheckedChangeListener(this);
        switch_button_loop4.setOnCheckedChangeListener(this);
        switch_button_loop5.setOnCheckedChangeListener(this);
        switch_button_loop6.setOnCheckedChangeListener(this);
        switch_button_loop7.setOnCheckedChangeListener(this);
        switch_button_loop8.setOnCheckedChangeListener(this);
        switch_button_loop1.setOnTouchListener(this);
        switch_button_loop2.setOnTouchListener(this);
        switch_button_loop3.setOnTouchListener(this);
        switch_button_loop4.setOnTouchListener(this);
        switch_button_loop5.setOnTouchListener(this);
        switch_button_loop6.setOnTouchListener(this);
        switch_button_loop7.setOnTouchListener(this);
        switch_button_loop8.setOnTouchListener(this);
        findViewById(R.id.loop_info_basics_item_img).setOnClickListener(this);

        li = new ArrayList<>();
        li.add(switch_button_loop1);
        li.add(switch_button_loop2);
        li.add(switch_button_loop3);
        li.add(switch_button_loop4);
        li.add(switch_button_loop5);
        li.add(switch_button_loop6);
        li.add(switch_button_loop7);
        li.add(switch_button_loop8);
        loop_info_hostname.setText(hostDataSet.getsS_Fname());
        int S_Onlineint = hostDataSet.getsS_Online();
        if (S_Onlineint == 1) {
            loop_info_hostIntnet.setText(getString(R.string.Online));
        } else {
            loop_info_hostIntnet.setText(getString(R.string.Offline));
        }
        String[] S_Voltage = hostDataSet.getsS_Voltage().split(",");
        loop_info_hostdianya.setText("A:" + S_Voltage[0] + " B:" + S_Voltage[1] + " C:" + S_Voltage[2]);
        String[] S_Current = hostDataSet.getsS_Current().split(",");
        loop_info_hostdianliu.setText("A:" + S_Current[0] + " B:" + S_Current[1] + " C:" + S_Current[2]);
        String[] S_power = hostDataSet.getsS_power().split(",");
        loop_info_hostgonglu.setText("A:" + S_power[0] + " B:" + S_power[1] + " C:" + S_power[2]);
        loop_info_hostwendu.setText(hostDataSet.getsS_Temperature());
        String[] S_UpdateTime = hostDataSet.getsS_UpdateTime().split("T");
        loop_info_hosttime.setText(S_UpdateTime[0] + " " + S_UpdateTime[1]);
        String[] S_LoopStateString = hostDataSet.getsS_LoopState().split(",");
        for (int i = 0; i < S_LoopStateString.length; i++) {
            int loopstate = Integer.parseInt(S_LoopStateString[i]);
            for (int j = 0; j < li.size(); j++) {
                if (loopstate == 1) {
                    li.get(i).setChecked(true);
                } else {
                    li.get(i).setChecked(false);
                }
            }
        }

    }


    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        String openL = "01";
        String closeL ="00";
            switch (view.getId()) {
                case R.id.switch_button_all:
                    if (isChecked) {
                        switch_button_loop1.setOnCheckedChangeListener(null);
                        switch_button_loop2.setOnCheckedChangeListener(null);
                        switch_button_loop3.setOnCheckedChangeListener(null);
                        switch_button_loop4.setOnCheckedChangeListener(null);
                        switch_button_loop5.setOnCheckedChangeListener(null);
                        switch_button_loop6.setOnCheckedChangeListener(null);
                        switch_button_loop7.setOnCheckedChangeListener(null);
                        switch_button_loop8.setOnCheckedChangeListener(null);
                        for (int i = 0; i < li.size(); i++) {
                            li.get(i).setChecked(true);
                        }
                        oPenOrClose("1,2,3,4,5,6,7,8,","01");
                    } else {
                        for (int i = 0; i < li.size(); i++) {
                            li.get(i).setChecked(false);
                        }
                        oPenOrClose("1,2,3,4,5,6,7,8,","00");
                    }
                    break;
                case R.id.switch_button_loop1:

                    if (isChecked){
                        //开启回路
                        oPenOrClose("1,",openL);
                    }else{
                        //关闭回路
                        oPenOrClose("1,",closeL);
                    }
                    break;
                case R.id.switch_button_loop2:
                    if (isChecked){
                        //开启回路
                        oPenOrClose("2,",openL);
                    }else{
                        //关闭回路
                        oPenOrClose("2,",closeL);
                    }
                    break;
                case R.id.switch_button_loop3:
                    if (isChecked){
                        //开启回路
                        oPenOrClose("3,",openL);
                    }else{
                        //关闭回路
                        oPenOrClose("3,",closeL);
                    }
                    break;
                case R.id.switch_button_loop4:
                    if (isChecked){
                        //开启回路
                        oPenOrClose("4,",openL);
                    }else{
                        //关闭回路
                        oPenOrClose("4,",closeL);
                    }
                    break;
                case R.id.switch_button_loop5:
                    if (isChecked){
                        //开启回路
                        oPenOrClose("5,",openL);
                    }else{
                        //关闭回路
                        oPenOrClose("5,",closeL);
                    }
                    break;
                case R.id.switch_button_loop6:
                    if (isChecked){
                        //开启回路
                        oPenOrClose("6,",openL);
                    }else{
                        //关闭回路
                        oPenOrClose("6,",closeL);
                    }
                    break;
                case R.id.switch_button_loop7:
                    if (isChecked){
                        //开启回路
                        oPenOrClose("7,",openL);
                    }else{
                        //关闭回路
                        oPenOrClose("7,",closeL);
                    }
                    break;
                case R.id.switch_button_loop8:
                    if (isChecked){
                        //开启回路
                        oPenOrClose("8,",openL);
                    }else{
                        //关闭回路
                        oPenOrClose("8,",closeL);
                    }
                    break;
            }
        }



    //开启或者关闭回路
    private void oPenOrClose(final String loop, final String openOrClose){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String loopNumbString = GsonUtil.getLamp(loop);
                String S_RegPackage = hostDataSet.getsS_RegPackage();
                String SL_Organize_S_Id =hostDataSet.getSL_Organize_S_Id();
                String value = S_RegPackage + "," + openOrClose + "," + loopNumbString + "," + SL_Organize_S_Id;
                LoopHttp.OpenOrCloseLoop(value);
            }
        }).start();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.switch_button_loop1:
                Log.e(TAG,"点击1");
                switch_button_loop1.setOnCheckedChangeListener(this);
                switch_button_loop2.setOnCheckedChangeListener(this);
                switch_button_loop3.setOnCheckedChangeListener(this);
                switch_button_loop4.setOnCheckedChangeListener(this);
                switch_button_loop5.setOnCheckedChangeListener(this);
                switch_button_loop6.setOnCheckedChangeListener(this);
                switch_button_loop7.setOnCheckedChangeListener(this);
                switch_button_loop8.setOnCheckedChangeListener(this);
                break;
            case R.id.switch_button_loop2:
                switch_button_loop1.setOnCheckedChangeListener(this);
                switch_button_loop2.setOnCheckedChangeListener(this);
                switch_button_loop3.setOnCheckedChangeListener(this);
                switch_button_loop4.setOnCheckedChangeListener(this);
                switch_button_loop5.setOnCheckedChangeListener(this);
                switch_button_loop6.setOnCheckedChangeListener(this);
                switch_button_loop7.setOnCheckedChangeListener(this);
                switch_button_loop8.setOnCheckedChangeListener(this);
                break;
            case R.id.switch_button_loop3:
                switch_button_loop1.setOnCheckedChangeListener(this);
                switch_button_loop2.setOnCheckedChangeListener(this);
                switch_button_loop3.setOnCheckedChangeListener(this);
                switch_button_loop4.setOnCheckedChangeListener(this);
                switch_button_loop5.setOnCheckedChangeListener(this);
                switch_button_loop6.setOnCheckedChangeListener(this);
                switch_button_loop7.setOnCheckedChangeListener(this);
                switch_button_loop8.setOnCheckedChangeListener(this);
                break;
            case R.id.switch_button_loop4:
                switch_button_loop1.setOnCheckedChangeListener(this);
                switch_button_loop2.setOnCheckedChangeListener(this);
                switch_button_loop3.setOnCheckedChangeListener(this);
                switch_button_loop4.setOnCheckedChangeListener(this);
                switch_button_loop5.setOnCheckedChangeListener(this);
                switch_button_loop6.setOnCheckedChangeListener(this);
                switch_button_loop7.setOnCheckedChangeListener(this);
                switch_button_loop8.setOnCheckedChangeListener(this);
                break;
            case R.id.switch_button_loop5:
                switch_button_loop1.setOnCheckedChangeListener(this);
                switch_button_loop2.setOnCheckedChangeListener(this);
                switch_button_loop3.setOnCheckedChangeListener(this);
                switch_button_loop4.setOnCheckedChangeListener(this);
                switch_button_loop5.setOnCheckedChangeListener(this);
                switch_button_loop6.setOnCheckedChangeListener(this);
                switch_button_loop7.setOnCheckedChangeListener(this);
                switch_button_loop8.setOnCheckedChangeListener(this);
                break;
            case R.id.switch_button_loop6:
                switch_button_loop1.setOnCheckedChangeListener(this);
                switch_button_loop2.setOnCheckedChangeListener(this);
                switch_button_loop3.setOnCheckedChangeListener(this);
                switch_button_loop4.setOnCheckedChangeListener(this);
                switch_button_loop5.setOnCheckedChangeListener(this);
                switch_button_loop6.setOnCheckedChangeListener(this);
                switch_button_loop7.setOnCheckedChangeListener(this);
                switch_button_loop8.setOnCheckedChangeListener(this);
                break;
            case R.id.switch_button_loop7:
                switch_button_loop1.setOnCheckedChangeListener(this);
                switch_button_loop2.setOnCheckedChangeListener(this);
                switch_button_loop3.setOnCheckedChangeListener(this);
                switch_button_loop4.setOnCheckedChangeListener(this);
                switch_button_loop5.setOnCheckedChangeListener(this);
                switch_button_loop6.setOnCheckedChangeListener(this);
                switch_button_loop7.setOnCheckedChangeListener(this);
                switch_button_loop8.setOnCheckedChangeListener(this);
                break;
            case R.id.switch_button_loop8:
                switch_button_loop1.setOnCheckedChangeListener(this);
                switch_button_loop2.setOnCheckedChangeListener(this);
                switch_button_loop3.setOnCheckedChangeListener(this);
                switch_button_loop4.setOnCheckedChangeListener(this);
                switch_button_loop5.setOnCheckedChangeListener(this);
                switch_button_loop6.setOnCheckedChangeListener(this);
                switch_button_loop7.setOnCheckedChangeListener(this);
                switch_button_loop8.setOnCheckedChangeListener(this);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
