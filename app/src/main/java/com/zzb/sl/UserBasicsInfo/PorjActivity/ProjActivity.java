package com.zzb.sl.UserBasicsInfo.PorjActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

public class ProjActivity extends SwipeBackActivity implements View.OnClickListener {
    private Organ organ;
    private EditText proactivity_info_zh, proactivity_info_pwd, proactivity_info_name,
            proactivity_info_Nname, proactivity_info_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj);
        proactivity_info_zh = (EditText) findViewById(R.id.proactivity_info_zh);
        proactivity_info_pwd = (EditText) findViewById(R.id.proactivity_info_pwd);
        proactivity_info_name = (EditText) findViewById(R.id.proactivity_info_name);
        proactivity_info_Nname = (EditText) findViewById(R.id.proactivity_info_Nname);
        proactivity_info_phone = (EditText) findViewById(R.id.proactivity_info_phone);
        findViewById(R.id.proactivity_info_img2).setOnClickListener(this);
        findViewById(R.id.proactivity_info_img3).setOnClickListener(this);
        getData();

    }


    private void getData() {
        //获取Intent中传递对对象
        Intent intent = this.getIntent();
        organ = (Organ) intent.getSerializableExtra("PROJINFOACTIVITY");
        proactivity_info_zh.setText(organ.getsS_FullName());
        proactivity_info_pwd.setText(organ.getsS_ManagerId());
        proactivity_info_name.setText(organ.getsS_TelePhone());
        proactivity_info_Nname.setText(organ.getsS_Address());
        proactivity_info_phone.setText(organ.getsS_Description());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.proactivity_info_img2:
                finish();
                break;
            case R.id.proactivity_info_img3:
                //修改信息
                if (proactivity_info_zh.getText().length() < 2){
                    ActivityUtil.showToasts(ProjActivity.this,R.string.TheNameMustNotBeLessThan2Bits,1*1000);
                }else if (proactivity_info_pwd.getText().length() < 1){
                    ActivityUtil.showToasts(ProjActivity.this,R.string.ContactsMustNotBeLessThan1Bits,1*1000);
                }else if (proactivity_info_name.getText().length()<1){
                    ActivityUtil.showToasts(ProjActivity.this,R.string.TheTelephoneCannotBeEmpty,1*1000);
                }else if (proactivity_info_Nname.getText().length()<1){
                    ActivityUtil.showToasts(ProjActivity.this,R.string.AddressCannotBeEmpty,1*1000);
                }else if (proactivity_info_phone.getText().length()<1){
                    ActivityUtil.showToasts(ProjActivity.this,R.string.RemarksCannotBeNull,1*1000);
                }else{
                    upDataUser();
                }
                break;
        }
    }



    //保存
    private void upDataUser() {
        final User user = TotalUrl.getUser();
        //获取页面数据
        //获取Intent中传递对对象
        Intent intent = this.getIntent();
        final Organ organ = (Organ) intent.getSerializableExtra("PROJINFOACTIVITY");
        //设置对话框标题
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.revise))
                .setMessage(getResources().getString(R.string.Modify))
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        organ.setsS_FullName(proactivity_info_zh.getText().toString());
                        organ.setsS_ManagerId(proactivity_info_pwd.getText().toString());
                        organ.setsS_TelePhone(proactivity_info_name.getText().toString());
                        organ.setsS_Address(proactivity_info_Nname.getText().toString());
                        organ.setsS_Description(proactivity_info_phone.getText().toString());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = OrganHttp.UpdataOrg(organ,user);
                                if (b){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(ProjActivity.this,R.string.Updatasuccess,1*1000);
                                            finish();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(ProjActivity.this,R.string.Updatafailed,1*1000);
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
