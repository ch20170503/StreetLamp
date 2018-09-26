package com.zzb.sl.UserBasicsInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.sl.R;
import com.zzb.sl.UserBasicsInfo.OperatorActivity.OperatorListActivity;
import com.zzb.sl.UserBasicsInfo.OrgActivity.OrgActivity;
import com.zzb.sl.UserBasicsInfo.PorjActivity.ProjInfoActivity;
import com.zzb.sl.UserBasicsInfo.PorjActivity.ProjOperatorActivity;
import com.maiml.library.BaseItemLayout;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class UserBasicsItemActivity extends SwipeBackActivity implements View.OnClickListener {
    private BaseItemLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_basics_item);
        findViewById(R.id.user_basics_item_img).setOnClickListener(this);
        layout = (BaseItemLayout) findViewById(R.id.user_basics_item_layout);
        User user = TotalUrl.getUser();
        int userQ = user.getdate().getSL_USER_RANKID();
        if (userQ == 2 || userQ == 1){
            showitem();
        }else {
            showitem2();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_basics_item_img:
                finish();
                break;
        }
    }
    private void showitem(){
        //存放Text显示(基础信息)
        String org = this.getResources().getString(R.string.orginfo);
        String OperatorList = this.getResources().getString(R.string.OperatorList);
        String ProjectInformation = this.getResources().getString(R.string.ProjectInformation);
        final List<String> valueList = new ArrayList<>();
        valueList.add(org);
        valueList.add(OperatorList);
        valueList.add(ProjectInformation);
        //存放图片
        List<Integer> resIdList = new ArrayList<>();
        resIdList.add(R.drawable.org);
        resIdList.add(R.drawable.operatorlist);
        resIdList.add(R.drawable.projectlnformation);
        layout.setValueList(valueList) // 文字 list
                .setResIdList(resIdList) //设置图片
                .create(); //
        layout.setOnBaseItemClick(new BaseItemLayout.OnBaseItemClick() {
            @Override
            public void onItemClick(int position) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(UserBasicsItemActivity.this, OrgActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(UserBasicsItemActivity.this, OperatorListActivity.class);
                        startActivity(intent1);

                        break;
                    case 2:
                        Intent intent2 = new Intent(UserBasicsItemActivity.this, ProjInfoActivity.class);
                        startActivity(intent2);

                        break;
                }
            }
        });
    }


    private void showitem2(){
        //存放Text显示(基础信息)
        String org = this.getResources().getString(R.string.orginfo);
        String ProjectInformation = this.getResources().getString(R.string.ProjectInformation);
        final List<String> valueList = new ArrayList<>();
        valueList.add(org);
        valueList.add(ProjectInformation);
        //存放图片
        List<Integer> resIdList = new ArrayList<>();
        resIdList.add(R.drawable.org);
        resIdList.add(R.drawable.projectlnformation);
        layout.setValueList(valueList) // 文字 list
                .setResIdList(resIdList) //设置图片
                .create(); //
        layout.setOnBaseItemClick(new BaseItemLayout.OnBaseItemClick() {
            @Override
            public void onItemClick(int position) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(UserBasicsItemActivity.this, OrgActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(UserBasicsItemActivity.this, ProjOperatorActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }
}
