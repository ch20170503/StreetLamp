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

public class AddHostActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG ="AddHostActivity" ;
    private Spinner host_add_info_pro;
    private EditText host_add_info_hostName, host_add_info_regpack, host_add_info_heart, host_add_info_lognitude,
            host_add_info_latitude, host_add_info_phone, host_add_info_adderss, host_add_info_bezhu;
    private static final int MESSAGETYPE_01 = 0x0001;
    private ProgressDialog progressDialog = null;
    private List<String> data_list;
    private List<String> data_list2;
    private ArrayAdapter<String> arr_adapter;
    private Host host = new Host();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_host);
        host_add_info_hostName = (EditText) findViewById(R.id.host_add_info_hostName);
        host_add_info_regpack = (EditText) findViewById(R.id.host_add_info_regpack);
        host_add_info_heart = (EditText) findViewById(R.id.host_add_info_heart);
        host_add_info_lognitude = (EditText) findViewById(R.id.host_add_info_lognitude);
        host_add_info_latitude = (EditText) findViewById(R.id.host_add_info_latitude);
        host_add_info_phone = (EditText) findViewById(R.id.host_add_info_phone);
        host_add_info_adderss = (EditText) findViewById(R.id.host_add_info_adderss);
        host_add_info_bezhu = (EditText) findViewById(R.id.host_add_info_bezhu);
        host_add_info_pro = (Spinner) findViewById(R.id.host_add_info_pro);
        findViewById(R.id.host_add_add_img2).setOnClickListener(this);
        findViewById(R.id.host_add_add_img3).setOnClickListener(this);
        host_add_info_lognitude.setOnClickListener(this);
        host_add_info_latitude.setOnClickListener(this);
        User user = TotalUrl.getUser();
        int userQ = user.getdate().getSL_USER_RANKID();
        if (userQ == 2) {
            getData();
        } else {
            getData2();
        }
    }

    //获取元数据
    private void getData(){
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //加载下拉列表框
        final User user = TotalUrl.getUser();
        final String userOrg = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AddHostActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                data_list = new ArrayList<>();
                data_list2 = new ArrayList<>();
                for (int i = 0; i < organList.size(); i++) {
                    if (organList.get(i).getsS_ParentId().equals(userOrg)) {
                        data_list.add(organList.get(i).getsS_FullName());
                        data_list2.add(organList.get(i).getsS_Id());
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //适配器
                        arr_adapter = new ArrayAdapter<>(AddHostActivity.this, android.R.layout.simple_spinner_item, data_list);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        host_add_info_pro.setAdapter(arr_adapter);
                        for (int i = 0; i < data_list.size(); i++) {
                            host_add_info_pro.setSelection(0);

                        }

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


    //获取元数据(操作员)
    private void getData2(){
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //加载下拉列表框
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AddHostActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                //用户信息
                List<SelectUser> selectUList1 = UserHttp.thisUser(user);
                if (selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() == "") {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AddHostActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                String[] proList;
                if (selectUList1.get(0).getsS_Project().contains(",")) {
                    proList = selectUList1.get(0).getsS_Project().split(",");
                } else {
                    proList = (selectUList1.get(0).getsS_Project() + ",").split(",");
                }

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //适配器
                        arr_adapter = new ArrayAdapter<>(AddHostActivity.this, android.R.layout.simple_spinner_item, data_list);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        host_add_info_pro.setAdapter(arr_adapter);
                        for (int i = 0; i < data_list.size(); i++) {
                            host_add_info_pro.setSelection(0);
                        }
                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler2.sendMessage(msg_listData);
                return;
            }
        }).start();
    }
    private Handler handler2 = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGETYPE_01:
                    //刷新UI，显示数据，并关闭进度条
                    progressDialog.dismiss(); //关闭进度条
                    break;
            }
        }
    };


    String proj = "";
    //添加
    private void addHost(){
        final User user = TotalUrl.getUser();
        host_add_info_pro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                proj = data_list2.get(i);
                Log.e(TAG, "data_list:" + proj);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(proj != null && proj != ""){
            host.setsSL_Organize_S_Id(proj);
        }else{
            if (data_list2.size() < 1){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActivityUtil.showToasts(AddHostActivity.this,"当前无项目信息", 1 * 1000);
                    }
                });
                return;
            }
            proj = data_list2.get(0);
            host.setsSL_Organize_S_Id(proj);
        }
        host.setsS_FullName(host_add_info_hostName.getText().toString());
        host.setsS_RegPackage(host_add_info_regpack.getText().toString());
        host.setsS_Heart(host_add_info_heart.getText().toString());
        host.setsS_Longitude(host_add_info_lognitude.getText().toString());
        host.setsS_Latitude(host_add_info_latitude.getText().toString());
        host.setsS_Phone(host_add_info_phone.getText().toString());
        host.setsS_Address(host_add_info_adderss.getText().toString());
        host.setsS_Description(host_add_info_bezhu.getText().toString());
        new AlertDialog.Builder(this).setTitle("添加")
                .setMessage("是否添加")
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = HostHttp.adHost(host, user);
                                if (b) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(AddHostActivity.this, R.string.Addsuccess, 1 * 1000);
                                            finish();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(AddHostActivity.this, R.string.Addfailed, 1 * 1000);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.host_add_info_lognitude:
                Intent intent = new Intent(this, HostMap.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.host_add_info_latitude:
                Intent intent1 = new Intent(this, HostMap.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.host_add_add_img2:
                finish();
                break;
            case R.id.host_add_add_img3:
                if (host_add_info_hostName.getText().length() < 1){
                    ActivityUtil.showToasts(AddHostActivity.this,"请输入正确名称",1*1000);
                }else if (host_add_info_regpack.getText().length() !=6){
                    ActivityUtil.showToasts(AddHostActivity.this,"注册包必须为6位纯数字",1*1000);
                }else if (host_add_info_heart.getText().length()!=6){
                    ActivityUtil.showToasts(AddHostActivity.this,"心跳包必须为6位纯数字",1*1000);
                }else if (host_add_info_phone.getText().length() >13){
                    ActivityUtil.showToasts(AddHostActivity.this,"电话不能大于13位",1*1000);
                }else if (host_add_info_adderss.getText().length() <1){
                    ActivityUtil.showToasts(AddHostActivity.this,"请输入正确地址",1*1000);
                }else if (host_add_info_bezhu.getText().length() <1){
                    ActivityUtil.showToasts(AddHostActivity.this,"请输入备注信息",1*1000);
                }else {
                   addHost();
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
            host_add_info_lognitude.setText(jwList[0]);
            host_add_info_latitude.setText(jwList[1]);
        }

    }
}
