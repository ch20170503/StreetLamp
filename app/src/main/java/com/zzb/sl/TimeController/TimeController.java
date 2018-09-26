package com.zzb.sl.TimeController;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.bean.HostDataSet;
import com.zzb.bean.Lampj;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.HostHttp;
import com.zzb.http.LampHttp;
import com.zzb.http.LoopHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.SubHttp;
import com.zzb.http.UserHttp;
import com.zzb.mapController.MapController;
import com.zzb.sl.R;
import com.zzb.sl.TimeController.AllLampController.AllLampControllerActivity;
import com.zzb.sl.TimeController.GroupController.GroupControllerActivity;
import com.zzb.sl.TimeController.LoopController.LoopControllerActivity;
import com.zzb.sl.TimeController.OneLampController.OneLampControllerActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class TimeController extends SwipeBackActivity implements View.OnClickListener {

    private static final String TAG =   "TimeController" ;
 /*   private ProgressDialog progressDialog = null;*/
    private static final int MESSAGETYPE_01 = 0x0001;

    private List<ControlS> contrlist = new ArrayList<>();
    private List<Lampj> lamplist = new ArrayList<>();
    private List<Host> hostList = new ArrayList<>();
    private List<HostDataSet> hostDS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_controller);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        if (user != null){
            int userID = user.getdate().getSL_USER_RANKID();
            if (userID == 2) {
                getData();
            } else{
                getData2();
            }
        }
        findViewById(R.id.timeContll_rl_Loop).setOnClickListener(this);
        findViewById(R.id.timeContll_rl_lamp).setOnClickListener(this);
        findViewById(R.id.timeContll_rl_alllamp).setOnClickListener(this);
        findViewById(R.id.timeContll_rl_group).setOnClickListener(this);
        findViewById(R.id.timeContll_rl_map).setOnClickListener(this);
        findViewById(R.id.contro_item_img).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timeContll_rl_Loop:
                Intent intent = new Intent(TimeController.this, LoopControllerActivity.class);
                startActivity(intent);
                break;
            case R.id.timeContll_rl_lamp:
                Intent intent2 = new Intent(TimeController.this, OneLampControllerActivity.class);
                startActivity(intent2);
                break;
            case R.id.timeContll_rl_alllamp:
                Intent intent3 = new Intent(TimeController.this, AllLampControllerActivity.class);
                startActivity(intent3);
                break;
            case R.id.timeContll_rl_group:
                Intent intent4 = new Intent(TimeController.this, GroupControllerActivity.class);
                startActivity(intent4);
                break;
            case R.id.timeContll_rl_map:
            /*    if (hostList == null || hostList.size() <1){
                    Intent intent5 = new Intent();
                    intent5.setClass(TimeController.this, MapController.class);
                    startActivity(intent5);
                    break;
                }
                if (lamplist == null || lamplist.size() <1){
                    Intent intent5 = new Intent();
                    intent5.setClass(TimeController.this, MapController.class);
                    startActivity(intent5);
                    break;
                }
                if (contrlist == null || contrlist.size() <1){
                    Intent intent5 = new Intent();
                    intent5.setClass(TimeController.this, MapController.class);
                    startActivity(intent5);
                    break;
                }*/

                    //查询用户所属信息中的所属项目字段
                    Intent intent5 = new Intent();
                    intent5.setClass(TimeController.this, MapController.class);
                    intent5.putExtra("MAPHOST", (Serializable) hostList);
                    intent5.putExtra("MAPLAMP", (Serializable) lamplist);
                    intent5.putExtra("MAPSUB", (Serializable) contrlist);
                    intent5.putExtra("MAPHOSTDS", (Serializable) hostDS);
                    startActivity(intent5);
                break;
            case R.id.contro_item_img:
                finish();
                break;


        }
    }


    //查询主机以及分控(操作员)
    private void getData(){
     /* progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));*/
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String userPro = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主机
                List<Host> hoseLists = HostHttp.SelectAllHost(user);
                List<Organ> organList =  OrganHttp.selectAllProj(user);
                if (hoseLists == null || hoseLists.size() <1){
                    hoseLists = HostHttp.SelectAllHost(user);
                    if (hoseLists == null || hoseLists.size() <1){
                     /*   //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }

                if (organList == null|| organList.size() <1){
                    organList =  OrganHttp.selectAllProj(user);
                    if (organList == null || organList.size() <1){
                      /*  //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }
                //查询所属灯光
                //灯光
                List<Lampj> LampjList = LampHttp.SelectAlllamp(user);
                if (LampjList == null|| LampjList.size() <1){
                    LampjList = LampHttp.SelectAlllamp(user);
                    if (LampjList == null || LampjList.size() <1){
                       /* //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }
                //分控
                List<ControlS> subList = SubHttp.SelectAllSub(user);
                if (subList == null|| subList.size() <1){
                    subList = SubHttp.SelectAllSub(user);
                    if (subList == null || subList.size() <1){
                     /*   //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }

                //查询所有主机状态信息
                List<HostDataSet> hdList = LoopHttp.SelectAllHostDataSet(user);
                if (hdList == null || hdList.size() < 1) {
                    hdList = LoopHttp.SelectAllHostDataSet(user);
                    if (hdList == null || hdList.size() < 1){
                      /*  //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }
                for (int i = 0; i < organList.size(); i++) {
                    String pro = organList.get(i).getsS_ParentId();
                    String proID = organList.get(i).getsS_Id();
                    if (pro.equals(userPro)) {
                        for (int j = 0; j < hoseLists.size(); j++) {
                            String org = hoseLists.get(j).getsSL_Organize_S_Id();
                            if (org.equals(proID)) {

                            }
                        }
                    }
                }
                for (int i = 0; i < organList.size(); i++) {
                    String pro = organList.get(i).getsS_ParentId();
                    String proID = organList.get(i).getsS_Id();
                    if (userPro.equals(pro)) {
                        for (int j = 0; j < hoseLists.size(); j++) {
                            String userOrg = hoseLists.get(j).getsSL_Organize_S_Id();
                            String RegPackage = hoseLists.get(j).getsS_RegPackage();
                            if (userOrg.equals(proID)) {
                                hostList.add(hoseLists.get(j));
                                for (int k = 0; k < subList.size(); k++) {
                                    String RegPackageFk = subList.get(k).getsSL_HostBast_RegPackage();
                                    String SupID = subList.get(k).getsS_Id();
                                    if (RegPackage.equals(RegPackageFk)) {
                                        contrlist.add(subList.get(k));
                                        for (int l = 0; l < LampjList.size(); l++) {
                                            String SupNumid = LampjList.get(l).getsSL_SubController_Id();
                                            if (SupID.equals(SupNumid)) {
                                                lamplist.add(LampjList.get(l));
                                            }
                                        }
                                    }
                                }
                                for (int k = 0; k < hdList.size(); k++) {
                                    if (hoseLists.get(j).getsS_RegPackage().equals(hdList.get(k).getsS_RegPackage())) {
                                        hdList.get(k).setSL_Organize_S_Id(hoseLists.get(j).getsSL_Organize_S_Id());
                                        hdList.get(k).setsS_Fname(hoseLists.get(j).getsS_FullName());
                                        hostDS.add(hdList.get(k));
                                    }
                                }
                            }
                        }
                    }
                }
             /*   Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler2.sendMessage(msg_listData);*/
            }
        }).start();
    }
    //查询主机以及分控(操作员)
    //操作员
    private void getData2(){
       /* progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));*/
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主机
                List<Host> hoseLists = HostHttp.SelectAllHost(user);
                //用户信息
                List<SelectUser> selectUList1 = UserHttp.thisUser(user);
                List<Organ> organList =  OrganHttp.selectAllProj(user);

                if (organList == null || organList.size() <1){
                    organList = OrganHttp.selectAllProj(user);
                    if (organList == null || organList.size() <1){
                       /* //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }
                if (hoseLists == null || hoseLists.size() <1){
                    hoseLists = HostHttp.SelectAllHost(user);
                    if (hoseLists == null || hoseLists.size() <1){
                      /*  //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }
                if (selectUList1 == null || selectUList1.size() <1){
                    selectUList1 = UserHttp.thisUser(user);
                    if (selectUList1 == null || selectUList1.size() <1){
                      /*  //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }
                if(selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() ==""){
                   /* //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);*/
                    return;
                }
                String [] proList;
                if(selectUList1.get(0).getsS_Project().contains(",")){
                    proList = selectUList1.get(0).getsS_Project().split(",");
                }else{
                    proList = (selectUList1.get(0).getsS_Project()+",").split(",");
                }


                //分控
                List<ControlS> subList = SubHttp.SelectAllSub(user);
                if (subList == null || subList.size() <1){
                    subList = SubHttp.SelectAllSub(user);
                    if (subList == null || subList.size() <1){
                      /*  //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }

                //灯光
                List<Lampj> LampjList = LampHttp.SelectAlllamp(user);
                if (LampjList == null || LampjList.size() <1){
                    LampjList = LampHttp.SelectAlllamp(user);
                    if (LampjList == null || LampjList.size() <1){
                      /*  //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }

                //查询所有主机状态信息
                List<HostDataSet> hdList = LoopHttp.SelectAllHostDataSet(user);
                if (hdList == null || hdList.size() < 1) {
                    hdList = LoopHttp.SelectAllHostDataSet(user);
                    if (hdList == null || hdList.size() < 1){
                     /*   //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);*/
                        return;
                    }
                }
                for (int a = 0; a <proList.length ; a++) {
                    for (int i = 0; i < organList.size(); i++) {
                        if (proList[a].equals(organList.get(i).getsS_Id())){
                            String proID = organList.get(i).getsS_Id();
                            for (int j = 0; j < hoseLists.size(); j++) {
                                String userOrg = hoseLists.get(j).getsSL_Organize_S_Id();
                                String RegPackage = hoseLists.get(j).getsS_RegPackage();
                                if (userOrg.equals(proID)) {
                                    hostList.add(hoseLists.get(j));
                                    for (int k = 0; k < subList.size(); k++) {
                                        String RegPackageFk = subList.get(k).getsSL_HostBast_RegPackage();
                                        String SupID = subList.get(k).getsS_Id();
                                        if (RegPackage.equals(RegPackageFk)) {
                                            contrlist.add(subList.get(k));
                                            for (int l = 0; l < LampjList.size(); l++) {
                                                String SupNumid = LampjList.get(l).getsSL_SubController_Id();
                                                if (SupID.equals(SupNumid)) {
                                                    lamplist.add(LampjList.get(l));
                                                }
                                            }

                                        }
                                    }
                                    for (int k = 0; k < hdList.size(); k++) {
                                        if (hoseLists.get(j).getsS_RegPackage().equals(hdList.get(k).getsS_RegPackage())) {
                                            hdList.get(k).setSL_Organize_S_Id(hoseLists.get(j).getsSL_Organize_S_Id());
                                            hdList.get(k).setsS_Fname(hoseLists.get(j).getsS_FullName());
                                            hostDS.add(hdList.get(k));
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
          /*      //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler2.sendMessage(msg_listData);*/
            }
        }).start();
    }

   /* private Handler handler2 = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGETYPE_01:
                    //刷新UI，显示数据，并关闭进度条
                    progressDialog.dismiss(); //关闭进度条
                    break;
            }
        }
    };*/
}
