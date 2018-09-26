package com.zzb.sl.UserBasicsInfo.PorjActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.zzb.bean.Organ;

import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.OrganHttp;

import com.zzb.sl.UserBasicsInfo.PorjActivity.projInfoCustomView.ItemRemoveRecyclerView;
import com.zzb.sl.UserBasicsInfo.PorjActivity.projInfoCustomView.MyAdapter;
import com.zzb.sl.UserBasicsInfo.PorjActivity.projInfoCustomView.OnItemClickListener;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ProjInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG ="OperatorListActivity" ;
    private ItemRemoveRecyclerView itrr;
    private SwipeRefreshLayout proj_srfl;
    private List<Organ> slU = new ArrayList<>();
    private boolean isPause;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj_info);
        itrr = (ItemRemoveRecyclerView) findViewById(R.id.proj_re_container);
        proj_srfl = (SwipeRefreshLayout) findViewById(R.id.proj_srfl);
            getData();
            proj_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    slU.clear();
                    getData();
                    proj_srfl.setRefreshing(false);
                }
            });
        findViewById(R.id.proj__img2).setOnClickListener(this);
        findViewById(R.id.proj_img3).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.proj__img2:
                finish();
                break;
            case R.id.proj_img3:
                Intent intent = new Intent(ProjInfoActivity.this,AddProjActivity.class);
                startActivity(intent);
                break;
        }
    }

   private void getData() {
       progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
       progressDialog.setCancelable(true);
       progressDialog.setCanceledOnTouchOutside(false);
       final User u = TotalUrl.getUser();
       final String userOrg = u.getdate().getOrganizeId();
       new Thread(new Runnable() {
           @Override
           public void run() {
               List<Organ> organList =  OrganHttp.selectAllProj(u);
               if (organList == null){
                   organList =  OrganHttp.selectAllProj(u);
                   if (organList == null){
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               ActivityUtil.showToasts(ProjInfoActivity.this, R.string.PleaseCheckTheNetwork, 1 * 1000);

                           }
                       });
                   }
                   //发送handler
                   Message msg_listData = new Message();
                   msg_listData.what = MESSAGETYPE_01;
                   handler.sendMessage(msg_listData);
                   return;
               }

               for (int i = 0; i < organList.size(); i++) {
                   if(organList.get(i).getsS_ParentId().equals(userOrg)){
                       slU.add(organList.get(i));
                   }
               }
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       final MyAdapter adapter = new MyAdapter(ProjInfoActivity.this,slU);
                       itrr.setLayoutManager(new LinearLayoutManager(ProjInfoActivity.this));
                       itrr.setAdapter(adapter);
                       LinearLayoutManager l = new LinearLayoutManager(ProjInfoActivity.this);
                       itrr.setLayoutManager(l);
                       itrr.setOnItemClickListener(new OnItemClickListener() {
                           @Override
                           public void onItemClick(View view, int position) {
                               Organ org = slU.get(position);
                               Intent intent = new Intent();
                               intent.setClass(ProjInfoActivity.this, ProjActivity.class);
                               Bundle bundle = new Bundle();
                               bundle.putSerializable("PROJINFOACTIVITY", org);
                               intent.putExtras(bundle);
                               startActivity(intent);
                           }
                           @Override
                           public void onDeleteClick(final int position) {
                               //设置对话框标题
                                   new AlertDialog.Builder(ProjInfoActivity.this).setTitle(getResources().getString(R.string.deleteItem))
                                       .setMessage(getResources().getString(R.string.delete))
                                       .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                               Organ org = slU.get(position);
                                               deleteproj(org.getsS_Id());
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

    //删除
    private void deleteproj(final String deleteId){
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = OrganHttp.DeleteOrgan(deleteId,user);
                if (b){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(ProjInfoActivity.this, R.string.DeleteSuccessfully, 1 * 1000);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(ProjInfoActivity.this,  R.string.DeleteFailed, 1 * 1000);

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
                slU.clear();
                getData();

        }
    }

}
