package com.zzb.fragment.sc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zzb.bean.Host;
import com.zzb.bean.Strategy;
import com.zzb.http.StrategyHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;
import com.zzb.util.GsonUtil;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class StrategyInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "StrategyInfoActivity";
    private Strategy strategy = new Strategy();
    private TextView stgc_pro, stgc_numb, stgc_name, stgc_shineng, stgc_youxianji,
            stgc_zhouqi, stgc_shengxiaoriqi, stgc_shixiaoriqi, stgc_qishizhou, stgc_jieshuzhou,
            stgc_cankaodian, stgc_chufashijian, stgc_fenzu, stgc_tiaoguangshineng, stgc_tiaoguangzhi,
            stgc_huilu, stgc_beizhu,stgc_huilukaiguan;
    private Host host = new Host();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_info2);
        Intent intent = getIntent();
        strategy = (Strategy) intent.getSerializableExtra("SELECTUSER_STRATEGYCONTREETINFO");
        host = (Host) intent.getSerializableExtra("SELECTUSER_STRATEGYCONTREETINFOHOST");

        //项目
        stgc_pro = (TextView) findViewById(R.id.stgc_pro);
        //编号
        stgc_numb=(TextView) findViewById(R.id.stgc_numb);
        //名称
        stgc_name=(TextView) findViewById(R.id.stgc_name);
        //使能开关
        stgc_shineng=(TextView) findViewById(R.id.stgc_shineng);
        //优先级别
        stgc_youxianji=(TextView) findViewById(R.id.stgc_youxianji);
        //周期
        stgc_zhouqi=(TextView) findViewById(R.id.stgc_zhouqi);
        //生效日期
        stgc_shengxiaoriqi=(TextView) findViewById(R.id.stgc_shengxiaoriqi);
        //失效日期
        stgc_shixiaoriqi=(TextView) findViewById(R.id.stgc_shixiaoriqi);
        //起始周
        stgc_qishizhou=(TextView) findViewById(R.id.stgc_qishizhou);
        //结束周
        stgc_jieshuzhou=(TextView) findViewById(R.id.stgc_jieshuzhou);
        //参考点
        stgc_cankaodian=(TextView) findViewById(R.id.stgc_cankaodian);
        //触发时间
        stgc_chufashijian=(TextView) findViewById(R.id.stgc_chufashijian);
        //分组
        stgc_fenzu=(TextView) findViewById(R.id.stgc_fenzu);
        //调光使能
        stgc_tiaoguangshineng =(TextView) findViewById(R.id.stgc_tiaoguangshineng);
        //调光值
        stgc_tiaoguangzhi=(TextView) findViewById(R.id.stgc_tiaoguangzhi);
        //回路开关
        stgc_huilukaiguan = (TextView) findViewById(R.id.stgc_huilukaiguan);
        //回路
        stgc_huilu=(TextView) findViewById(R.id.stgc_huilu);

        //备注
        stgc_beizhu=(TextView) findViewById(R.id.stgc_beizhu);
        findViewById(R.id.strategycInfo_list_img2).setOnClickListener(this);
        findViewById(R.id.strategycInfo_list_img3).setOnClickListener(this);
        setData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.strategycInfo_list_img2:
                finish();
                break;
            case R.id.strategycInfo_list_img3:
                
                download();
                break;
        }
    }


    private void setData(){
        stgc_pro.setText(strategy.getsSL_Organize_S_Id());
        stgc_numb.setText(strategy.getsS_Number()+"");
        stgc_name.setText(strategy.getsS_FullName());
        //使能开关
        if (strategy.getsS_Enabled() == 1) {
            stgc_shineng.setText(getString(R.string.open));
        } else {
            stgc_shineng.setText(getString(R.string.close));
        }
        //优先级
        switch (strategy.getsS_Priority()) {
            case 1:
                stgc_youxianji.setText(getString(R.string.OneStage));
                break;
            case 2:
                stgc_youxianji.setText(getString(R.string.TwoStage));
                break;
            case 3:
                stgc_youxianji.setText(getString(R.string.ThreeStage));
                break;
            case 4:
                stgc_youxianji.setText(getString(R.string.ThreeStage));
                break;
        }
        switch (strategy.getsS_Cycle()) {
            case 1:
                stgc_zhouqi.setText(getString(R.string.day));
                break;
            case 2:
                stgc_zhouqi.setText(getString(R.string.week));
                break;
            case 3:
                stgc_zhouqi.setText(getString(R.string.month));
                break;
            case 4:
                stgc_zhouqi.setText(getString(R.string.year));
                break;
        }

        switch (strategy.getsS_StartWeek()) {
            case 1:
                stgc_qishizhou.setText(getString(R.string.Monday));
                break;
            case 2:
                stgc_qishizhou.setText(getString(R.string.Tuesday));
                break;
            case 3:
                stgc_qishizhou.setText(getString(R.string.Wednesday));
                break;
            case 4:
                stgc_qishizhou.setText(getString(R.string.Thursday));
                break;
            case 5:
                stgc_qishizhou.setText(getString(R.string.Friday));
                break;
            case 6:
                stgc_qishizhou.setText(getString(R.string.Saturday));
                break;
            case 7:
                stgc_qishizhou.setText(getString(R.string.Sunday));
                break;
        }
        switch (strategy.getsS_EndWeek()) {
            case 1:
                stgc_jieshuzhou.setText(getString(R.string.Monday));
                break;
            case 2:
                stgc_jieshuzhou.setText(getString(R.string.Tuesday));
                break;
            case 3:
                stgc_jieshuzhou.setText(getString(R.string.Wednesday));
                break;
            case 4:
                stgc_jieshuzhou.setText(getString(R.string.Thursday));
                break;
            case 5:
                stgc_jieshuzhou.setText(getString(R.string.Friday));
                break;
            case 6:
                stgc_jieshuzhou.setText(getString(R.string.Saturday));
                break;
            case 7:
                stgc_jieshuzhou.setText(getString(R.string.Sunday));
                break;
        }
        switch (strategy.getsS_Reference()) {
            case 1:
                stgc_cankaodian.setText(getString(R.string.standardTime));
                break;
            case 2:
                stgc_cankaodian.setText(getString(R.string.beforeSunrise));
                break;
            case 3:
                stgc_cankaodian.setText(getString(R.string.AfterSunrise));
                break;
            case 4:
                stgc_cankaodian.setText(getString(R.string.BeforeSunset));
                break;
            case 5:
                stgc_cankaodian.setText(getString(R.string.AfterSunset));
                break;
        }
        switch (strategy.getsS_DimEnabled()) {
            case 1:
                stgc_tiaoguangshineng.setText(getString(R.string.open));
                break;
            case 0:
                stgc_tiaoguangshineng.setText(getString(R.string.close));
                break;
        }
        switch (strategy.getsS_Switch()) {
            case 1:
                stgc_huilukaiguan.setText(getString(R.string.open));
                break;
            case 0:
                stgc_huilukaiguan.setText(getString(R.string.close));
                break;
        }

        // 转换成int类型分组
        int arr = Integer.parseInt(strategy.getsS_GroupMask());
        //策略分组  数据处理
        String S_GroupMask = GsonUtil.setStringNumb(arr);
        stgc_fenzu.setText(S_GroupMask);
        //回路
        int arrh = Integer.parseInt(strategy.getsS_LoopMsk());
        String sS_LoopMsk  = GsonUtil.setStringLamp(arrh);
        stgc_huilu.setText(sS_LoopMsk);
        stgc_shengxiaoriqi.setText(strategy.getsS_StartYear()+"-"+strategy.getsS_StartMonth()+"-"+strategy.getsS_StartDay());
        stgc_shixiaoriqi.setText(strategy.getsS_EndYear()+"-"+strategy.getsS_EndMonth()+"-"+strategy.getsS_EndDay());
        stgc_chufashijian.setText(strategy.getsS_Hour()+":"+strategy.getsS_Minute());
        stgc_tiaoguangzhi.setText(strategy.getsS_DimValue()+"");
        stgc_beizhu.setText(strategy.getsS_Description());
    }


    //下载
    private void download(){
        //注册包
        String pack = host.getsS_RegPackage();
        //策略编号
        String numb = strategy.getsS_Number() + "";
        //使能
        String S_Enabled = strategy.getsS_Enabled() + "";
        //优先级
        String S_Priority = strategy.getsS_Priority() + "";
        //策略周期
        String S_Cycle = strategy.getsS_Cycle() + "";
        //起始年
        String S_StartYear = String.valueOf(strategy.getsS_StartYear()).substring(2);
        Log.e(TAG,"起始年:"+S_StartYear);
        //月
        String S_StartMonth = strategy.getsS_StartMonth() + "";
        //日
        String S_StartDay = strategy.getsS_StartDay() + "";
        //周
        String S_StartWeek = strategy.getsS_StartWeek() + "";
        //结束年
        String S_EndYear = String.valueOf(strategy.getsS_EndYear()).substring(2);
        //月
        String S_EndMonth = strategy.getsS_EndMonth() + "";
        //日
        String S_EndDay = strategy.getsS_EndDay() + "";
        //周
        String S_EndWeek = strategy.getsS_EndWeek() + "";
        //参考时间
        String S_Reference = strategy.getsS_Reference() + "";
        //触发时
        String S_Hour = strategy.getsS_Hour() + "";
        //分
        String S_Minute = strategy.getsS_Minute() + "";
        // 分组
        String a =strategy.getsS_GroupMask();
        // 转换成int类型
        int arr = Integer.parseInt(a);
        //策略分组  数据处理
        String S_GroupMask = GsonUtil.setStringNumb(arr);

        String sS_GroupMask =  GsonUtil.getGroups(S_GroupMask);
        //装换成int(低位)
        int intsS_GroupMask = Integer.parseInt(sS_GroupMask);
        //定义个16进制的255
        int a1 = intsS_GroupMask & 0xff;
        //高位
        int intsS_GroupMask2 = intsS_GroupMask >> 8;
        String xxx = a1+"."+intsS_GroupMask2;

        //调光
        String S_DimEnabled = strategy.getsS_DimEnabled() + "";
        //调光值
        String S_DimValue = strategy.getsS_DimValue() + "";
        //回路开关
        String S_Switch = strategy.getsS_Switch() + "";
        //回路掩码
        String S_LoopMsk = strategy.getsS_LoopMsk() + "";
        //机构
        String org = strategy.getsSL_Organize_S_Id();
        final String value = pack + "," + numb + "," + S_Enabled + "," + S_Priority + "," + S_Cycle + "," + S_StartYear + "." + S_StartMonth + "." + S_StartDay + "." + S_StartWeek + "," +
                S_EndYear + "." + S_EndMonth + "." + S_EndDay + "." + S_EndWeek + "," + S_Reference + "," + S_Hour + "." + S_Minute + "," + xxx + "," + S_DimEnabled + "," +
                S_DimValue + "," + S_Switch + "," + S_LoopMsk + "," + org;
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = StrategyHttp.dowStrategy(value);
                if(b){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.downloadOK,1*1500);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyInfoActivity.this,R.string.downloadNO,1*1500);
                        }
                    });
                }
            }
        }).start();
    }
}
