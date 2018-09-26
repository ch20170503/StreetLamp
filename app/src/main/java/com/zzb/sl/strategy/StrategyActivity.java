package com.zzb.sl.strategy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bigkoo.pickerview.TimePickerView;
import com.zzb.bean.Organ;
import com.zzb.bean.Strategy;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.TypeBean;
import com.zzb.bean.User;
import com.zzb.http.OrganHttp;
import com.zzb.http.StrategyHttp;
import com.zzb.sl.R;
import com.zzb.sl.deviceManagement.LampManage.Util;
import com.zzb.util.ActivityUtil;
import com.zzb.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class StrategyActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "StrategyActivity";
    private Spinner strategy_info_pro;
    private EditText strategy_info_S_Number,
            strategy_info_S_FullName,
            strategy_info_S_Enabled,
            strategy_info_S_Priority,
            strategy_info_S_Cycle,
            strategy_info_S_StartYear,
            strategy_info_S_EndYear,
            strategy_info_S_StartWeek,
            strategy_info_S_EndWeek,
            strategy_info_S_Reference,
            strategy_info_S_Hour,
            strategy_info_S_GroupMask,
            strategy_info_S_DimEnabled,
            strategy_info_S_DimValue,
            strategy_info_S_Switch,
            strategy_info_S_Description,
            strategy_info_S_LoopMsk;
    private static final int MESSAGETYPE_01 = 0x0001;
    private ProgressDialog progressDialog = null;

    private Strategy strategy = new Strategy();

    private List<String> data_list;
    private List<String> data_list2;
    private ArrayAdapter<String> arr_adapter;
    //使能开关
    private ArrayList<TypeBean> S_EnabledList = new ArrayList<>();
    //优先级
    private ArrayList<TypeBean> S_PriorityList = new ArrayList<>();
    //周期
    private ArrayList<TypeBean> S_CycleList = new ArrayList<>();
    //起始周
    private ArrayList<TypeBean> S_StartWeekList = new ArrayList<>();
    //结束周
    private ArrayList<TypeBean> S_EndWeekList = new ArrayList<>();
    //参考点
    private ArrayList<TypeBean> S_ReferenceList = new ArrayList<>();
    //分组
    private ArrayList<TypeBean> S_GroupMaskList = new ArrayList<>();
    //调光使能
    private ArrayList<TypeBean> S_DimEnabledList = new ArrayList<>();
    //回路开关
    private ArrayList<TypeBean> S_SwitchList = new ArrayList<>();
    //回路值
    private ArrayList<TypeBean> S_LoopMskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy);
        //获取控件id
        strategy_info_pro = (Spinner) findViewById(R.id.strategy_info_pro);
        strategy_info_S_Number = (EditText) findViewById(R.id.strategy_info_S_Number);
        strategy_info_S_FullName = (EditText) findViewById(R.id.strategy_info_S_FullName);
        strategy_info_S_Enabled = (EditText) findViewById(R.id.strategy_info_S_Enabled);
        strategy_info_S_Priority = (EditText) findViewById(R.id.strategy_info_S_Priority);
        strategy_info_S_Cycle = (EditText) findViewById(R.id.strategy_info_S_Cycle);
        strategy_info_S_StartYear = (EditText) findViewById(R.id.strategy_info_S_StartYear);
        strategy_info_S_EndYear = (EditText) findViewById(R.id.strategy_info_S_EndYear);
        strategy_info_S_StartWeek = (EditText) findViewById(R.id.strategy_info_S_StartWeek);
        strategy_info_S_EndWeek = (EditText) findViewById(R.id.strategy_info_S_EndWeek);
        strategy_info_S_Reference = (EditText) findViewById(R.id.strategy_info_S_Reference);
        strategy_info_S_Hour = (EditText) findViewById(R.id.strategy_info_S_Hour);
        strategy_info_S_GroupMask = (EditText) findViewById(R.id.strategy_info_S_GroupMask);
        strategy_info_S_DimEnabled = (EditText) findViewById(R.id.strategy_info_S_DimEnabled);
        strategy_info_S_DimValue = (EditText) findViewById(R.id.strategy_info_S_DimValue);
        strategy_info_S_Switch = (EditText) findViewById(R.id.strategy_info_S_Switch);
        strategy_info_S_Description = (EditText) findViewById(R.id.strategy_info_S_Description);
        strategy_info_S_LoopMsk = (EditText) findViewById(R.id.strategy_info_S_LoopMsk);
        //使能开关
        strategy_info_S_Enabled.setOnClickListener(this);
        //优先级
        strategy_info_S_Priority.setOnClickListener(this);
        //周期
        strategy_info_S_Cycle.setOnClickListener(this);
        //生效日期
        strategy_info_S_StartYear.setOnClickListener(this);
        //失效日期
        strategy_info_S_EndYear.setOnClickListener(this);
        //起始周
        strategy_info_S_StartWeek.setOnClickListener(this);
        //结束周
        strategy_info_S_EndWeek.setOnClickListener(this);
        //参考点
        strategy_info_S_Reference.setOnClickListener(this);
        //触发时间
        strategy_info_S_Hour.setOnClickListener(this);
        //分组
        strategy_info_S_GroupMask.setOnClickListener(this);
        //调光使能
        strategy_info_S_DimEnabled.setOnClickListener(this);
        //回路开关
        strategy_info_S_Switch.setOnClickListener(this);
        //回路值
        strategy_info_S_LoopMsk.setOnClickListener(this);
        findViewById(R.id.strategy_info_img2).setOnClickListener(this);
        findViewById(R.id.strategy_info_img3).setOnClickListener(this);
        getData();
    }


    //获取数据源
    private void getData() {
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        final User user = TotalUrl.getUser();
        final String userOrg = user.getdate().getOrganizeId();
        Intent intent = getIntent();
        strategy = (Strategy) intent.getSerializableExtra("SELECTUSER_STRATEGY");
        final String idname = strategy.getsSL_Organize_S_Id();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                String name = "";
                //数据
                data_list = new ArrayList<>();
                data_list2 = new ArrayList<>();
                for (int i = 0; i < organList.size(); i++) {
                    if (idname.equals(organList.get(i).getsS_Id())) {
                        name = organList.get(i).getsS_FullName();
                    }
                }

                for (int i = 0; i < organList.size(); i++) {
                    if (organList.get(i).getsS_ParentId().equals(userOrg)) {
                        data_list.add(organList.get(i).getsS_FullName());
                        data_list2.add(organList.get(i).getsS_Id());
                    }
                }
                final String finalName = name;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //适配器
                        arr_adapter = new ArrayAdapter<>(StrategyActivity.this, android.R.layout.simple_spinner_item, data_list);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        strategy_info_pro.setAdapter(arr_adapter);
                        for (int i = 0; i < data_list.size(); i++) {
                            if (data_list.get(i).equals(finalName)) {
                                strategy_info_pro.setSelection(i);
                            }
                        }
                        // 分组
                        String a = strategy.getsS_GroupMask();
                        // 转换成int类型
                        int arr = Integer.parseInt(a);
                        // 数据处理
                        strategy.setsS_GroupMask(GsonUtil.setStringNumb(arr));

                        // 回路
                        String b = strategy.getsS_LoopMsk();
                        // 转换成int类型
                        int arr1 = Integer.parseInt(b);
                        // 数据处理
                        strategy.setsS_LoopMsk(GsonUtil.setStringLamp(arr1));


                        strategy_info_S_Number.setText(strategy.getsS_Number() + "");
                        strategy_info_S_FullName.setText(strategy.getsS_FullName());
                        //使能开关
                        if (strategy.getsS_Enabled() == 1) {
                            strategy_info_S_Enabled.setText(getString(R.string.open));
                        } else {
                            strategy_info_S_Enabled.setText(getString(R.string.close));
                        }

                        switch (strategy.getsS_Priority()) {
                            case 1:
                                strategy_info_S_Priority.setText(getString(R.string.OneStage));
                                break;
                            case 2:
                                strategy_info_S_Priority.setText(getString(R.string.TwoStage));
                                break;
                            case 3:
                                strategy_info_S_Priority.setText(getString(R.string.ThreeStage));
                                break;
                            case 4:
                                strategy_info_S_Priority.setText(getString(R.string.ThreeStage));
                                break;
                        }
                        switch (strategy.getsS_Cycle()) {
                            case 1:
                                strategy_info_S_Cycle.setText(getString(R.string.day));
                                break;
                            case 2:
                                strategy_info_S_Cycle.setText(getString(R.string.week));
                                break;
                            case 3:
                                strategy_info_S_Cycle.setText(getString(R.string.month));
                                break;
                            case 4:
                                strategy_info_S_Cycle.setText(getString(R.string.year));
                                break;
                        }
                        strategy_info_S_StartYear.setText(strategy.getsS_StartYear() + "-" + strategy.getsS_StartMonth() + "-" + strategy.getsS_StartDay());
                        strategy_info_S_EndYear.setText(strategy.getsS_EndYear() + "-" + strategy.getsS_EndMonth() + "-" + strategy.getsS_EndDay());

                        switch (strategy.getsS_StartWeek()) {
                            case 1:
                                strategy_info_S_StartWeek.setText(getString(R.string.Monday));
                                break;
                            case 2:
                                strategy_info_S_StartWeek.setText(getString(R.string.Tuesday));
                                break;
                            case 3:
                                strategy_info_S_StartWeek.setText(getString(R.string.Wednesday));
                                break;
                            case 4:
                                strategy_info_S_StartWeek.setText(getString(R.string.Thursday));
                                break;
                            case 5:
                                strategy_info_S_StartWeek.setText(getString(R.string.Friday));
                                break;
                            case 6:
                                strategy_info_S_StartWeek.setText(getString(R.string.Saturday));
                                break;
                            case 7:
                                strategy_info_S_StartWeek.setText(getString(R.string.Sunday));
                                break;
                        }
                        switch (strategy.getsS_EndWeek()) {
                            case 1:
                                strategy_info_S_EndWeek.setText(getString(R.string.Monday));
                                break;
                            case 2:
                                strategy_info_S_EndWeek.setText(getString(R.string.Tuesday));
                                break;
                            case 3:
                                strategy_info_S_EndWeek.setText(getString(R.string.Wednesday));
                                break;
                            case 4:
                                strategy_info_S_EndWeek.setText(getString(R.string.Thursday));
                                break;
                            case 5:
                                strategy_info_S_EndWeek.setText(getString(R.string.Friday));
                                break;
                            case 6:
                                strategy_info_S_EndWeek.setText(getString(R.string.Saturday));
                                break;
                            case 7:
                                strategy_info_S_EndWeek.setText(getString(R.string.Sunday));
                                break;
                        }
                        switch (strategy.getsS_Reference()) {
                            case 1:
                                strategy_info_S_Reference.setText(getString(R.string.standardTime));
                                break;
                            case 2:
                                strategy_info_S_Reference.setText(getString(R.string.beforeSunrise));
                                break;
                            case 3:
                                strategy_info_S_Reference.setText(getString(R.string.AfterSunrise));
                                break;
                            case 4:
                                strategy_info_S_Reference.setText(getString(R.string.BeforeSunset));
                                break;
                            case 5:
                                strategy_info_S_Reference.setText(getString(R.string.AfterSunset));
                                break;
                        }

                        strategy_info_S_Hour.setText(strategy.getsS_Hour() + ":" + strategy.getsS_Minute());

                        strategy_info_S_GroupMask.setText(strategy.getsS_GroupMask());
                        switch (strategy.getsS_DimEnabled()) {
                            case 1:
                                strategy_info_S_DimEnabled.setText(getString(R.string.open));
                                break;
                            case 0:
                                strategy_info_S_DimEnabled.setText(getString(R.string.close));
                                break;
                        }
                        strategy_info_S_DimValue.setText(strategy.getsS_DimValue() + "");
                        switch (strategy.getsS_Switch()) {
                            case 1:
                                strategy_info_S_Switch.setText(getString(R.string.open));
                                break;
                            case 0:
                                strategy_info_S_Switch.setText(getString(R.string.close));
                                break;
                        }

                        strategy_info_S_LoopMsk.setText(strategy.getsS_LoopMsk());
                        strategy_info_S_Description.setText(strategy.getsS_Description() + "");


                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler.sendMessage(msg_listData);
                return;
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
    String proj = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //使能开关
            case R.id.strategy_info_S_Enabled:
                S_EnabledList.clear();
                S_EnabledList.add(new TypeBean(1, getString(R.string.open)));
                S_EnabledList.add(new TypeBean(0, getString(R.string.close)));
                Util.alertBottomWheelOption(StrategyActivity.this, S_EnabledList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_info_S_Enabled.setText("");
                        strategy_info_S_Enabled.setText(S_EnabledList.get(postion).getName());
                    }
                });
                break;
            //优先级
            case R.id.strategy_info_S_Priority:
                S_PriorityList.clear();
                S_PriorityList.add(new TypeBean(1, getString(R.string.OneStage)));
                S_PriorityList.add(new TypeBean(2, getString(R.string.TwoStage)));
                S_PriorityList.add(new TypeBean(3, getString(R.string.ThreeStage)));
                S_PriorityList.add(new TypeBean(4, getString(R.string.FourStage)));
                Util.alertBottomWheelOption(StrategyActivity.this, S_PriorityList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_info_S_Priority.setText("");
                        strategy_info_S_Priority.setText(S_PriorityList.get(postion).getName());
                    }
                });
                break;
            //周期
            case R.id.strategy_info_S_Cycle:
                S_CycleList.clear();
                S_CycleList.add(new TypeBean(1, getString(R.string.day)));
                S_CycleList.add(new TypeBean(2, getString(R.string.week)));
                S_CycleList.add(new TypeBean(3, getString(R.string.Monday)));
                S_CycleList.add(new TypeBean(4,  getString(R.string.year)));
                Util.alertBottomWheelOption(StrategyActivity.this, S_CycleList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_info_S_Cycle.setText("");
                        strategy_info_S_Cycle.setText(S_CycleList.get(postion).getName());
                    }
                });
                break;
            //生效日期
            case R.id.strategy_info_S_StartYear:
                String format = "";
                TimePickerView.Type type;
                type = TimePickerView.Type.YEAR_MONTH_DAY;
                format = "yyyy-MM-dd";
                Util.alertTimerPicker(this, type, format, new Util.TimerPickerCallBack() {
                    @Override
                    public void onTimeSelect(String date) {
                        // Toast.makeText(TestActivity.this, date, Toast.LENGTH_SHORT).show();
                        strategy_info_S_StartYear.setText("");
                        strategy_info_S_StartYear.setText(date);
                    }
                });
                break;
            //失效日期
            case R.id.strategy_info_S_EndYear:
                String format1 = "";
                TimePickerView.Type type1;
                type1 = TimePickerView.Type.YEAR_MONTH_DAY;
                format1 = "yyyy-MM-dd";
                Util.alertTimerPicker(this, type1, format1, new Util.TimerPickerCallBack() {
                    @Override
                    public void onTimeSelect(String date) {
                        strategy_info_S_EndYear.setText("");
                        strategy_info_S_EndYear.setText(date);
                    }
                });
                break;
            //起始周
            case R.id.strategy_info_S_StartWeek:
                S_StartWeekList.clear();
                S_StartWeekList.add(new TypeBean(1, getString(R.string.Monday)));
                S_StartWeekList.add(new TypeBean(2, getString(R.string.Tuesday)));
                S_StartWeekList.add(new TypeBean(3, getString(R.string.Wednesday)));
                S_StartWeekList.add(new TypeBean(4, getString(R.string.Thursday)));
                S_StartWeekList.add(new TypeBean(5, getString(R.string.Friday)));
                S_StartWeekList.add(new TypeBean(6, getString(R.string.Saturday)));
                S_StartWeekList.add(new TypeBean(7, getString(R.string.Sunday)));
                Util.alertBottomWheelOption(StrategyActivity.this, S_StartWeekList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_info_S_StartWeek.setText("");
                        strategy_info_S_StartWeek.setText(S_StartWeekList.get(postion).getName());
                    }
                });
                break;
            //结束周
            case R.id.strategy_info_S_EndWeek:
                S_EndWeekList.clear();
                S_EndWeekList.add(new TypeBean(1, getString(R.string.Monday)));
                S_EndWeekList.add(new TypeBean(2, getString(R.string.Tuesday)));
                S_EndWeekList.add(new TypeBean(3, getString(R.string.Wednesday)));
                S_EndWeekList.add(new TypeBean(4, getString(R.string.Thursday)));
                S_EndWeekList.add(new TypeBean(5, getString(R.string.Friday)));
                S_EndWeekList.add(new TypeBean(6, getString(R.string.Saturday)));
                S_EndWeekList.add(new TypeBean(7, getString(R.string.Sunday)));
                Util.alertBottomWheelOption(StrategyActivity.this, S_EndWeekList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_info_S_EndWeek.setText("");
                        strategy_info_S_EndWeek.setText(S_EndWeekList.get(postion).getName());
                    }
                });
                break;
            //参考点
            case R.id.strategy_info_S_Reference:
                S_ReferenceList.clear();
                S_ReferenceList.add(new TypeBean(1, getString(R.string.standardTime)));
                S_ReferenceList.add(new TypeBean(2, getString(R.string.beforeSunrise)));
                S_ReferenceList.add(new TypeBean(3, getString(R.string.AfterSunrise)));
                S_ReferenceList.add(new TypeBean(4, getString(R.string.BeforeSunset)));
                S_ReferenceList.add(new TypeBean(5, getString(R.string.AfterSunset)));
                Util.alertBottomWheelOption(StrategyActivity.this, S_ReferenceList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_info_S_Reference.setText("");
                        strategy_info_S_Reference.setText(S_ReferenceList.get(postion).getName());
                    }
                });
                break;
            //触发时间
            case R.id.strategy_info_S_Hour:
                String format2 = "";
                TimePickerView.Type type2;
                type2 = TimePickerView.Type.HOURS_MINS;
                format2 = "HH:mm";
                Util.alertTimerPicker(this, type2, format2, new Util.TimerPickerCallBack() {
                    @Override
                    public void onTimeSelect(String date) {
                        strategy_info_S_Hour.setText("");
                        strategy_info_S_Hour.setText(date);
                    }
                });
                break;
            //分组
            case R.id.strategy_info_S_GroupMask:
                S_GroupMaskList.clear();
                for (int i = 0; i < 16; i++) {
                    S_GroupMaskList.add(new TypeBean((i + 1), (getString(R.string.Group) + (i + 1))));
                    Log.e(TAG, "分组id:" + S_GroupMaskList.get(i).getId() + "---分组名称:" + S_GroupMaskList.get(i).getName());
                }
                showMutilAlertDialog(S_GroupMaskList, "strategy_info_S_GroupMask");

                break;
            //调光使能
            case R.id.strategy_info_S_DimEnabled:
                S_DimEnabledList.clear();
                S_DimEnabledList.add(new TypeBean(1, getString(R.string.open)));
                S_DimEnabledList.add(new TypeBean(0, getString(R.string.close)));
                Util.alertBottomWheelOption(StrategyActivity.this, S_DimEnabledList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_info_S_DimEnabled.setText("");
                        strategy_info_S_DimEnabled.setText(S_DimEnabledList.get(postion).getName());
                    }
                });
                break;
            //回路开关
            case R.id.strategy_info_S_Switch:
                S_SwitchList.clear();
                S_SwitchList.add(new TypeBean(1, getString(R.string.open)));
                S_SwitchList.add(new TypeBean(0, getString(R.string.close)));
                Util.alertBottomWheelOption(StrategyActivity.this, S_SwitchList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_info_S_Switch.setText("");
                        strategy_info_S_Switch.setText(S_SwitchList.get(postion).getName());
                    }
                });
                break;
            //回路值
            case R.id.strategy_info_S_LoopMsk:
                S_LoopMskList.clear();
                for (int i = 0; i < 8; i++) {
                    S_LoopMskList.add(new TypeBean((i + 1), (getString(R.string.Loop) + (i + 1))));
                    Log.e(TAG, "回路id:" + S_LoopMskList.get(i).getId() + "---回路名称:" + S_LoopMskList.get(i).getName());
                }
                showMutilAlertDialog(S_LoopMskList, "strategy_info_S_LoopMsk");
                break;

            case R.id.strategy_info_img2:
                finish();
                break;
            case R.id.strategy_info_img3:
                //开始
                String sta = strategy_info_S_StartYear.getText().toString().replace("-","");
                //结束
                String end =strategy_info_S_EndYear.getText().toString().replace("-","");
                int staint = Integer.parseInt(sta);
                int endint = Integer.parseInt(end);
                if ( Integer.parseInt(strategy_info_S_Number.getText().toString()) >30){
                    ActivityUtil.showToasts(StrategyActivity.this, getString(R.string.PleaseEnterTheCorrectNumber), 1 * 1000);
                    return;
                }
                if (Integer.parseInt(strategy_info_S_DimValue.getText().toString()) >100){
                    ActivityUtil.showToasts(StrategyActivity.this, getString(R.string.PleaseEnterTheCorrectBrightness), 1 * 1000);
                    return;
                }
                //修改数据
                if (strategy_info_S_Number.getText().length() < 1){
                    ActivityUtil.showToasts(StrategyActivity.this,getString(R.string.PleaseEnterTheCorrectNumber),1*1000);
                }else if (strategy_info_S_FullName.getText().length() <1){
                    ActivityUtil.showToasts(StrategyActivity.this,getString(R.string.PleaseEnterTheCorrectName),1*1000);
                }else if (strategy_info_S_DimValue.getText().length() <1){
                    ActivityUtil.showToasts(StrategyActivity.this,getString(R.string.PleaseEnterTheCorrectBrightness),1*1000);
                }else if (strategy_info_S_Description.getText().length() <1){
                    ActivityUtil.showToasts(StrategyActivity.this,getString(R.string.PleaseInputMemoInformation),1*1000);
                }else if (staint >=endint){
                    ActivityUtil.showToasts(StrategyActivity.this,getString(R.string.PleaseSelectTheCorrectDate),1*1000);
                }else{
                    //转换数据
                    convertData();
                    Log.e(TAG,"转换后的数据:"+strategy);
                    Log.e(TAG,"转换后的数据:"+strategy.getsSL_Organize_S_Id());
                    updata(strategy);

                }
                break;

        }
    }


    // 多选提示框
    private AlertDialog alertDialog3;
    private String a = "";
    private String b = "";

    public void showMutilAlertDialog(List<TypeBean> S_GroupMaskList, final String id) {
        a = "";
        b = "";
        for (int i = 0; i < S_GroupMaskList.size(); i++) {
            a += S_GroupMaskList.get(i).getName() + ",";
            b += S_GroupMaskList.get(i).getId() + ",";
        }
        String[] proj = a.split(",");
        final String[] proj2 = b.split(",");
        final boolean[] selected = new boolean[S_GroupMaskList.size()];
        final boolean[] selected2 = new boolean[S_GroupMaskList.size()];
        // 创建一个AlertDialog建造者
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StrategyActivity.this);
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
                } else {
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
                if (proj2.length < 1) {
                    ActivityUtil.showToasts(StrategyActivity.this, getString(R.string.PleaseSelectTheCorrectInformation), 1 * 1000);
                    return;
                }
                for (int i = 0; i < selected2.length; i++) {
                    if (selected2[i] == true) {
                        selectedStr += proj2[i] + ",";
                    }
                }
                if (selectedStr == "," || selectedStr == "") {
                    ActivityUtil.showToasts(StrategyActivity.this, getString(R.string.PleaseSelectTheCorrectInformation), 1 * 1000);

                    return;
                }
                Log.e(TAG, "点击确定：" + selectedStr);
                if (id.equals("strategy_info_S_GroupMask")) {
                    strategy_info_S_GroupMask.setText(selectedStr);
                } else {
                    strategy_info_S_LoopMsk.setText(selectedStr);
                }
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



    private void convertData(){
        //获取数据并且转换数据
        strategy_info_pro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                proj = data_list2.get(i);
                Log.e(TAG, "data_list:" + proj);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        String pro = proj;
        String sS_Number = strategy_info_S_Number.getText().toString();
        String sS_FullName = strategy_info_S_FullName.getText().toString();
        String sS_Enabled = strategy_info_S_Enabled.getText().toString();
        String sS_Priority = strategy_info_S_Priority.getText().toString();
        String sS_Cycle = strategy_info_S_Cycle.getText().toString();
        String sS_StartYear = strategy_info_S_StartYear.getText().toString();
        String sS_EndYear = strategy_info_S_EndYear.getText().toString();
        String sS_StartWeek = strategy_info_S_StartWeek.getText().toString();
        String sS_EndWeek = strategy_info_S_EndWeek.getText().toString();
        String sS_Reference = strategy_info_S_Reference.getText().toString();
        String sS_Hour = strategy_info_S_Hour.getText().toString();
        String sS_GroupMask = strategy_info_S_GroupMask.getText().toString();
        String sS_DimEnabled = strategy_info_S_DimEnabled.getText().toString();
        String sS_DimValue = strategy_info_S_DimValue.getText().toString();
        String sS_Switch = strategy_info_S_Switch.getText().toString();
        String sS_Description = strategy_info_S_Description.getText().toString();
        String sS_LoopMsk = strategy_info_S_LoopMsk.getText().toString();
        if (!sS_GroupMask.equals("")){
            Log.e(TAG,"分组:"+sS_GroupMask);
            //分组
            strategy.setsS_GroupMask(GsonUtil.getGroup(sS_GroupMask));
        }
        if (!sS_LoopMsk.equals("")){
            //回路
            strategy.setsS_LoopMsk(GsonUtil.getLamp(sS_LoopMsk));
        }

        //策略编号
        strategy.setsS_FullName(sS_FullName);
        // 使能开关 1 开 0关
        if (sS_Enabled.equals(getString(R.string.open))){
            strategy.setsS_Enabled(1);
        }else{
            strategy.setsS_Enabled(0);
        }

        // 策略优先级
        if (sS_Priority.equals(getString(R.string.OneStage))){
            strategy.setsS_Priority(1);
        }else if(sS_Priority.equals(getString(R.string.TwoStage))){
            strategy.setsS_Priority(2);
        }else if (sS_Priority.equals(getString(R.string.ThreeStage))){
            strategy.setsS_Priority(3);
        }else if (sS_Priority.equals(getString(R.string.FourStage))){
            strategy.setsS_Priority(4);
        }


        // 策略实行周期
        if (sS_Cycle.equals(getString(R.string.day))){
            strategy.setsS_Cycle(1);
        }else if (sS_Cycle.equals(getString(R.string.week))){
            strategy.setsS_Cycle(2);
        }else if (sS_Cycle.equals(getString(R.string.month))){
            strategy.setsS_Cycle(3);
        }else if (sS_Cycle.equals(getString(R.string.year))){
            strategy.setsS_Cycle(4);
        }
        String[] stringArrQ = GsonUtil.getdates(sS_StartYear);
        int QYear = Integer.parseInt(stringArrQ[0]);
        int QMonth = Integer.parseInt(stringArrQ[1]);
        int QDay = Integer.parseInt(stringArrQ[2]);
        strategy.setsS_StartYear(QYear);
        strategy.setsS_StartMonth(QMonth);
        strategy.setsS_StartDay(QDay);
        // 策略起始周
        if (sS_StartWeek.equals(getString(R.string.Monday))){
            strategy.setsS_StartWeek(1);
        }else if(sS_StartWeek.equals(getString(R.string.Tuesday))){
            strategy.setsS_StartWeek(2);
        }else if(sS_StartWeek.equals(getString(R.string.Wednesday))){
            strategy.setsS_StartWeek(3);
        }else if(sS_StartWeek.equals(getString(R.string.Thursday))){
            strategy.setsS_StartWeek(4);
        }else if(sS_StartWeek.equals(getString(R.string.Friday))){
            strategy.setsS_StartWeek(5);
        }else if(sS_StartWeek.equals(getString(R.string.Saturday))){
            strategy.setsS_StartWeek(6);
        }else if(sS_StartWeek.equals(getString(R.string.Sunday))){
            strategy.setsS_StartWeek(7);
        }

        //策略结束年
        String[] stringArrJ = GsonUtil.getdates(sS_EndYear);
        int JYear = Integer.parseInt(stringArrJ[0]);
        int JMonth = Integer.parseInt(stringArrJ[1]);
        int JDay = Integer.parseInt(stringArrJ[2]);
        strategy.setsS_EndYear(JYear);
        strategy.setsS_EndMonth(JMonth);
        strategy.setsS_EndDay(JDay);
        // 策略结束周
        if (sS_EndWeek.equals(getString(R.string.Monday))){
            strategy.setsS_EndWeek(1);
        }else if(sS_EndWeek.equals(getString(R.string.Tuesday))){
            strategy.setsS_EndWeek(2);
        }else if(sS_EndWeek.equals(getString(R.string.Wednesday))){
            strategy.setsS_EndWeek(3);
        }else if(sS_EndWeek.equals(getString(R.string.Thursday))){
            strategy.setsS_EndWeek(4);
        }else if(sS_EndWeek.equals(getString(R.string.Friday))){
            strategy.setsS_EndWeek(5);
        }else if(sS_EndWeek.equals(getString(R.string.Saturday))){
            strategy.setsS_EndWeek(6);
        }else if(sS_EndWeek.equals(getString(R.string.Sunday))){
            strategy.setsS_EndWeek(7);
        }

        // 策略时间参考点
        if (sS_Reference.equals(getString(R.string.standardTime))){
            strategy.setsS_Reference(1);

        }else if (sS_Reference.equals(getString(R.string.beforeSunrise))){
            strategy.setsS_Reference(1);

        }else if (sS_Reference.equals(getString(R.string.AfterSunrise))){
            strategy.setsS_Reference(3);

        }else if (sS_Reference.equals(getString(R.string.BeforeSunset))){
            strategy.setsS_Reference(4);

        }else if (sS_Reference.equals(getString(R.string.AfterSunset))){
            strategy.setsS_Reference(5);

        }

        // 策略触发时
        String[] stringArrTime = GsonUtil.getTime(sS_Hour);
        int hour = Integer.parseInt(stringArrTime[0]);
        int minute = Integer.parseInt(stringArrTime[1]);
        strategy.setsS_Hour(hour);
        strategy.setsS_Minute(minute);
        // 调光使能开关
        if (sS_DimEnabled.equals(getString(R.string.open))){
            strategy.setsS_DimValue(1);
        }else{
            strategy.setsS_DimValue(0);
        }
        // 回路开关
        if (sS_Switch.equals(getString(R.string.open))){
            strategy.setsS_Switch(1);
        }else{
            strategy.setsS_Switch(0);
        }
        // 策略信息备注
        strategy.setsS_Description(sS_Description);
        if(!pro.equals("")){
            // 机构或机构主键的ID
            strategy.setsSL_Organize_S_Id(pro);
        }
        // 策略编号
        strategy.setsS_Number(Integer.parseInt(sS_Number));
        // 调光值
        strategy.setsS_DimValue(Integer.parseInt(sS_DimValue));
    }


    private void updata(final Strategy st){
        final User user = TotalUrl.getUser();
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.revise))
                .setMessage(getResources().getString(R.string.Modify))
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = StrategyHttp.UpdataStrategy(st, user);
                                if (b) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(StrategyActivity.this, R.string.Updatasuccess, 1 * 1000);
                                            finish();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(StrategyActivity.this, R.string.Updatafailed, 1 * 1000);
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
