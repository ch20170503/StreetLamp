package com.zzb.sl.deviceManagement.LampManage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.zzb.bean.Host;
import com.zzb.bean.Lampj;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.LampHttp;
import com.zzb.sl.deviceManagement.LampManage.lampCustomView.ItemRemoveRecyclerView;
import com.zzb.sl.deviceManagement.LampManage.lampCustomView.MyAdapter;
import com.zzb.sl.deviceManagement.LampManage.lampCustomView.OnItemClickListener;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class LampManageActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "LampManageActivity";
    private ItemRemoveRecyclerView lamp_re_container;
    private SwipeRefreshLayout lamp_list_srfl;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private Host host = new Host();
    private List<Lampj> lampLists;
    private boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_manage);
        lamp_re_container = (ItemRemoveRecyclerView) findViewById(R.id.lamp_re_container);
        lamp_list_srfl = (SwipeRefreshLayout) findViewById(R.id.lamp_list_srfl);
        findViewById(R.id.lamp_list_img2).setOnClickListener(this);
        findViewById(R.id.lamp_list_img3).setOnClickListener(this);
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_LAMPHOST");
        getData();
        lamp_list_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //初始化
                if(lampLists !=null){
                    lampLists.clear();
                }
                getData();
                lamp_list_srfl.setRefreshing(false);
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
                lampLists = LampHttp.getHostLamp(pcke,user);
                if (lampLists == null || lampLists.size()<1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LampManageActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(LampManageActivity.this,lampLists);
                        lamp_re_container.setLayoutManager(new LinearLayoutManager(LampManageActivity.this));
                        lamp_re_container.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(LampManageActivity.this);
                        lamp_re_container.setLayoutManager(l);
                        lamp_re_container.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Lampj lampj = lampLists.get(position);
                                Log.e(TAG,"跳转的:"+lampj);
                                Intent intent = new Intent();
                                intent.setClass(LampManageActivity.this, LampInfoManageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_LAMPINFO", lampj);
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
        switch (view.getId()){
            case R.id.lamp_list_img2:
                finish();
                break;
            case R.id.lamp_list_img3:
                Intent intent = new Intent();
                intent.setClass(LampManageActivity.this, AddLampActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("SELECTUSER_ADDLAMPHOST", host);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
