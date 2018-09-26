package com.zzb.sl.UserBasicsInfo.OperatorActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OperatorInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    //创建操作员
    private SelectUser selectUser = new SelectUser();
    //页面控件信息
    private EditText zh, pwd, name, nname, phone, email, bezhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_info);
        findViewById(R.id.operator_info_img2).setOnClickListener(this);
        findViewById(R.id.operator_info_img3).setOnClickListener(this);
        zh = (EditText) findViewById(R.id.op_info_zh);
        pwd = (EditText) findViewById(R.id.op_info_pwd);
        name = (EditText) findViewById(R.id.op_info_name);
        nname = (EditText) findViewById(R.id.op_info_Nname);
        phone = (EditText) findViewById(R.id.op_info_phone);
        email = (EditText) findViewById(R.id.op_info_email);
        bezhu = (EditText) findViewById(R.id.op_info_bezhu);
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.operator_info_img2:
                finish();
                break;
            case R.id.operator_info_img3:
               //修改用户信息
                if (zh.getText().length() < 6){
                    ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.UserNameMustNotBeLessThan6Bits,1*1000);
                }else if (pwd.getText().length() < 6){
                    ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.PasswordMustNotBeLessThan6Bits,1*1000);
                }else if (name.getText().length()<1){
                    ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.TheNameCannotBeEmpty,1*1000);
                }else if (nname.getText().length()<1){
                    ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.NicknamesCannotBeEmpty,1*1000);
                }else if (phone.getText().length()<1){
                    ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.TheTelephoneCannotBeEmpty,1*1000);
                }else if (email.getText().length()<1){
                    ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.EmailCannotBeEmpty,1*1000);
                }else if (bezhu.getText().length() <1){
                    ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.RemarksCannotBeNull,1*1000);
                }else{
                    upDataUser();
                }

                break;
        }
    }

    //获取数据
    private void getData() {
        //获取Intent中传递对对象
        Intent intent = this.getIntent();
        selectUser = (SelectUser) intent.getSerializableExtra("SELECTUSER_OPERATOR");
        //设置页面信息
        zh.setText(selectUser.getsS_Account());
        pwd.setText(selectUser.getsS_Password());
        name.setText(selectUser.getsS_RealName());
        nname.setText(selectUser.getsS_NickName());
        phone.setText(selectUser.getsS_MobilePhone());
        email.setText(selectUser.getsS_Email());
        bezhu.setText(selectUser.getsS_Description());
    }

    //修改操作员
    private void upDataUser(){
        final User user = TotalUrl.getUser();
        //获取页面数据
        //获取Intent中传递对对象
        Intent intent = this.getIntent();
        final SelectUser upselectUser = (SelectUser) intent.getSerializableExtra("SELECTUSER_OPERATOR");
        //设置对话框标题
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.revise))
                .setMessage(getResources().getString(R.string.Modify))
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        upselectUser.setsS_Account(zh.getText().toString());
                        upselectUser.setsS_Password(pwd.getText().toString());
                        upselectUser.setsS_RealName(name.getText().toString());
                        upselectUser.setsS_NickName(nname.getText().toString());
                        upselectUser.setsS_MobilePhone(phone.getText().toString());
                        upselectUser.setsS_Email(email.getText().toString());
                        upselectUser.setsS_Description(bezhu.getText().toString());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = UserHttp.UpdataUser(upselectUser,user);
                                if (b){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.Updatasuccess,1*1000);
                                            finish();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(OperatorInfoActivity.this,R.string.Updatafailed,1*1000);
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
}
