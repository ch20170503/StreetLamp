package com.zzb.sl.strategy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.zzb.bean.ControlS;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.Strategy;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.HostHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.StrategyHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.sl.deviceManagement.HostManage.HostManageActivity;
import com.zzb.sl.strategy.strategyInfoCustomView.ItemRemoveRecyclerView;
import com.zzb.sl.strategy.strategyInfoCustomView.MyAdapter;
import com.zzb.sl.strategy.strategyInfoCustomView.OnItemClickListener;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class StrategyInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "StrategyInfoActivity";
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private List<Strategy> strategyList = new ArrayList<>();
    private ItemRemoveRecyclerView strategy_item_irrlv;
    private SwipeRefreshLayout strategy_list_srfl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_info);
        findViewById(R.id.Strategy_list_img2).setOnClickListener(this);
        findViewById(R.id.Strategy_list_img3).setOnClickListener(this);
        //获取id
        strategy_item_irrlv = (ItemRemoveRecyclerView) findViewById(R.id.strategy_item_irrlv);
        strategy_list_srfl = (SwipeRefreshLayout) findViewById(R.id.strategy_list_srfl);
        User user = TotalUrl.getUser();
        int userQ = user.getdate().getSL_USER_RANKID();
        if (userQ == 2){
            getData();
            strategy_list_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    strategyList.clear();
                    getData();
                    strategy_list_srfl.setRefreshing(false);
                }
            });
        }else{
            getData2();
            strategy_list_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    strategyList.clear();
                    getData2();
                    strategy_list_srfl.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Strategy_list_img2:
                finish();
                break;
            case R.id.Strategy_list_img3:
                Intent intent = new Intent(StrategyInfoActivity.this,AddStrategyActivity.class);
                startActivity(intent);
                break;
        }
    }

    //获取数据
    private void getData(){
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String userPro = user.getdate().getOrganizeId();
        new Thread(new Runnable() {

            @Override
            public void run() {
                //查询项目信息
                List<Organ> organList =  OrganHttp.selectAllProj(user);
                // 查询所有策略信息
                List<Strategy> strategy = StrategyHttp.SelectAllStrategy(user);
                if (organList == null || organList.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }

                if (strategy == null || strategy.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                Collections.sort(strategy, new Comparator<Strategy>() {
                    @Override
                    public int compare(Strategy o1, Strategy o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });
                for (int i = 0; i < organList.size(); i++) {
                    String pro = organList.get(i).getsS_ParentId();
                    String proID = organList.get(i).getsS_Id();
                    if (pro.equals(userPro)){
                        for (int j = 0; j <strategy.size() ; j++) {
                            String org = strategy.get(j).getsSL_Organize_S_Id();
                            if (org.equals(proID)){
                                strategyList.add(strategy.get(j));
                            }
                        }
                    }
                }
                if (strategyList == null || strategyList.size()<1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                Log.e(TAG,"遍历完成后的:"+strategyList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(StrategyInfoActivity.this,strategyList);
                        strategy_item_irrlv.setLayoutManager(new LinearLayoutManager(StrategyInfoActivity.this));
                        strategy_item_irrlv.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(StrategyInfoActivity.this);
                        strategy_item_irrlv.setLayoutManager(l);
                        strategy_item_irrlv.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Strategy ho = strategyList.get(position);
                                Log.e(TAG,"跳转的:"+ho);
                                Intent intent = new Intent();
                                intent.setClass(StrategyInfoActivity.this, StrategyActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_STRATEGY", ho);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                            @Override
                            public void onDeleteClick(final int position) {
                                //设置对话框标题
                                new AlertDialog.Builder(StrategyInfoActivity.this).setTitle(getResources().getString(R.string.deleteItem))
                                        .setMessage(getResources().getString(R.string.delete))
                                        .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                Strategy ho = strategyList.get(position);
                                                deleteproj(ho.getsS_Id());
                                                adapter.removeItem(position);
                                            }
                                        }).setNegativeButton(getResources().getString(R.string.No),new DialogInterface.OnClickListener() {//添加返回按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//响应事件
                                    }
                                }).show();//在按键响应事件中显示此对话框
                            }
                        });
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



    private void getData2(){
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //用户信息
                List<SelectUser> selectUList1 = UserHttp.thisUser(user);
                List<Organ> organList =  OrganHttp.selectAllProj(user);
                // 查询所有策略信息
                List<Strategy> strategy = StrategyHttp.SelectAllStrategy(user);
                if (selectUList1 == null || selectUList1.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                if (organList == null || organList.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                if (strategy == null || strategy.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }

                if(selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() ==""){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this, getString(R.string.Currentlynoinformation), 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                String [] proList;
                if(selectUList1.get(0).getsS_Project().contains(",")){
                    proList = selectUList1.get(0).getsS_Project().split(",");
                }else{
                    proList = (selectUList1.get(0).getsS_Project()+",").split(",");
                }


                Collections.sort(strategy, new Comparator<Strategy>() {
                    @Override
                    public int compare(Strategy o1, Strategy o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });
                for (int a = 0; a <proList.length ; a++) {
                    for (int i = 0; i <organList.size() ; i++) {
                        if (proList[a].equals(organList.get(i).getsS_Id())){
                            String proID = organList.get(i).getsS_Id();
                            for (int j = 0; j < strategy.size(); j++) {
                                String userOrg = strategy.get(j).getsSL_Organize_S_Id();
                                if (userOrg.equals(proID)) {
                                    strategyList.add(strategy.get(j));
                                }
                            }
                        }
                    }
                }
                if (strategyList == null || strategyList.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }


                Log.e(TAG,"遍历完成后的:"+strategyList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(StrategyInfoActivity.this,strategyList);
                        strategy_item_irrlv.setLayoutManager(new LinearLayoutManager(StrategyInfoActivity.this));
                        strategy_item_irrlv.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(StrategyInfoActivity.this);
                        strategy_item_irrlv.setLayoutManager(l);
                        strategy_item_irrlv.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Strategy ho = strategyList.get(position);
                                Log.e(TAG,"跳转的:"+ho);
                                Intent intent = new Intent();
                                intent.setClass(StrategyInfoActivity.this, StrategyActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_STRATEGY", ho);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                            @Override
                            public void onDeleteClick(final int position) {
                                //设置对话框标题
                                new AlertDialog.Builder(StrategyInfoActivity.this).setTitle(getResources().getString(R.string.deleteItem))
                                        .setMessage(getResources().getString(R.string.delete))
                                        .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                Strategy ho = strategyList.get(position);
                                                 deleteproj(ho.getsS_Id());
                                                 adapter.removeItem(position);
                                            }
                                        }).setNegativeButton(getResources().getString(R.string.No),new DialogInterface.OnClickListener() {//添加返回按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//响应事件
                                    }
                                }).show();//在按键响应事件中显示此对话框
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
    //删除
    private void deleteproj(final String deleteId){
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = StrategyHttp.DeleteStrategy(deleteId,user);
                if (b){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this, R.string.DeleteSuccessfully, 1 * 1000);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,  R.string.DeleteFailed, 1 * 1000);

                        }
                    });
                }
            }
        }).start();
    }
    /***
     *
     * 
     *
     *
     *
     */
}
