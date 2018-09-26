package com.zzb.sl.group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.zzb.bean.Host;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.sl.group.grouphostCustomView.ItemRemoveRecyclerView;
import com.zzb.sl.group.grouphostCustomView.MyAdapter;
import com.zzb.sl.group.grouphostCustomView.OnItemClickListener;
import com.zzb.http.HostHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GroupHostActivity extends SwipeBackActivity {
    private static final String TAG ="GroupHostActivity" ;
    private ItemRemoveRecyclerView itrr;
    private SwipeRefreshLayout proj_srfl;
    private List<Host> hostList = new ArrayList<>();
    private boolean isPause;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private ImageView grouphost_list_img2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_config);
        proj_srfl = (SwipeRefreshLayout) findViewById(R.id.grouphost_list_srfl);
        itrr = (ItemRemoveRecyclerView) findViewById(R.id.grouphost_re_container);
        grouphost_list_img2 = (ImageView) findViewById(R.id.grouphost_list_img2);
        grouphost_list_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        User user = TotalUrl.getUser();
        int userQ = user.getdate().getSL_USER_RANKID();
        if (userQ == 2){
            getData();
            proj_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    hostList.clear();
                    getData();
                    proj_srfl.setRefreshing(false);
                }
            });
        }else{
            getData2();
            proj_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.e(TAG,"333333");
                    //初始化
                    hostList.clear();
                    getData2();
                    proj_srfl.setRefreshing(false);
                }
            });
        }

    }

    private void getData() {
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupHostActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }

                if (organList == null|| organList.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupHostActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                for (int i = 0; i <organList.size() ; i++) {
                    String pro = organList.get(i).getsS_ParentId();
                    String proID = organList.get(i).getsS_Id();
                    if (pro.equals(userPro)){
                        for (int j = 0; j < hoseLists.size(); j++) {
                            String org = hoseLists.get(j).getsSL_Organize_S_Id();
                            if (org.equals(proID)){
                                hostList.add(hoseLists.get(j));
                            }
                        }
                    }
                }
                if (hostList == null|| hostList.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupHostActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }

                Log.e(TAG,"遍历完成后的:"+hostList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(GroupHostActivity.this,hostList);
                        itrr.setLayoutManager(new LinearLayoutManager(GroupHostActivity.this));
                        itrr.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(GroupHostActivity.this);
                        itrr.setLayoutManager(l);
                        itrr.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Host ho = hostList.get(position);
                                Log.e(TAG,"跳转的:"+ho);
                                Intent intent = new Intent();
                                intent.setClass(GroupHostActivity.this, GroupInfoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_GROUPHOST", ho);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler.sendMessage(msg_listData);
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

    //操作员
    private void getData2(){
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupHostActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                if (hoseLists == null || hoseLists.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupHostActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                if (selectUList1 == null || selectUList1.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupHostActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                if(selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() ==""){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupHostActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                String [] proList;
                if(selectUList1.get(0).getsS_Project().contains(",")){
                    proList = selectUList1.get(0).getsS_Project().split(",");
                }else{
                    proList = (selectUList1.get(0).getsS_Project()+",").split(",");
                }
                for (int a = 0; a <proList.length ; a++) {
                    for (int i = 0; i <organList.size() ; i++) {
                        if (proList[a].equals(organList.get(i).getsS_Id())){
                            String proID = organList.get(i).getsS_Id();
                            for (int j = 0; j < hoseLists.size(); j++) {
                                String userOrg = hoseLists.get(j).getsSL_Organize_S_Id();
                                if (userOrg.equals(proID)) {
                                    hostList.add(hoseLists.get(j));
                                }
                            }
                        }
                    }
                }
                if (hostList == null || hostList.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupHostActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }

                Log.e(TAG,"遍历完成后的:"+hostList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(GroupHostActivity.this,hostList);
                        itrr.setLayoutManager(new LinearLayoutManager(GroupHostActivity.this));
                        itrr.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(GroupHostActivity.this);
                        itrr.setLayoutManager(l);
                        itrr.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Host ho = hostList.get(position);
                                Log.e(TAG,"跳转的:"+ho);
                                Intent intent = new Intent();
                                intent.setClass(GroupHostActivity.this, GroupInfoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_GROUPHOST", ho);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler2.sendMessage(msg_listData);
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


    @Override
    protected void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isPause){ //判断是否暂停
            User user = TotalUrl.getUser();
            int userQ = user.getdate().getSL_USER_RANKID();
            if (userQ == 2){
                isPause = false;
                hostList.clear();
                getData();
            }else {
                isPause = false;
                hostList.clear();
                getData2();
            }
        }
    }

}
