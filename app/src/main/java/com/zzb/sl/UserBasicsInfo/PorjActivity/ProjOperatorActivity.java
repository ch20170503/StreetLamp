package com.zzb.sl.UserBasicsInfo.PorjActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.OrganHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.UserBasicsInfo.PorjActivity.projoperatorCustomView.ItemRemoveRecyclerView;
import com.zzb.sl.UserBasicsInfo.PorjActivity.projoperatorCustomView.MyAdapter;
import com.zzb.sl.UserBasicsInfo.PorjActivity.projoperatorCustomView.OnItemClickListener;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ProjOperatorActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG ="OperatorListActivity" ;
    private ItemRemoveRecyclerView itrr;
    private SwipeRefreshLayout proj_srfl;
    private List<Organ> slU = new ArrayList<>();
    private boolean isPause;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;

    private List<Organ> pros= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj_operator);
        proj_srfl = (SwipeRefreshLayout) findViewById(R.id.projoper_srfl);
        itrr = (ItemRemoveRecyclerView) findViewById(R.id.projoper_re_container);
        findViewById(R.id.projoper__img2).setOnClickListener(this);
        getData2();
        proj_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //初始化
                pros.clear();
                getData2();
                proj_srfl.setRefreshing(false);
            }
        });
    }


    //操作员
    private void getData2() {
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SelectUser> selectUsers = UserHttp.thisUser(user);
                List<Organ> organList =  OrganHttp.selectAllProj(user);
                if (organList  == null || organList.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(ProjOperatorActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                if (selectUsers == null || selectUsers.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(ProjOperatorActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }else{
                    String pro = selectUsers.get(0).getsS_Project();

                    String [] proList = pro.split(",");
                    if (proList.length>=1){
                        for (int i = 0; i < proList.length; i++) {
                            for (int j = 0; j <organList.size() ; j++) {
                                if (proList[i].equals(organList.get(j).getsS_Id())){
                                    pros.add(organList.get(j));
                                }
                            }
                        }
                    }
                    if (pros.size() <1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ActivityUtil.showToasts(ProjOperatorActivity.this,R.string.Currentlynoinformation,1*1000);
                            }
                        });
                        //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);
                        return;
                    }

                    Log.e(TAG,"pros:"+pros);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final MyAdapter adapter = new MyAdapter(ProjOperatorActivity.this,pros);
                            itrr.setLayoutManager(new LinearLayoutManager(ProjOperatorActivity.this));
                            itrr.setAdapter(adapter);
                            LinearLayoutManager l = new LinearLayoutManager(ProjOperatorActivity.this);
                            itrr.setLayoutManager(l);
                            itrr.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Organ org = pros.get(position);
                                    Intent intent = new Intent();
                                    intent.setClass(ProjOperatorActivity.this, ProjOperatorinfoActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("SELECTUSER_ORGOPEN", org);
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
    public void onClick(View v) {
        finish();
    }
}
