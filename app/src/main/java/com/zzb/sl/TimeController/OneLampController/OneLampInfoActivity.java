package com.zzb.sl.TimeController.OneLampController;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zzb.bean.Host;
import com.zzb.bean.Lampj;
import com.zzb.http.LampHttp;
import com.zzb.sl.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OneLampInfoActivity extends SwipeBackActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "OneLampInfoActivity";
    private SeekBar onelampInfo_list_temp_control;
    private TextView onelampInfo_numb, onelampInfo_type, onelampInfo_port,
            onelampInfo_Brightnes, onelampInfo_Voltage, onelampInfo_Electriccurrent,
            onelampInfo_power, onelampInfo_frequency,huadongonelamp;
    private Lampj lampj = new Lampj();
    private Host host = new Host();
    private Button button1,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_lamp_info);
        onelampInfo_list_temp_control = (SeekBar) findViewById(R.id.onelampInfo_list_temp_control);
        onelampInfo_list_temp_control.setOnSeekBarChangeListener(this);
        findViewById(R.id.onelampInfo_list_img2).setOnClickListener(this);
        huadongonelamp = (TextView) findViewById(R.id.huadongonelamp);
        onelampInfo_numb = (TextView) findViewById(R.id.onelampInfo_numb);
        onelampInfo_type = (TextView) findViewById(R.id.onelampInfo_type);
        onelampInfo_port = (TextView) findViewById(R.id.onelampInfo_port);
        onelampInfo_Brightnes = (TextView) findViewById(R.id.onelampInfo_Brightnes);
        onelampInfo_Voltage = (TextView) findViewById(R.id.onelampInfo_Voltage);
        onelampInfo_Electriccurrent = (TextView) findViewById(R.id.onelampInfo_Electriccurrent);
        onelampInfo_power = (TextView) findViewById(R.id.onelampInfo_power);
        onelampInfo_frequency = (TextView) findViewById(R.id.onelampInfo_frequency);
        Intent intent = getIntent();
        lampj = (Lampj) intent.getSerializableExtra("SELECTUSER_ONELAMPJAMPJ");
        host = (Host) intent.getSerializableExtra("SELECTUSER_ONELAMPJAMPJHOST");
        button1 = (Button) findViewById(R.id.btn_1_opens);
        button1 .setOnClickListener(this);
        button2 = (Button) findViewById(R.id.btn_1_closes);
        button2 .setOnClickListener(this);
        setData();
    }
    private void setData() {
        onelampInfo_numb.setText(lampj.getsS_Number() + "");
        onelampInfo_type.setText(lampj.getsS_Type() + "");
        onelampInfo_port.setText(lampj.getsS_LightHD()+"");
        onelampInfo_Brightnes.setText(lampj.getsS_Brightness()+"");
        onelampInfo_list_temp_control.setProgress(lampj.getsS_Brightness());
        onelampInfo_Voltage.setText(lampj.getsS_Voltage());
        onelampInfo_Electriccurrent.setText(lampj.getsS_Current());
        onelampInfo_power.setText(lampj.getsS_power());
        onelampInfo_frequency.setText(lampj.getsS_Frequency());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.onelampInfo_list_img2:
               finish();
                break;
            case R.id.btn_1_opens:
                huadongonelamp.setText(""+100);
                onelampInfo_list_temp_control.setProgress(100);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String value = host.getsS_RegPackage()+","+lampj.getsS_SubNum()+","+lampj.getsS_LightHD()+","+"100"+","+host.getsSL_Organize_S_Id();
                        LampHttp.sendOneLamp(value);
                    }
                }).start();
                break;
            case R.id.btn_1_closes:
                huadongonelamp.setText(""+0);
                onelampInfo_list_temp_control.setProgress(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String value = host.getsS_RegPackage()+","+lampj.getsS_SubNum()+","+lampj.getsS_LightHD()+","+"0"+","+host.getsSL_Organize_S_Id();
                        LampHttp.sendOneLamp(value);
                    }
                }).start();
                break;
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        huadongonelamp.setText(""+progress);
        Log.e(TAG, "progress:"+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
            Log.e(TAG, "开始滑动");
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        final String temp = huadongonelamp.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String value = host.getsS_RegPackage()+","+lampj.getsS_SubNum()+","+lampj.getsS_LightHD()+","+temp+","+host.getsSL_Organize_S_Id();
                LampHttp.sendOneLamp(value);
            }
        }).start();
    }
}
