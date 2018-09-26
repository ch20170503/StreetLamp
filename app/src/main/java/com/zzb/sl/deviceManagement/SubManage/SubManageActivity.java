package com.zzb.sl.deviceManagement.SubManage;

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
import com.zzb.bean.Host;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.SubHttp;
import com.zzb.sl.R;

import com.zzb.sl.deviceManagement.SubManage.subCustomView.ItemRemoveRecyclerView;
import com.zzb.sl.deviceManagement.SubManage.subCustomView.MyAdapter;
import com.zzb.sl.deviceManagement.SubManage.subCustomView.OnItemClickListener;
import com.zzb.util.ActivityUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SubManageActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "SubManageActivity";
    private ItemRemoveRecyclerView sub_re_container;
    private SwipeRefreshLayout sub_list_srfl;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private Host host = new Host();
    private List<ControlS> lampLists;
    private boolean isPause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_manage);
        sub_re_container = (ItemRemoveRecyclerView) findViewById(R.id.sub_re_container);
        sub_list_srfl = (SwipeRefreshLayout) findViewById(R.id.sub_list_srfl);
        findViewById(R.id.sub_list_img2).setOnClickListener(this);
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_SUBHOST");
        getData();
        sub_list_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //初始化
                if(lampLists !=null){
                    lampLists.clear();
                }
              getData();
                sub_list_srfl.setRefreshing(false);
            }
        });
    }


    //获取数据
    private void getData(){
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String pcke = host.getsS_RegPackage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主机
                lampLists = SubHttp.selectPackSub(user,pcke);
                if (lampLists == null || lampLists.size()<1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(SubManageActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                Collections.sort(lampLists, new Comparator<ControlS>() {
                    @Override
                    public int compare(ControlS o1, ControlS o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(SubManageActivity.this,lampLists);
                        sub_re_container.setLayoutManager(new LinearLayoutManager(SubManageActivity.this));
                        sub_re_container.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(SubManageActivity.this);
                        sub_re_container.setLayoutManager(l);
                        sub_re_container.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                ControlS lampj = lampLists.get(position);
                                Log.e(TAG,"跳转的:"+lampj);
                                Intent intent = new Intent();
                                intent.setClass(SubManageActivity.this, SubInfoManageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_SUBINFO", lampj);
                                bundle.putSerializable("SELECTUSER_SUBINFOHOST", host);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                            @Override
                            public void onDeleteClick(final int position) {
                                //设置对话框标题
                                new AlertDialog.Builder(SubManageActivity.this).setTitle(getResources().getString(R.string.deleteItem))
                                        .setMessage(getResources().getString(R.string.delete))
                                        .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                ControlS ho = lampLists.get(position);
                                                deletesub(ho.getsS_Id(),adapter,position);

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



    private void deletesub(final String id, final MyAdapter adapter, final int position){
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = SubHttp.DeleteSub(id,user);
                if (b){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(SubManageActivity.this, R.string.DeleteSuccessfully, 1 * 1000);
                            adapter.removeItem(position);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(SubManageActivity.this,  R.string.DeleteFailed, 1 * 1000);
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isPause){ //判断是否暂停
            isPause = false;
            //初始化
            if(lampLists !=null){
                lampLists.clear();
            }
            getData();
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
