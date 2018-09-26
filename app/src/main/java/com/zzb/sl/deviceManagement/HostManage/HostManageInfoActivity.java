package com.zzb.sl.deviceManagement.HostManage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.zzb.bean.Host;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.HostHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.UserHttp;
import com.zzb.mapController.HostMap;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class HostManageInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "HostManageInfoActivity";
    private EditText host_info_hostName, host_info_regpack,
            host_info_heart, host_info_lognitude, host_info_latitude,
            host_info_phone, host_info_adderss, host_info_bezhu;
    private Host host = new Host();
    private static final int MESSAGETYPE_01 = 0x0001;
    private ProgressDialog progressDialog = null;
    private Spinner spinner;
    private List<String> data_list;
    private List<String> data_list2;
    private ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_manage_info);
        spinner = (Spinner) findViewById(R.id.host_info_pro);
        //host_info_pro = (EditText) findViewById(R.id.host_info_pro);
        host_info_hostName = (EditText) findViewById(R.id.host_info_hostName);
        host_info_regpack = (EditText) findViewById(R.id.host_info_regpack);
        host_info_heart = (EditText) findViewById(R.id.host_info_heart);
        host_info_lognitude = (EditText) findViewById(R.id.host_info_lognitude);
        host_info_latitude = (EditText) findViewById(R.id.host_info_latitude);
        host_info_phone = (EditText) findViewById(R.id.host_info_phone);
        host_info_adderss = (EditText) findViewById(R.id.host_info_adderss);
        host_info_bezhu = (EditText) findViewById(R.id.host_info_bezhu);
        host_info_lognitude.setOnClickListener(this);
        host_info_latitude.setOnClickListener(this);
        findViewById(R.id.host_info_img2).setOnClickListener(this);
        findViewById(R.id.host_info_img3).setOnClickListener(this);
        User user = TotalUrl.getUser();
        int userQ = user.getdate().getSL_USER_RANKID();
        if (userQ == 2) {
            getData();
        } else {
            getData2();
        }

    }

    //获取数据源
    private void getData2() {
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        final User user = TotalUrl.getUser();
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_HOST");
        final String idname = host.getsSL_Organize_S_Id();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Organ> organList = OrganHttp.selectAllProj(user);
                //用户信息
                List<SelectUser> selectUList1 = UserHttp.thisUser(user);
                if (selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() == "") {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(HostManageInfoActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                String[] proList;
                if (selectUList1.get(0).getsS_Project().contains(",")) {
                    proList = selectUList1.get(0).getsS_Project().split(",");
                } else {
                    proList = (selectUList1.get(0).getsS_Project() + ",").split(",");
                }


                if (organList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(HostManageInfoActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                String name = "";
                //数据
                data_list = new ArrayList<>();
                data_list2 = new ArrayList<>();
                for (int i = 0; i < proList.length; i++) {
                    for (int j = 0; j < organList.size(); j++) {
                        if (proList[i].equals(organList.get(j).getsS_Id())) {
                            data_list.add(organList.get(j).getsS_FullName());
                            data_list2.add(organList.get(j).getsS_Id());
                        }
                    }

                }
                for (int i = 0; i < organList.size(); i++) {
                    if (idname.equals(organList.get(i).getsS_Id())) {
                        name = organList.get(i).getsS_FullName();
                    }
                }
                final String finalName = name;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        //适配器
                        arr_adapter = new ArrayAdapter<>(HostManageInfoActivity.this, android.R.layout.simple_spinner_item, data_list);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        spinner.setAdapter(arr_adapter);
                        for (int i = 0; i < data_list.size(); i++) {
                            if (data_list.get(i).equals(finalName)) {
                                spinner.setSelection(i);
                            }
                        }
                        host_info_hostName.setText(host.getsS_FullName());
                        host_info_regpack.setText(host.getsS_RegPackage());
                        host_info_heart.setText(host.getsS_Heart());
                        host_info_lognitude.setText(host.getsS_Longitude());
                        host_info_latitude.setText(host.getsS_Latitude());
                        host_info_phone.setText(host.getsS_Phone());
                        host_info_adderss.setText(host.getsS_Address());
                        host_info_bezhu.setText(host.getsS_Description());
                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler.sendMessage(msg_listData);
                return;
            }
        }).start();
    }

    //获取数据源
    private void getData() {
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        final User user = TotalUrl.getUser();
        final String userOrg = user.getdate().getOrganizeId();
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_HOST");
        final String idname = host.getsSL_Organize_S_Id();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(HostManageInfoActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                String name = "";
                //数据
                data_list = new ArrayList<>();
                data_list2 = new ArrayList<>();
                for (int i = 0; i < organList.size(); i++) {
                    if (idname.equals(organList.get(i).getsS_Id())) {
                        name = organList.get(i).getsS_FullName();
                    }
                }

                for (int i = 0; i < organList.size(); i++) {
                    if (organList.get(i).getsS_ParentId().equals(userOrg)) {
                        data_list.add(organList.get(i).getsS_FullName());
                        data_list2.add(organList.get(i).getsS_Id());
                    }
                }
                final String finalName = name;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //适配器
                        arr_adapter = new ArrayAdapter<>(HostManageInfoActivity.this, android.R.layout.simple_spinner_item, data_list);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        spinner.setAdapter(arr_adapter);
                        for (int i = 0; i < data_list.size(); i++) {
                            if (data_list.get(i).equals(finalName)) {
                                spinner.setSelection(i);
                            }
                        }
                        host_info_hostName.setText(host.getsS_FullName());
                        host_info_regpack.setText(host.getsS_RegPackage());
                        host_info_heart.setText(host.getsS_Heart());
                        host_info_lognitude.setText(host.getsS_Longitude());
                        host_info_latitude.setText(host.getsS_Latitude());
                        host_info_phone.setText(host.getsS_Phone());
                        host_info_adderss.setText(host.getsS_Address());
                        host_info_bezhu.setText(host.getsS_Description());
                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler.sendMessage(msg_listData);
                return;
            }
        }).start();
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.host_info_lognitude:
                Intent intent = new Intent(this, HostMap.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.host_info_latitude:
                Intent intent1 = new Intent(this, HostMap.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.host_info_img2:
                finish();
                break;
            case R.id.host_info_img3:
                if (host_info_hostName.getText().length() < 1){
                    ActivityUtil.showToasts(HostManageInfoActivity.this,R.string.PleaseEnterTheCorrectName,1*1000);
                }else if (host_info_regpack.getText().length() !=6){
                    ActivityUtil.showToasts(HostManageInfoActivity.this,R.string.TheRegistryPackageMustBe6BitPureNumbers,1*1000);
                }else if (host_info_heart.getText().length()!=6){
                    ActivityUtil.showToasts(HostManageInfoActivity.this,R.string.TheHeartbeatPacketMustBe6BitPureDigits,1*1000);
                }else if (host_info_phone.getText().length() >13){
                    ActivityUtil.showToasts(HostManageInfoActivity.this,R.string.NoMoreThan13Calls,1*1000);
                }else if (host_info_adderss.getText().length() <1){
                    ActivityUtil.showToasts(HostManageInfoActivity.this,R.string.PleaseEnterTheCorrectAddress,1*1000);
                }else if (host_info_bezhu.getText().length() <1){
                    ActivityUtil.showToasts(HostManageInfoActivity.this,R.string.PleaseInputMemoInformation,1*1000);
                }else {
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
            host_info_lognitude.setText(jwList[0]);
            host_info_latitude.setText(jwList[1]);
        }

    }

    String proj = "";
    //修改
    private void updata() {
        final User user = TotalUrl.getUser();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                proj = data_list2.get(i);
                Log.e(TAG, "data_list:" + proj);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Log.e(TAG,"主机信息修改前:"+host);
        if(proj != null && proj != ""){
            host.setsSL_Organize_S_Id(proj);
        }
        host.setsS_FullName(host_info_hostName.getText().toString());
        host.setsS_RegPackage(host_info_regpack.getText().toString());
        host.setsS_Heart(host_info_heart.getText().toString());
        host.setsS_Longitude(host_info_lognitude.getText().toString());
        host.setsS_Latitude(host_info_latitude.getText().toString());
        host.setsS_Phone(host_info_phone.getText().toString());
        host.setsS_Address(host_info_adderss.getText().toString());
        host.setsS_Description(host_info_bezhu.getText().toString());
        Log.e(TAG,"主机信息修改后:"+host);
        Log.e(TAG,"主机信息修改后的项目ID:"+host.getsSL_Organize_S_Id());
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.revise))
                .setMessage(getResources().getString(R.string.Modify))
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = HostHttp.UpdataHost(host, user);
                                if (b) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(HostManageInfoActivity.this, R.string.Updatasuccess, 1 * 1000);
                                            finish();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(HostManageInfoActivity.this, R.string.Updatafailed, 1 * 1000);
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }).setNegativeButton(getResources().getString(R.string.No),new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
            }
        }).show();//在按键响应事件中显示此对话框

    }
}
