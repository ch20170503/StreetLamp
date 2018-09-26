package com.zzb.sl.deviceManagement.LampManage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.pickerview.OptionsPickerView;
import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.bean.Lampj;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.TypeBean;
import com.zzb.bean.User;
import com.zzb.http.LampHttp;
import com.zzb.http.SubHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;
import com.zzb.util.GsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AddLampActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG ="AddLampActivity" ;
    private EditText lampadd_hostName,lampadd_regpack,lampadd_pro;
    private ArrayList<TypeBean> mList = new ArrayList<TypeBean>();
    private ArrayList<TypeBean> mList2 = new ArrayList<TypeBean>();
    private Host host = new Host();
    OptionsPickerView pvOptions;

    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lamp);
        findViewById(R.id.lampadd_list_img2).setOnClickListener(this);
        findViewById(R.id.lampadd_img3).setOnClickListener(this);
        //分控类型
        lampadd_hostName = (EditText) findViewById(R.id.lampadd_hostName);
        lampadd_hostName.setOnClickListener(this);
        //调光端口
        lampadd_regpack = (EditText) findViewById(R.id.lampadd_regpack);
        lampadd_pro = (EditText) findViewById(R.id.lampadd_pro);
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_ADDLAMPHOST");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lampadd_list_img2:
               finish();
                break;
            case R.id.lampadd_hostName:
                mList.clear();
                mList2.clear();
                // 单项选择
                mList.add(new TypeBean(1, getString(R.string.single)));
                mList.add(new TypeBean(2, getString(R.string.Double)));
                Util.alertBottomWheelOption(AddLampActivity.this, mList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        lampadd_regpack.setText("");
                        lampadd_hostName.setText(mList.get(postion).getName());
                        if (lampadd_hostName.getText().toString().equals(getString(R.string.Double))){
                            lampadd_regpack.setText("");
                            lampadd_regpack.setText(getString(R.string.all));
                        }else{
                            mList.clear();
                            mList2.clear();
                            // 单项选择(调光端口)
                            mList2.add(new TypeBean(1, "1"));
                            mList2.add(new TypeBean(2, "2"));
                            lampadd_regpack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!lampadd_regpack.getText().toString().equals(R.string.all)){
                                        Util.alertBottomWheelOption(AddLampActivity.this, mList2, new Util.OnWheelViewClick() {
                                            @Override
                                            public void onClick(View view, int postion) {
                                                lampadd_regpack.setText("");
                                                lampadd_regpack.setText(mList2.get(postion).getName());
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            case R.id.lampadd_img3:
                //获取值
                String lampnumb = lampadd_pro.getText().toString();
                String subType = lampadd_hostName.getText().toString();
                if (subType.equals(getString(R.string.single))){
                    subType = "1";
                }else{
                    subType = "2";
                }
                String lampaddD = lampadd_regpack.getText().toString();
                if (lampaddD.equals(getString(R.string.all))){
                    lampaddD = "3";
                }
                if (lampnumb.length() <1 ){
                    ActivityUtil.showToasts(AddLampActivity.this,getString(R.string.EnterNumberOfLights),1*1500);
                }else if (Integer.parseInt(lampnumb) >50){
                    ActivityUtil.showToasts(AddLampActivity.this,getString(R.string.NotgreaterThan15),1*1500);
                }else if(subType.equals("") ||lampaddD == null ){
                    ActivityUtil.showToasts(AddLampActivity.this,getString(R.string.PleaseSelectTheSubControlType),1*1500);

                }else if (lampaddD.equals("") || lampaddD == null){
                    ActivityUtil.showToasts(AddLampActivity.this,getString(R.string.PleaseSelectTheDimmingPort),1*1500);
                }else{
                    //添加
                    addLampAndSub(lampnumb,subType,lampaddD);
                }
                break;
        }
    }
    boolean c = false;
    boolean l = false;
    //添加
    private void addLampAndSub(final String lampNumbs, final String subType, final String light){

        final User user = TotalUrl.getUser();
        new AlertDialog.Builder(this).setTitle(getString(R.string.Add))
                .setMessage(getString(R.string.WhetherToAdd))
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        progressDialog = ProgressDialog.show(AddLampActivity.this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                              int lampnum  = Integer.parseInt(lampNumbs);
                                //根据注册包查询分控信息
                                List<ControlS> controlSList = SubHttp.selectPackSub(user,host.getsS_RegPackage());
                                //获取分控的最大编号
                                List<Integer> subnumb = new ArrayList<>();
                                if (controlSList.size() <=0){
                                    subnumb.add(0);
                                }else{
                                    for (int i = 0; i <controlSList.size() ; i++) {
                                        subnumb.add(controlSList.get(i).getsS_Number());
                                    }
                                }
                                //根据注册包查询灯光信息
                                List<Lampj> lampjList = LampHttp.getHostLamp(host.getsS_RegPackage(),user);
                                //获取灯光的最大编号
                                List<Integer> lampnumb = new ArrayList<>();
                                if (lampjList == null || lampjList.size() <=0){
                                    lampnumb.add(0);
                                }else{
                                    for (int i = 0; i <lampjList.size() ; i++) {
                                        lampnumb.add(lampjList.get(i).getsS_Number());
                                    }
                                }

                                // 分控的最大编号
                                int Max = Collections.max(subnumb);
                                // 灯光的最大编号
                                int Max1 = Collections.max(lampnumb);
                                // 计算添加的分控数量
                                int contone = lampnum / 2;
                                // 取余
                                int conttwo = lampnum % 2;
                                // 要添加的分控数
                                int cont = contone + conttwo;
                                // 获取分控类型
                                int lighttType = Integer.parseInt(subType);
                                // 获取调光口
                                int lightHD = Integer.parseInt(light);
                                switch (lighttType){
                                    case 1:
                                        for (int i = 0; i < lampnum; i++){
                                            // 创建分控
                                            ControlS controls = new ControlS();
                                            // 分控器名称
                                            String fullName = getString(R.string.SubControl)+ (Max + (i + 1));
                                            // 分控器表的主键(随机生成)
                                            String fid = GsonUtil.getStringRandom();
                                            controls.setsS_Id(fid);
                                            // 分控编号
                                            int fNumber = Max + (i + 1);
                                            controls.setsS_Number(fNumber);
                                            // 分控名称
                                            controls.setsS_FullName(fullName);
                                            // 绑定的调光口名称
                                            controls.setsS_PoleName(lightHD+"");
                                            controls.setsS_Address("-");
                                            controls.setsS_Description("-");

                                            controls.setsS_EnabledMark(1);
                                            controls.setsS_Longitude("-");
                                            controls.setsS_Latitude("-");
                                            // 主机注册包
                                            controls.setsSL_HostBast_RegPackage(host.getsS_RegPackage());
                                            c = SubHttp.addContl(controls,user);
                                            if (c){
                                                Log.e(TAG,"第-"+(i+1)+"-分控添加成功");
                                                // 创建灯光类
                                                Lampj lampadd = new Lampj();
                                                // 灯具表主键(随机数)
                                                lampadd.setsS_Id(GsonUtil.getStringRandom());
                                                // 灯具编号
                                                lampadd.setsS_Number(Max1 + (i + 1));
                                                lampadd.setsS_LightHD(lightHD);
                                                // 获取灯具类型
                                                lampadd.setsS_Type(lighttType + "");
                                                // 分控器表的主键fid
                                                lampadd.setsSL_SubController_Id(fid);
                                                // 分控编号
                                                lampadd.setsS_SubNum(fNumber);
                                                // 调用Http
                                                l = LampHttp.addLamp(lampadd,user);
                                                if (l) {
                                                    Log.e(TAG,"第-"+(i+1)+"-灯光添加成功了");
                                                } else {
                                                    Log.e(TAG,"第-"+(i+1)+"-灯光添加失败了重新添加");
                                               /*    LampHttp.addLamp(lampadd, user);*/
                                                    while (l == false){
                                                        l =   LampHttp.addLamp(lampadd, user);
                                                    }
                                                }
                                            }else{
                                                Log.e(TAG,"第-"+(i+1)+"-分控添加失败了重新添加");
                                               /* SubHttp.addContl(controls,user);*/
                                                while (l == false){
                                                    l = SubHttp.addContl(controls,user);
                                                }
                                            }
                                        }
                                        //发送handler
                                        Message msg_listData = new Message();
                                        msg_listData.what = MESSAGETYPE_01;
                                        handler.sendMessage(msg_listData);
                                        break;
                                    case 2:
                                        for (int i = 0; i < cont; i++) {
                                            // 创建分控
                                            ControlS controls = new ControlS();
                                            // 分控器名称
                                            String fullName = getString(R.string.SubControl) + (Max + (i + 1));
                                            // 分控器表的主键(随机生成)
                                            String fid = GsonUtil.getStringRandom();
                                            controls.setsS_Id(fid);
                                            // 分控编号
                                            int fNumber = Max + (i + 1);
                                            controls.setsS_Number(fNumber);
                                            // 分控名称
                                            controls.setsS_FullName(fullName);
                                            // 绑定的灯杆名称
                                            controls.setsS_PoleName(lightHD+"");

                                            controls.setsS_Address("-");
                                            controls.setsS_Description("-");

                                            controls.setsS_EnabledMark(1);
                                            controls.setsS_Longitude("-");
                                            controls.setsS_Latitude("-");
                                            // 主机注册包
                                            controls.setsSL_HostBast_RegPackage(host.getsS_RegPackage());
                                            boolean c = SubHttp.addContl(controls,user);

                                            if (c) {
                                                Log.e(TAG,"第-"+(i+1)+"-分控添加成功了");
                                                // 判断如何余数不等于0的话就给该分控添加1个灯否则就添加2个
                                                if (conttwo != 0) {
                                                    if ((i + 1) != cont) {
                                                        int lampPort = 1;
                                                        for (int j = 0; j < 2; j++) {

                                                            Log.e(TAG,"添加调光口1或2");
                                                            // 创建灯光类
                                                            Lampj lampadd = new Lampj();
                                                            // 获取页面信息
                                                            // 灯具表主键(随机数)
                                                            lampadd.setsS_Id(GsonUtil.getStringRandom());
                                                            // 灯具编号
                                                            lampadd.setsS_Number(Max1 + (i + 1));
                                                            lampadd.setsS_LightHD(lampPort);
                                                            // 获取灯具类型
                                                            lampadd.setsS_Type(lighttType + "");
                                                            Log.e(TAG,"添加灯光:啊啊啊啊----------------------------" + lampadd.getsS_Type());

                                                            // 分控器表的主键fid
                                                            lampadd.setsSL_SubController_Id(fid);
                                                            // 分控编号
                                                            lampadd.setsS_SubNum(fNumber);
                                                            // 调用Http
                                                            // 调用Http
                                                            boolean l = LampHttp.addLamp(lampadd,user);
                                                            if (l) {
                                                                Log.e(TAG,"添加灯光成功了");
                                                                lampPort++;
                                                            } else {
                                                                Log.e(TAG,"添加灯光失败了重新添加");
                                                             /*   LampHttp.addLamp(lampadd,user);
                                                                lampPort++;
                                                                if(lampPort == 3){
                                                                    lampPort = lampPort-1;
                                                                }*/
                                                                while (l == false){
                                                                    l = LampHttp.addLamp(lampadd,user);
                                                                    lampPort++;
                                                                    if(lampPort == 3){
                                                                        lampPort = lampPort-1;
                                                                    }
                                                                }

                                                            }

                                                        }
                                                    } else {
                                                        Log.e(TAG,"添加调光口1");
                                                        // 创建灯光类
                                                        Lampj lampadd = new Lampj();
                                                        // 获取页面信息
                                                        // 灯具表主键(随机数)
                                                        lampadd.setsS_Id(GsonUtil.getStringRandom());
                                                        // 灯具编号
                                                        lampadd.setsS_Number(Max1 + (i + 1));
                                                        lampadd.setsS_LightHD(1);
                                                        // 获取灯具类型
                                                        lampadd.setsS_Type(lighttType + "");
                                                        Log.e(TAG,"添加灯光:啊啊啊啊----------------------------" + lampadd.getsS_Type());
                                                        // 分控器表的主键fid
                                                        lampadd.setsSL_SubController_Id(fid);
                                                        // 分控编号
                                                        lampadd.setsS_SubNum(fNumber);
                                                        // 调用Http
                                                        // 调用Http
                                                        l = LampHttp.addLamp(lampadd,user);
                                                        if (l){
                                                            Log.e(TAG,"添加灯光成功了");
                                                        }else {
                                                            Log.e(TAG,"添加灯光失败了重新添加");
                                                          /*  LampHttp.addLamp(lampadd,user);*/
                                                            while (l == false){
                                                                l = LampHttp.addLamp(lampadd,user);
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    int lampPort = 1;
                                                    for (int j = 0; j < 2; j++) {
                                                        Log.e(TAG,"添加调光口1或2");
                                                        // 创建灯光类
                                                        Lampj lampadd = new Lampj();
                                                        // 获取页面信息
                                                        // 灯具表主键(随机数)
                                                        lampadd.setsS_Id(GsonUtil.getStringRandom());
                                                        // 灯具编号
                                                        lampadd.setsS_Number(Max1 + (i + 1));
                                                        lampadd.setsS_LightHD(lampPort);
                                                        // 获取灯具类型
                                                        lampadd.setsS_Type(lighttType + "");
                                                        Log.e(TAG,"添加灯光:啊啊啊啊----------------------------" + lampadd.getsS_Type());

                                                        // 分控器表的主键fid
                                                        lampadd.setsSL_SubController_Id(fid);
                                                        // 分控编号
                                                        lampadd.setsS_SubNum(fNumber);
                                                        // 调用Http
                                                        // 调用Http
                                                        l = LampHttp.addLamp(lampadd,user);
                                                        if (l) {
                                                            Log.e(TAG,"添加灯光成功了");
                                                            lampPort++;
                                                        } else {
                                                            Log.e(TAG,"添加灯光失败了重新添加");
                                                        /*   LampHttp.addLamp(lampadd,user);
                                                            lampPort++;
                                                            if(lampPort == 3){
                                                                lampPort = lampPort-1;
                                                            }*/
                                                            while (l == false){
                                                                l = LampHttp.addLamp(lampadd,user);
                                                                lampPort++;
                                                                if(lampPort == 3){
                                                                    lampPort = lampPort-1;
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            } else {
                                                Log.e(TAG,"添加分控失败了重新添加");
                                                /*SubHttp.addContl(controls,user);*/
                                                while (l == false){
                                                    l  = SubHttp.addContl(controls,user);
                                                }
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                //发送handler
                                Message msg_listData = new Message();
                                msg_listData.what = MESSAGETYPE_01;
                                handler.sendMessage(msg_listData);
                                finish();
                            }
                        }).start();
                    }
                }).setNegativeButton(getResources().getString(R.string.No),new DialogInterface.OnClickListener() {//添加返回按钮
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
