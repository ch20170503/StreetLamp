package com.zzb.sl;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.zzb.bean.User;
import com.zzb.fragment.SetFragment;
import com.zzb.fragment.message.MessageFragment;
import com.zzb.fragment.sc.CityFragment;
import com.zzb.mainnavigatetabbar.widget.MainNavigateTabBar;
import com.zzb.sl.TimeController.TimeController;
import com.zzb.util.ActivityUtil;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class HomPage extends SwipeBackActivity {
    private static final String TAG = "HomPage";
    private long mExitTime;

    private MainNavigateTabBar mNavigateTabBar;
    //创建用户
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_page);
        getSwipeBackLayout().setEnableGesture(false);

 /*       getData();*/
        mNavigateTabBar = (MainNavigateTabBar) findViewById(R.id.mainTabBar);
        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);
        mNavigateTabBar.addTab(com.zzb.fragment.HomeFragment.class, new MainNavigateTabBar.TabParam(R.drawable.c, R.drawable.c2, HomPage.this.getResources().getString(R.string.Home)));
        mNavigateTabBar.addTab(CityFragment.class, new MainNavigateTabBar.TabParam(R.drawable.sj, R.drawable.sj2, HomPage.this.getResources().getString(R.string.Strategy)));
        mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, HomPage.this.getResources().getString(R.string.Control)));
        mNavigateTabBar.addTab(MessageFragment.class, new MainNavigateTabBar.TabParam(R.drawable.j, R.drawable.j2, HomPage.this.getResources().getString(R.string.Alert)));
        mNavigateTabBar.addTab(SetFragment.class, new MainNavigateTabBar.TabParam(R.drawable.s, R.drawable.s2, HomPage.this.getResources().getString(R.string.Set)));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 实现再按一次退出
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ActivityUtil.showToasts(this, R.string.PressAgainToExit, 1*1000);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
/*    //获取数据
    private void getData() {
        //获取Intent中传递对对象
        Intent intent = this.getIntent();
    }*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigateTabBar.onSaveInstanceState(outState);
    }
    public void onClickPublish(View v) {
        Intent intent =new Intent(HomPage.this,TimeController.class);
        startActivity(intent);
    }
    //动态权限申请(拍照)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ActivityUtil.doNext(requestCode, grantResults, this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      switch (requestCode){
          case 1:
              super.onActivityResult(requestCode,resultCode,data);
              break;
          case 2:
              super.onActivityResult(requestCode,resultCode,data);
              break;
      }
    }
}
