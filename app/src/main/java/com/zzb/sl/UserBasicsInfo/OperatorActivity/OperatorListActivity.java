package com.zzb.sl.UserBasicsInfo.OperatorActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.zzb.sl.UserBasicsInfo.OperatorActivity.opreatorCustomView.ItemRemoveRecyclerView;
import com.zzb.sl.UserBasicsInfo.OperatorActivity.opreatorCustomView.MyAdapter;
import com.zzb.sl.UserBasicsInfo.OperatorActivity.opreatorCustomView.OnItemClickListener;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.OrganHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OperatorListActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG ="OperatorListActivity" ;
    private ItemRemoveRecyclerView itrr;
    private SwipeRefreshLayout operator_srfl;
    private List<SelectUser> slU = new ArrayList<>();
    private boolean isPause;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_list);
        itrr = (ItemRemoveRecyclerView) findViewById(R.id.operator_re_container);
        operator_srfl = (SwipeRefreshLayout) findViewById(R.id.operator_srfl);

        getData();
        operator_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //初始化
                slU.clear();
                getData();
                operator_srfl.setRefreshing(false);
            }
        });


        findViewById(R.id.operator__img2).setOnClickListener(this);
        findViewById(R.id.operator_img3).setOnClickListener(this);
    }

    //获取数据
    private void getData(){
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        final User u = TotalUrl.getUser();
        final String userOrg = u.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SelectUser> selectUsers = UserHttp.getAllUser(u);
                if (selectUsers == null){
                    selectUsers = UserHttp.getAllUser(u);
                    if (selectUsers == null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ActivityUtil.showToasts(OperatorListActivity.this, R.string.PleaseCheckTheNetwork, 1 * 1000);
                            }
                        });
                        return;
                    }
                }
                for (int i = 0; i < selectUsers.size(); i++) {
                    if (selectUsers.get(i).getsS_USER_RANKID() > 2){
                        if (selectUsers.get(i).getsSL_Organize_S_Id().equals(userOrg)){
                            slU.add(selectUsers.get(i));
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(OperatorListActivity.this, slU);
                        itrr.setLayoutManager(new LinearLayoutManager(OperatorListActivity.this));
                        itrr.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(OperatorListActivity.this);
                        itrr.setLayoutManager(l);

                        itrr.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                SelectUser selectUser = slU.get(position);
                                Intent intent = new Intent();
                                intent.setClass(OperatorListActivity.this, OperatorInfoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_OPERATOR", selectUser);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }

                            @Override
                            public void onDeleteClick(final int position) {
                                //设置对话框标题
                                new AlertDialog.Builder(OperatorListActivity.this).setTitle(getResources().getString(R.string.deleteUser))
                                        .setMessage(getResources().getString(R.string.delete))
                                        .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                SelectUser selectUser = slU.get(position);
                                                deleteUser(selectUser.getsS_Id());
                                                adapter.removeItem(position);
                                            }
                                        }).setNegativeButton(getResources().getString(R.string.No),new DialogInterface.OnClickListener() {//添加返回按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//响应事件
                                    }
                                }).show();//在按键响应事件中显示此对话框

                            }

                            @Override
                            public void onProjClick(final int position) {
                                SelectUser selectUser = slU.get(position);
                                showMutilAlertDialog(selectUser);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.operator__img2:
                finish();
                break;
            case R.id.operator_img3:
                Intent intent = new Intent(OperatorListActivity.this,AddOperatorActivity.class);
                startActivity(intent);
                break;
        }
    }

    //删除用户
    private void deleteUser(final String userID){
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserHttp.DeleteUser(userID,user);
                if (b){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(OperatorListActivity.this, R.string.DeleteSuccessfully, 1 * 1000);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActivityUtil.showToasts(OperatorListActivity.this, R.string.DeleteFailed, 1 * 1000);
                    }
                    });
                }
            }
        }).start();
    }

    // 多选提示框
    private AlertDialog alertDialog3;
    private String a = "";
    private String b = "";

    public void showMutilAlertDialog(final SelectUser selectU){
        a = "";
        b = "";
        final User user = TotalUrl.getUser();
        final String userPro = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(OperatorListActivity.this, R.string.PleaseAddItemInformationFirst, 1 * 1000);
                        }
                    });
                }else{
                    for (int i = 0; i <organList.size() ; i++) {
                        String pro = organList.get(i).getsS_ParentId();
                       if (pro.equals(userPro)){
                            a += organList.get(i).getsS_FullName()+",";
                            b +=organList.get(i).getsS_Id()+",";
                        }
                    }
                    Log.e(TAG,"项目："+b);
                }
                if (a !=null || a !=","){
                    final String [] proj = a.split(",");
                    final String [] proj2 = b.split(",");
                    final boolean[] selected = new boolean[proj.length];
                    final boolean[] selected2 = new boolean[proj2.length];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 创建一个AlertDialog建造者
                            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(OperatorListActivity.this);
                            // 设置标题
                            alertDialogBuilder.setTitle(getResources().getString(R.string.AssignmentItem));
                            // 参数介绍
                            // 第一个参数：弹出框的信息集合，一般为字符串集合
                            // 第二个参数：被默认选中的，一个布尔类型的数组
                            // 第三个参数：勾选事件监听
                            alertDialogBuilder.setMultiChoiceItems(proj, null, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    // dialog：不常使用，弹出框接口
                                    // which：勾选或取消的是第几个
                                    // isChecked：是否勾选
                                    if (isChecked) {
                                        // 选中
                                        selected[which] = isChecked;
                                        selected2[which] = isChecked;
                                    }else {
                                        // 选中
                                        selected[which] = isChecked;
                                        selected2[which] = isChecked;
                                        // 取消选中

                                    }

                                }
                            });
                            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //TODO 业务逻辑代码
                                    String selectedStr = "";
                                    if (proj2.length<1){
                                        ActivityUtil.showToasts(OperatorListActivity.this, "请选择项目信息", 1 * 1000);
                                        return;
                                    }
                                    for(int i=0; i<selected2.length; i++) {
                                        if(selected2[i] == true) {
                                            selectedStr += proj2[i]+",";
                                        }
                                    }
                                    if (selectedStr == "," || selectedStr == "" ){
                                        ActivityUtil.showToasts(OperatorListActivity.this, "请选择项目信息", 1 * 1000);
                                        return;
                                    }
                                    Log.e(TAG,"点击确定："+selectedStr);
                                    selectedStr = selectedStr.substring(0,selectedStr.length() -1);
                                    setProj(selectU,selectedStr);
                                    // 关闭提示框
                                    alertDialog3.dismiss();
                                }
                            });
                            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO 业务逻辑代码
                                    // 关闭提示框
                                    alertDialog3.dismiss();
                                }
                            });
                            alertDialog3 = alertDialogBuilder.create();
                            alertDialog3.show();
                        }
                    });
                }

            }
        }).start();
    }

    //分配所属项目
    private void setProj(final SelectUser selectUser,String proList){
        final User user = TotalUrl.getUser();
        selectUser.setsS_Project(proList);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserHttp.UpdataUserProj(selectUser,user);
                if (b) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                ActivityUtil.showToasts(OperatorListActivity.this, R.string.AssignmentComplete, 1 * 1000);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(OperatorListActivity.this, R.string.allocationFailed, 1 * 1000);
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
