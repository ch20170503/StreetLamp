package com.zzb.sl.UserBasicsInfo.OrgActivity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zzb.bean.Organ;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.OrganHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OrgActivity extends SwipeBackActivity implements View.OnClickListener {

    private static final String TAG = "OrgActivity";
    private TextView orgname,address,linkman,phone;
    //下拉刷新
    private SwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org);
        findViewById(R.id.org_img2).setOnClickListener(this);
        orgname = (TextView) findViewById(R.id.org_text_name);
        address = (TextView) findViewById(R.id.org_text_Nname);
        linkman = (TextView) findViewById(R.id.org_text_zhH);
        phone = (TextView) findViewById(R.id.org_text_phone);
        srl = (SwipeRefreshLayout) findViewById(R.id.org_srfl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //初始化
                orgname.setText(" ");
                address.setText("");
                linkman.setText(" ");
                phone.setText(" ");
                setData();
                srl.setRefreshing(false);
            }
        });
        setData();
    }

    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.org_img2:
             finish();
         break;
     }
    }
    //加载数据
    private void setData(){
        final User u = TotalUrl.getUser();
        //用户机构
        final String orgUser = u.getdate().getOrganizeId();

        new Thread(new Runnable() {
            @Override
            public void run() {
               List <Organ> organList = OrganHttp.SelectOrg(u);
                if (organList == null){
                  organList = OrganHttp.SelectOrg(u);
                    if (organList == null){
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ActivityUtil.showToasts(OrgActivity.this, R.string.PleaseCheckTheNetwork, 1 * 1000);
                                orgname.setText(" ");
                                address.setText("");
                                linkman.setText(" ");
                                phone.setText(" ");
                            }
                        });
                        return;
                    }

                }
                final List<Organ> o =new ArrayList<>();
                if(organList  != null){
                    for (int i = 0; i < organList.size() ; i++) {
                        if(orgUser.equals(organList.get(i).getsS_Id())){
                            Log.e(TAG,"相等的机构："+organList.get(i));
                            o.add(organList.get(i));
                        }
                    }
                }else{
                    return;
                }

                    if (o.size()>= 1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            orgname.setText(o.get(0).getsS_FullName());
                            address.setText(o.get(0).getsS_Address());
                            linkman.setText(o.get(0).getsS_ManagerId());
                            phone.setText(o.get(0).getsS_TelePhone());
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(OrgActivity.this, R.string.PleaseCheckTheNetwork, 1 * 1000);
                        }
                    });
                }

            }
        }).start();
    }
}
