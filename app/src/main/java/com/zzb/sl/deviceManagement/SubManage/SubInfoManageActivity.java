package com.zzb.sl.deviceManagement.SubManage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.SubHttp;
import com.zzb.mapController.SubMap;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SubInfoManageActivity extends SwipeBackActivity implements View.OnClickListener {
    private ControlS controlS = new ControlS();
    private TextView sub_info_text_nameNumb, sub_info_text_hostSub, sub_info_text_type;
    private EditText sub_info_subName, sub_info_subaddress, sub_info_bezhu, sub_info_jd, sub_info_wd;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private Host host = new Host();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_info_manage);
        //不可更改
        sub_info_text_nameNumb = (TextView) findViewById(R.id.sub_info_text_nameNumb);
        sub_info_text_hostSub = (TextView) findViewById(R.id.sub_info_text_hostSub);
        sub_info_text_type = (TextView) findViewById(R.id.sub_info_text_type);
        //EditText
        sub_info_subName = (EditText) findViewById(R.id.sub_info_subName);
        sub_info_subaddress = (EditText) findViewById(R.id.sub_info_subaddress);
        sub_info_bezhu = (EditText) findViewById(R.id.sub_info_bezhu);
        sub_info_jd = (EditText) findViewById(R.id.sub_info_jd);
        sub_info_wd = (EditText) findViewById(R.id.sub_info_wd);
        sub_info_jd.setOnClickListener(this);
        sub_info_wd.setOnClickListener(this);
        findViewById(R.id.sub_info_img2).setOnClickListener(this);
        findViewById(R.id.sub_info_img3).setOnClickListener(this);
        Intent intent = getIntent();
        controlS = (ControlS) intent.getSerializableExtra("SELECTUSER_SUBINFO");
        host = (Host) intent.getSerializableExtra("SELECTUSER_SUBINFOHOST");
        setData();
    }


    //设置数据
    private void setData() {
        sub_info_text_nameNumb.setText(controlS.getsS_Number() + "");
        if (controlS.getsSL_HostBast_RegPackage().equals(host.getsS_RegPackage())) {
            sub_info_text_hostSub.setText(host.getsS_FullName());
        }
        sub_info_text_type.setText(controlS.getsS_PoleName());
        sub_info_subName.setText(controlS.getsS_FullName());
        sub_info_subaddress.setText(controlS.getsS_Address());
        sub_info_bezhu.setText(controlS.getsS_Description());
        sub_info_jd.setText(controlS.getsS_Longitude());
        sub_info_wd.setText(controlS.getsS_Latitude());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sub_info_img2:
                finish();
                break;
            case R.id.sub_info_jd:
                Intent intent = new Intent(this, SubMap.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.sub_info_wd:
                Intent intent1 = new Intent(this, SubMap.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.sub_info_img3:
                if (sub_info_subName.length() < 1) {
                    ActivityUtil.showToasts(this,getString(R.string.TheNameLengthMustNotBeLessThan1), 1 * 1000);
                } else if (sub_info_subaddress.length() < 1) {
                    ActivityUtil.showToasts(this, getString(R.string.AddressLengthMustNotBeLessThan1), 1 * 1000);
                } else if (sub_info_bezhu.length() < 1) {
                    ActivityUtil.showToasts(this, getString(R.string.RemarkLengthMustNotBeLessThan1), 1 * 1000);
                } else if (sub_info_jd.length() < 1) {
                    ActivityUtil.showToasts(this, getString(R.string.PleaseaAddLongitude), 1 * 1000);
                } else if (sub_info_wd.length() < 1) {
                    ActivityUtil.showToasts(this, getString(R.string.PleaseAddLatitude), 1 * 1000);
                } else {
                    updata();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            String jw = data.getStringExtra("latitudes");
            String[] jwList = jw.split(",");
            sub_info_jd.setText(jwList[0]);
            sub_info_wd.setText(jwList[1]);
        }

    }

    //保存
    private void updata() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.revise))
                .setMessage(getString(R.string.Modify))
                .setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        progressDialog = ProgressDialog.show(SubInfoManageActivity.this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
                        final User user = TotalUrl.getUser();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                controlS.setsS_FullName(sub_info_subName.getText().toString());
                                controlS.setsS_Address(sub_info_subaddress.getText().toString());
                                controlS.setsS_Description(sub_info_bezhu.getText().toString());
                                controlS.setsS_Longitude(sub_info_jd.getText().toString());
                                controlS.setsS_Latitude(sub_info_wd.getText().toString());
                                boolean b = SubHttp.UpdataSub(controlS, user);
                                if (b) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(SubInfoManageActivity.this, R.string.Updatasuccess, 1 * 1000);
                                            finish();
                                        }
                                    });
                                    //发送handler
                                    Message msg_listData = new Message();
                                    msg_listData.what = MESSAGETYPE_01;
                                    handler.sendMessage(msg_listData);
                                    return;
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(SubInfoManageActivity.this, R.string.Updatafailed, 1 * 1000);
                                            //发送handler
                                            Message msg_listData = new Message();
                                            msg_listData.what = MESSAGETYPE_01;
                                            handler.sendMessage(msg_listData);
                                            return;
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }).setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
            }
        }).show();//在按键响应事件中显示此对话框
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGETYPE_01:
                    //刷新UI，显示数据，并关闭进度条
                    progressDialog.dismiss(); //关闭进度条
                    break;
            }
        }
    };
}

