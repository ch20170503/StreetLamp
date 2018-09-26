package com.zzb.sl.UserBasicsInfo.OperatorActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AddOperatorActivity extends SwipeBackActivity implements View.OnClickListener {
    //页面控件信息
    private EditText zh, pwd, name, nname, phone, email, bezhu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operator);
        findViewById(R.id.operator_add_img2).setOnClickListener(this);
        findViewById(R.id.operator_add_img3).setOnClickListener(this);
        zh = (EditText) findViewById(R.id.op_add_zh);
        pwd = (EditText) findViewById(R.id.op_add_pwd);
        name = (EditText) findViewById(R.id.op_add_name);
        nname = (EditText) findViewById(R.id.op_add_Nname);
        phone = (EditText) findViewById(R.id.op_add_phone);
        email = (EditText) findViewById(R.id.op_add_email);
        bezhu = (EditText) findViewById(R.id.op_add_bezhu);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.operator_add_img2:
                finish();
                break;
            case R.id.operator_add_img3:
                //添加用户信息
             if (zh.getText().length() < 6){
                 ActivityUtil.showToasts(AddOperatorActivity.this,R.string.UserNameMustNotBeLessThan6Bits,1*1000);
             }else if (pwd.getText().length() < 6){
                 ActivityUtil.showToasts(AddOperatorActivity.this,R.string.PasswordMustNotBeLessThan6Bits,1*1000);
             }else if (name.getText().length()<1){
                 ActivityUtil.showToasts(AddOperatorActivity.this,R.string.TheNameCannotBeEmpty,1*1000);
             }else if (nname.getText().length()<1){
                 ActivityUtil.showToasts(AddOperatorActivity.this,R.string.NicknamesCannotBeEmpty,1*1000);
             }else if (phone.getText().length()<1){
                 ActivityUtil.showToasts(AddOperatorActivity.this,R.string.TheTelephoneCannotBeEmpty,1*1000);
             }else if (email.getText().length()<1){
                 ActivityUtil.showToasts(AddOperatorActivity.this,R.string.EmailCannotBeEmpty,1*1000);
             }else if (bezhu.getText().length() <1){
                 ActivityUtil.showToasts(AddOperatorActivity.this,R.string.RemarksCannotBeNull,1*1000);
             }else{
                 getData();
             }
                break;
        }
    }

    //添加用户信息
    private void getData(){
        final User user = TotalUrl.getUser();
        final SelectUser addselectUser = new SelectUser();
        //设置对话框标题
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.Add))
                .setMessage(getResources().getString(R.string.WhetherToAdd))
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        addselectUser.setsS_Account(zh.getText().toString());
                        addselectUser.setsS_Password(pwd.getText().toString());
                        addselectUser.setsS_RealName(name.getText().toString());
                        addselectUser.setsS_NickName(nname.getText().toString());
                        addselectUser.setsS_MobilePhone(phone.getText().toString());
                        addselectUser.setsS_Email(email.getText().toString());

                        addselectUser.setsS_Description(bezhu.getText().toString());
                        //用户头像
                        addselectUser.setsS_HeadIcon("1");
                        //是否有效
                        addselectUser.setsS_EnabledMark(1);
                        //所属机构
                        addselectUser.setsSL_Organize_S_Id(user.getdate().getOrganizeId());
                        //用户等级
                        addselectUser.setsS_USER_RANKID(3);
                       //用户所属角色
                        addselectUser.setsSL_Role_S_Id(user.getdate().getRoleId());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = UserHttp.addUser(user,addselectUser);
                                if (b){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(AddOperatorActivity.this,R.string.Addsuccess,1*1000);
                                            finish();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(AddOperatorActivity.this,R.string.Addfailed,1*1000);
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
