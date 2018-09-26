package com.zzb.sl.UserBasicsInfo.PorjActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.zzb.bean.Organ;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.OrganHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AddProjActivity extends SwipeBackActivity implements View.OnClickListener {
    private EditText addproactivity_info_zh, addproactivity_info_pwd, addproactivity_info_name,
            addproactivity_info_Nname, addproactivity_info_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_proj);
        addproactivity_info_zh = (EditText) findViewById(R.id.addproactivity_info_zh);
        addproactivity_info_pwd = (EditText) findViewById(R.id.addproactivity_info_pwd);
        addproactivity_info_name = (EditText) findViewById(R.id.addproactivity_info_name);
        addproactivity_info_Nname = (EditText) findViewById(R.id.addproactivity_info_Nname);
        addproactivity_info_phone = (EditText) findViewById(R.id.addproactivity_info_phone);
        findViewById(R.id.addproactivity_info_img2).setOnClickListener(this);
        findViewById(R.id.addproactivity_info_img3).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addproactivity_info_img2:
                finish();
                break;
            case R.id.addproactivity_info_img3:
                //修改信息
                if (addproactivity_info_zh.getText().length() < 2){
                    ActivityUtil.showToasts(AddProjActivity.this,R.string.TheNameMustNotBeLessThan2Bits,1*1000);
                }else if (addproactivity_info_pwd.getText().length() < 1){
                    ActivityUtil.showToasts(AddProjActivity.this,R.string.ContactsMustNotBeLessThan1Bits,1*1000);
                }else if (addproactivity_info_name.getText().length()<1){
                    ActivityUtil.showToasts(AddProjActivity.this,R.string.TheTelephoneCannotBeEmpty,1*1000);
                }else if (addproactivity_info_Nname.getText().length()<1){
                    ActivityUtil.showToasts(AddProjActivity.this,R.string.AddressCannotBeEmpty,1*1000);
                }else if (addproactivity_info_phone.getText().length()<1){
                    ActivityUtil.showToasts(AddProjActivity.this,R.string.RemarksCannotBeNull,1*1000);
                }else{
                    addUser();
                }
                break;
        }
    }

    private void addUser() {
        final User user = TotalUrl.getUser();
        //所属机构
        final String org = user.getdate().getOrganizeId();
        final Organ addorg = new Organ();
        //设置对话框标题
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.Add))
                .setMessage(getResources().getString(R.string.WhetherToAdd))
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        //名称
                        addorg.setsS_FullName(addproactivity_info_zh.getText().toString());
                        //联系人
                        addorg.setsS_ManagerId(addproactivity_info_pwd.getText().toString());
                        //电话
                        addorg.setsS_TelePhone(addproactivity_info_name.getText().toString());
                        //地址
                        addorg.setsS_Address(addproactivity_info_Nname.getText().toString());
                        //备注
                        addorg.setsS_Description(addproactivity_info_phone.getText().toString());
                        //机构
                        addorg.setsS_ParentId(org);
                        addorg.setsS_Layers(1);
                        addorg.setsS_EnabledMark(1);
                        addorg.setsS_CategoryId("1");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = OrganHttp.addOrg(user,addorg);
                                if (b){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(AddProjActivity.this,R.string.Addsuccess,1*1000);
                                            finish();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(AddProjActivity.this,R.string.Addfailed,1*1000);
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
