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
import com.zzb.bean.SelectUser;
import com.zzb.bean.Strategy;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.TypeBean;
import com.zzb.bean.User;
import com.zzb.http.OrganHttp;
import com.zzb.http.StrategyHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.sl.deviceManagement.LampManage.Util;
import com.zzb.util.ActivityUtil;
import com.zzb.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AddStrategyActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "AddAddStrategyActivity";
    private Spinner strategy_add_pro;
    private EditText strategy_add_S_Number,
            strategy_add_S_FullName,
            strategy_add_S_Enabled,
            strategy_add_S_Priority,
            strategy_add_S_Cycle,
            strategy_add_S_StartYear,
            strategy_add_S_EndYear,
            strategy_add_S_StartWeek,
            strategy_add_S_EndWeek,
            strategy_add_S_Reference,
            strategy_add_S_Hour,
            strategy_add_S_GroupMask,
            strategy_add_S_DimEnabled,
            strategy_add_S_DimValue,
            strategy_add_S_Switch,
            strategy_add_S_Description,
            strategy_add_S_LoopMsk;
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

    private List<Organ> pros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy);
        //获取控件id
        strategy_add_pro = (Spinner) findViewById(R.id.strategy_add_pro);
        strategy_add_S_Number = (EditText) findViewById(R.id.strategy_add_S_Number);
        strategy_add_S_FullName = (EditText) findViewById(R.id.strategy_add_S_FullName);
        strategy_add_S_Enabled = (EditText) findViewById(R.id.strategy_add_S_Enabled);
        strategy_add_S_Priority = (EditText) findViewById(R.id.strategy_add_S_Priority);
        strategy_add_S_Cycle = (EditText) findViewById(R.id.strategy_add_S_Cycle);
        strategy_add_S_StartYear = (EditText) findViewById(R.id.strategy_add_S_StartYear);
        strategy_add_S_EndYear = (EditText) findViewById(R.id.strategy_add_S_EndYear);
        strategy_add_S_StartWeek = (EditText) findViewById(R.id.strategy_add_S_StartWeek);
        strategy_add_S_EndWeek = (EditText) findViewById(R.id.strategy_add_S_EndWeek);
        strategy_add_S_Reference = (EditText) findViewById(R.id.strategy_add_S_Reference);
        strategy_add_S_Hour = (EditText) findViewById(R.id.strategy_add_S_Hour);
        strategy_add_S_GroupMask = (EditText) findViewById(R.id.strategy_add_S_GroupMask);
        strategy_add_S_DimEnabled = (EditText) findViewById(R.id.strategy_add_S_DimEnabled);
        strategy_add_S_DimValue = (EditText) findViewById(R.id.strategy_add_S_DimValue);
        strategy_add_S_Switch = (EditText) findViewById(R.id.strategy_add_S_Switch);
        strategy_add_S_Description = (EditText) findViewById(R.id.strategy_add_S_Description);
        strategy_add_S_LoopMsk = (EditText) findViewById(R.id.strategy_add_S_LoopMsk);
        //使能开关
        strategy_add_S_Enabled.setOnClickListener(this);
        //优先级
        strategy_add_S_Priority.setOnClickListener(this);
        //周期
        strategy_add_S_Cycle.setOnClickListener(this);
        //生效日期
        strategy_add_S_StartYear.setOnClickListener(this);
        //失效日期
        strategy_add_S_EndYear.setOnClickListener(this);
        //起始周
        strategy_add_S_StartWeek.setOnClickListener(this);
        //结束周
        strategy_add_S_EndWeek.setOnClickListener(this);
        //参考点
        strategy_add_S_Reference.setOnClickListener(this);
        //触发时间
        strategy_add_S_Hour.setOnClickListener(this);
        //分组
        strategy_add_S_GroupMask.setOnClickListener(this);
        //调光使能
        strategy_add_S_DimEnabled.setOnClickListener(this);
        //回路开关
        strategy_add_S_Switch.setOnClickListener(this);
        //回路值
        strategy_add_S_LoopMsk.setOnClickListener(this);
        findViewById(R.id.strategy_add_img2).setOnClickListener(this);
        findViewById(R.id.strategy_add_img3).setOnClickListener(this);

        User user = TotalUrl.getUser();
        int userQ = user.getdate().getSL_USER_RANKID();
        if (userQ == 2) {
            getData();
        } else {
            getData2();
        }
    }


    //获取数据源
    private void getData() {
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        final User user = TotalUrl.getUser();
        final String userOrg = user.getdate().getOrganizeId();
        Intent intent = getIntent();
        final String idname = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AddStrategyActivity.this, R.string.Currentlynoinformation, 1 * 1000);
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
                    if (idname.equals(organList.get(i).getsS_ParentId())) {
                        name = organList.get(i).getsS_FullName();
                    }
                }

                for (int i = 0; i < organList.size(); i++) {
                    if (organList.get(i).getsS_ParentId().equals(userOrg)) {
                        data_list.add(organList.get(i).getsS_FullName());
                        data_list2.add(organList.get(i).getsS_Id());
                    }
                }
                if (data_list.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AddStrategyActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    finish();
                    return;
                }
                final String finalName = name;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //适配器
                        arr_adapter = new ArrayAdapter<>(AddStrategyActivity.this, android.R.layout.simple_spinner_item, data_list);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        strategy_add_pro.setAdapter(arr_adapter);
                        for (int i = 0; i < data_list.size(); i++) {
                            if (data_list.get(i).equals(finalName)) {
                                strategy_add_pro.setSelection(i);
                            }
                        }
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

    //获取数据源
    private void getData2() {
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        final User user = TotalUrl.getUser();
        final String idname = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Organ> organList = OrganHttp.selectAllProj(user);
                List<SelectUser> selectUsers = UserHttp.thisUser(user);
                if (selectUsers == null || selectUsers.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AddStrategyActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                if (organList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AddStrategyActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                String pro = selectUsers.get(0).getsS_Project();
                String[] proList = pro.split(",");
                String name = "";
                //数据
                data_list = new ArrayList<>();
                data_list2 = new ArrayList<>();
                if (proList.length >= 1) {
                    for (int i = 0; i < proList.length; i++) {
                        for (int j = 0; j < organList.size(); j++) {
                            if (proList[i].equals(organList.get(j).getsS_Id())) {
                                data_list.add(organList.get(i).getsS_FullName());
                                data_list2.add(organList.get(i).getsS_Id());
                            }
                        }
                    }
                }
                if (data_list.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AddStrategyActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    finish();
                    return;
                }
                for (int i = 0; i < organList.size(); i++) {
                    if (idname.equals(organList.get(i).getsS_ParentId())) {
                        name = organList.get(i).getsS_FullName();
                    }
                }

                final String finalName = name;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //适配器
                        arr_adapter = new ArrayAdapter<>(AddStrategyActivity.this, android.R.layout.simple_spinner_item, data_list);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        strategy_add_pro.setAdapter(arr_adapter);
                        for (int i = 0; i < data_list.size(); i++) {
                            if (data_list.get(i).equals(finalName)) {
                                strategy_add_pro.setSelection(i);
                            }
                        }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //使能开关
            case R.id.strategy_add_S_Enabled:
                S_EnabledList.clear();
                S_EnabledList.add(new TypeBean(1, getString(R.string.open)));
                S_EnabledList.add(new TypeBean(0, getString(R.string.close)));
                Util.alertBottomWheelOption(AddStrategyActivity.this, S_EnabledList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_add_S_Enabled.setText("");
                        strategy_add_S_Enabled.setText(S_EnabledList.get(postion).getName());
                    }
                });
                break;
            //优先级
            case R.id.strategy_add_S_Priority:
                S_PriorityList.clear();
                S_PriorityList.add(new TypeBean(1, getString(R.string.OneStage)));
                S_PriorityList.add(new TypeBean(2, getString(R.string.TwoStage)));
                S_PriorityList.add(new TypeBean(3, getString(R.string.ThreeStage)));
                S_PriorityList.add(new TypeBean(4, getString(R.string.FourStage)));
                Util.alertBottomWheelOption(AddStrategyActivity.this, S_PriorityList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_add_S_Priority.setText("");
                        strategy_add_S_Priority.setText(S_PriorityList.get(postion).getName());
                    }
                });
                break;
            //周期
            case R.id.strategy_add_S_Cycle:
                S_CycleList.clear();
                S_CycleList.add(new TypeBean(1, getString(R.string.day)));
                S_CycleList.add(new TypeBean(2, getString(R.string.week)));
                S_CycleList.add(new TypeBean(3,getString(R.string.month)));
                S_CycleList.add(new TypeBean(4, getString(R.string.year)));
                Util.alertBottomWheelOption(AddStrategyActivity.this, S_CycleList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_add_S_Cycle.setText("");
                        strategy_add_S_Cycle.setText(S_CycleList.get(postion).getName());
                    }
                });
                break;
            //生效日期
            case R.id.strategy_add_S_StartYear:
                String format = "";
                TimePickerView.Type type;
                type = TimePickerView.Type.YEAR_MONTH_DAY;
                format = "yyyy-MM-dd";
                Util.alertTimerPicker(this, type, format, new Util.TimerPickerCallBack() {
                    @Override
                    public void onTimeSelect(String date) {
                        // Toast.makeText(TestActivity.this, date, Toast.LENGTH_SHORT).show();
                        strategy_add_S_StartYear.setText("");
                        strategy_add_S_StartYear.setText(date);
                    }
                });
                break;
            //失效日期
            case R.id.strategy_add_S_EndYear:
                String format1 = "";
                TimePickerView.Type type1;
                type1 = TimePickerView.Type.YEAR_MONTH_DAY;
                format1 = "yyyy-MM-dd";
                Util.alertTimerPicker(this, type1, format1, new Util.TimerPickerCallBack() {
                    @Override
                    public void onTimeSelect(String date) {
                        strategy_add_S_EndYear.setText("");
                        strategy_add_S_EndYear.setText(date);
                    }
                });
                break;
            //起始周
            case R.id.strategy_add_S_StartWeek:
                S_StartWeekList.clear();
                S_StartWeekList.add(new TypeBean(1, getString(R.string.Monday)));
                S_StartWeekList.add(new TypeBean(2, getString(R.string.Tuesday)));
                S_StartWeekList.add(new TypeBean(3, getString(R.string.Wednesday)));
                S_StartWeekList.add(new TypeBean(4, getString(R.string.Thursday)));
                S_StartWeekList.add(new TypeBean(5, getString(R.string.Friday)));
                S_StartWeekList.add(new TypeBean(6, getString(R.string.Saturday)));
                S_StartWeekList.add(new TypeBean(7, getString(R.string.Sunday)));
                Util.alertBottomWheelOption(AddStrategyActivity.this, S_StartWeekList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_add_S_StartWeek.setText("");
                        strategy_add_S_StartWeek.setText(S_StartWeekList.get(postion).getName());
                    }
                });
                break;
            //结束周
            case R.id.strategy_add_S_EndWeek:
                S_EndWeekList.clear();
                S_EndWeekList.add(new TypeBean(1, getString(R.string.Monday)));
                S_EndWeekList.add(new TypeBean(2, getString(R.string.Tuesday)));
                S_EndWeekList.add(new TypeBean(3, getString(R.string.Wednesday)));
                S_EndWeekList.add(new TypeBean(4, getString(R.string.Thursday)));
                S_EndWeekList.add(new TypeBean(5, getString(R.string.Friday)));
                S_EndWeekList.add(new TypeBean(6, getString(R.string.Saturday)));
                S_EndWeekList.add(new TypeBean(7, getString(R.string.Sunday)));
                Util.alertBottomWheelOption(AddStrategyActivity.this, S_EndWeekList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_add_S_EndWeek.setText("");
                        strategy_add_S_EndWeek.setText(S_EndWeekList.get(postion).getName());
                    }
                });
                break;
            //参考点
            case R.id.strategy_add_S_Reference:
                S_ReferenceList.clear();
                S_ReferenceList.add(new TypeBean(1, getString(R.string.standardTime)));
                S_ReferenceList.add(new TypeBean(2, getString(R.string.beforeSunrise)));
                S_ReferenceList.add(new TypeBean(3, getString(R.string.AfterSunrise)));
                S_ReferenceList.add(new TypeBean(4, getString(R.string.BeforeSunset)));
                S_ReferenceList.add(new TypeBean(5, getString(R.string.AfterSunset)));
                Util.alertBottomWheelOption(AddStrategyActivity.this, S_ReferenceList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_add_S_Reference.setText("");
                        strategy_add_S_Reference.setText(S_ReferenceList.get(postion).getName());
                    }
                });
                break;
            //触发时间
            case R.id.strategy_add_S_Hour:
                String format2 = "";
                TimePickerView.Type type2;
                type2 = TimePickerView.Type.HOURS_MINS;
                format2 = "HH:mm";
                Util.alertTimerPicker(this, type2, format2, new Util.TimerPickerCallBack() {
                    @Override
                    public void onTimeSelect(String date) {
                        strategy_add_S_Hour.setText("");
                        strategy_add_S_Hour.setText(date);
                    }
                });
                break;
            //分组
            case R.id.strategy_add_S_GroupMask:
                S_GroupMaskList.clear();
                for (int i = 0; i < 16; i++) {
                    S_GroupMaskList.add(new TypeBean((i + 1), (getString(R.string.Group) + (i + 1))));
                    Log.e(TAG, "分组id:" + S_GroupMaskList.get(i).getId() + "---分组名称:" + S_GroupMaskList.get(i).getName());
                }
                showMutilAlertDialog(S_GroupMaskList, "strategy_add_S_GroupMask");

                break;
            //调光使能
            case R.id.strategy_add_S_DimEnabled:
                S_DimEnabledList.clear();
                S_DimEnabledList.add(new TypeBean(1, getString(R.string.open)));
                S_DimEnabledList.add(new TypeBean(0, getString(R.string.close)));
                Util.alertBottomWheelOption(AddStrategyActivity.this, S_DimEnabledList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_add_S_DimEnabled.setText("");
                        strategy_add_S_DimEnabled.setText(S_DimEnabledList.get(postion).getName());
                    }
                });
                break;
            //回路开关
            case R.id.strategy_add_S_Switch:
                S_SwitchList.clear();
                S_SwitchList.add(new TypeBean(1, getString(R.string.open)));
                S_SwitchList.add(new TypeBean(0, getString(R.string.close)));
                Util.alertBottomWheelOption(AddStrategyActivity.this, S_SwitchList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        strategy_add_S_Switch.setText("");
                        strategy_add_S_Switch.setText(S_SwitchList.get(postion).getName());
                    }
                });
                break;
            //回路值
            case R.id.strategy_add_S_LoopMsk:
                S_LoopMskList.clear();
                for (int i = 0; i < 8; i++) {
                    S_LoopMskList.add(new TypeBean((i + 1), (getString(R.string.Loop) + (i + 1))));
                    Log.e(TAG, "回路id:" + S_LoopMskList.get(i).getId() + "---回路名称:" + S_LoopMskList.get(i).getName());
                }
                showMutilAlertDialog(S_LoopMskList, "strategy_add_S_LoopMsk");
                break;

            case R.id.strategy_add_img2:
                finish();
                break;
            case R.id.strategy_add_img3:

                if (strategy_add_S_StartYear.getText().length() < 4) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectTheCorrectDate, 1 * 1000);
                    return;
                }
                if (strategy_add_S_EndYear.getText().length() < 4) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectTheCorrectDate, 1 * 1000);
                    return;
                }

                if (Integer.parseInt(strategy_add_S_Number.getText().toString()) > 30) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseEnterTheCorrectNumber, 1 * 1000);
                    return;
                }

                if (Integer.parseInt(strategy_add_S_DimValue.getText().toString()) > 100) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseEnterTheCorrectBrightness, 1 * 1000);
                    return;
                }

                //开始
                String sta = strategy_add_S_StartYear.getText().toString().replace("-", "");
                //结束
                String end = strategy_add_S_EndYear.getText().toString().replace("-", "");

                int staint = Integer.parseInt(sta);
                int endint = Integer.parseInt(end);
                //修改数据
                if (strategy_add_S_Number.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseEnterTheCorrectNumber, 1 * 1000);
                } else if (strategy_add_S_FullName.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseEnterTheCorrectName, 1 * 1000);
                } else if (strategy_add_S_Enabled.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectEnableSwitch, 1 * 1000);
                } else if (strategy_add_S_Priority.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.selectingPriority, 1 * 1000);
                } else if (strategy_add_S_Cycle.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.selectcycle, 1 * 1000);
                } else if (staint >= endint) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectTheCorrectDate, 1 * 1000);
                } else if (strategy_add_S_StartWeek.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.SelectStartWeek, 1 * 1000);
                } else if (strategy_add_S_EndWeek.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectEndWeeks, 1 * 1000);
                } else if (strategy_add_S_Reference.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectAReferencePoint, 1 * 1000);
                } else if (strategy_add_S_Hour.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectTriggerTime, 1 * 1000);
                } else if (strategy_add_S_GroupMask.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectGroups, 1 * 1000);
                } else if (strategy_add_S_DimEnabled.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this,R.string.PleaseSelectDimming, 1 * 1000);
                } else if (strategy_add_S_DimValue.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseEnterTheCorrectBrightness, 1 * 1000);
                } else if (strategy_add_S_Switch.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectLoopSwitch, 1 * 1000);
                } else if (strategy_add_S_LoopMsk.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectLoop, 1 * 1000);
                } else if (strategy_add_S_Description.getText().length() < 1) {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseInputMemoInformation, 1 * 1000);
                } else {
                    //转换数据
                    convertData();
                    Log.e(TAG, "转换后的数据:" + strategy);
                    Log.e(TAG, "转换后的数据项目:" + strategy.getsSL_Organize_S_Id());
                    addStrategy();
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddStrategyActivity.this);
        // 设置标题
        alertDialogBuilder.setTitle(getResources().getString(R.string.LoopSet));
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
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectTheCorrectInformation, 1 * 1000);
                    return;
                }
                for (int i = 0; i < selected2.length; i++) {
                    if (selected2[i] == true) {
                        selectedStr += proj2[i] + ",";
                    }
                }
                if (selectedStr == "," || selectedStr == "") {
                    ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseSelectTheCorrectInformation, 1 * 1000);
                    ActivityUtil.showToasts(AddStrategyActivity.this, "请选择正确信息", 1 * 1000);
                    return;
                }
                Log.e(TAG, "点击确定：" + selectedStr);
                if (id.equals("strategy_add_S_GroupMask")) {
                    strategy_add_S_GroupMask.setText(selectedStr);
                } else {
                    strategy_add_S_LoopMsk.setText(selectedStr);
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

    String proj = "";

    private void convertData() {
        proj = "";
        //获取数据并且转换数据
        strategy_add_pro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        if (pro.equals("")) {
            // 机构或机构主键的ID
            if (data_list2.size() < 1) {
                ActivityUtil.showToasts(AddStrategyActivity.this, R.string.PleaseAddItemInformationFirst, 1 * 1500);
                return;
            } else {
                pro = data_list2.get(0);
            }
        }
        Log.e(TAG, "项目pro:" + pro);
        String sS_Number = strategy_add_S_Number.getText().toString();
        String sS_FullName = strategy_add_S_FullName.getText().toString();
        String sS_Enabled = strategy_add_S_Enabled.getText().toString();
        String sS_Priority = strategy_add_S_Priority.getText().toString();
        String sS_Cycle = strategy_add_S_Cycle.getText().toString();
        String sS_StartYear = strategy_add_S_StartYear.getText().toString();
        String sS_EndYear = strategy_add_S_EndYear.getText().toString();
        String sS_StartWeek = strategy_add_S_StartWeek.getText().toString();
        String sS_EndWeek = strategy_add_S_EndWeek.getText().toString();
        String sS_Reference = strategy_add_S_Reference.getText().toString();
        String sS_Hour = strategy_add_S_Hour.getText().toString();
        String sS_GroupMask = strategy_add_S_GroupMask.getText().toString();
        String sS_DimEnabled = strategy_add_S_DimEnabled.getText().toString();
        String sS_DimValue = strategy_add_S_DimValue.getText().toString();
        String sS_Switch = strategy_add_S_Switch.getText().toString();
        String sS_Description = strategy_add_S_Description.getText().toString();
        String sS_LoopMsk = strategy_add_S_LoopMsk.getText().toString();
        if (!sS_GroupMask.equals("")) {
            Log.e(TAG, "分组:" + sS_GroupMask);
            //分组
            strategy.setsS_GroupMask(GsonUtil.getGroup(sS_GroupMask));
        }
        if (!sS_LoopMsk.equals("")) {
            //回路
            strategy.setsS_LoopMsk(GsonUtil.getLamp(sS_LoopMsk));
        }
        // 机构或机构主键的ID
        strategy.setsSL_Organize_S_Id(pro);
        //策略名称
        strategy.setsS_FullName(sS_FullName);
        // 使能开关 1 开 0关
        if (sS_Enabled.equals(getString(R.string.open))) {
            strategy.setsS_Enabled(1);
        } else {
            strategy.setsS_Enabled(0);
        }

        // 策略优先级
        if (sS_Priority.equals(getString(R.string.OneStage))) {
            strategy.setsS_Priority(1);
        } else if (sS_Priority.equals(getString(R.string.TwoStage))) {
            strategy.setsS_Priority(2);
        } else if (sS_Priority.equals(getString(R.string.ThreeStage))) {
            strategy.setsS_Priority(3);
        } else if (sS_Priority.equals(getString(R.string.FourStage))) {
            strategy.setsS_Priority(4);
        }

        // 策略实行周期
        if (sS_Cycle.equals(getString(R.string.day))) {
            strategy.setsS_Cycle(1);
        } else if (sS_Cycle.equals(getString(R.string.week))) {
            strategy.setsS_Cycle(2);
        } else if (sS_Cycle.equals(getString(R.string.month))) {
            strategy.setsS_Cycle(3);
        } else if (sS_Cycle.equals(getString(R.string.year))) {
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
        if (sS_StartWeek.equals(getString(R.string.Monday))) {
            strategy.setsS_StartWeek(1);
        } else if (sS_StartWeek.equals(getString(R.string.Tuesday))) {
            strategy.setsS_StartWeek(2);

        } else if (sS_StartWeek.equals(getString(R.string.Wednesday))) {
            strategy.setsS_StartWeek(3);

        } else if (sS_StartWeek.equals(getString(R.string.Thursday))) {
            strategy.setsS_StartWeek(4);

        } else if (sS_StartWeek.equals(getString(R.string.Friday))) {
            strategy.setsS_StartWeek(5);

        } else if (sS_StartWeek.equals(getString(R.string.Saturday))) {
            strategy.setsS_StartWeek(6);

        } else if (sS_StartWeek.equals(getString(R.string.Sunday))) {
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
        if (sS_EndWeek.equals(getString(R.string.Monday))) {
            strategy.setsS_EndWeek(1);
        } else if (sS_EndWeek.equals(getString(R.string.Tuesday))) {
            strategy.setsS_EndWeek(2);
        } else if (sS_EndWeek.equals(getString(R.string.Wednesday))) {
            strategy.setsS_EndWeek(3);
        } else if (sS_EndWeek.equals(getString(R.string.Thursday))) {
            strategy.setsS_EndWeek(4);
        } else if (sS_EndWeek.equals(getString(R.string.Friday))) {
            strategy.setsS_EndWeek(5);
        } else if (sS_EndWeek.equals(getString(R.string.Saturday))) {
            strategy.setsS_EndWeek(6);
        } else if (sS_EndWeek.equals(getString(R.string.Sunday))) {
            strategy.setsS_EndWeek(7);
        }

        // 策略时间参考点
        if (sS_Reference.equals(getString(R.string.standardTime))){
            strategy.setsS_Reference(1);

        }else if (sS_Reference.equals(getString(R.string.beforeSunrise))){
            strategy.setsS_Reference(1);

        }else if (sS_Reference.equals(getString(R.string.AfterSunrise))){
            strategy.setsS_Reference(3);

        }else if (sS_Reference.equals( getString(R.string.BeforeSunset))){
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
        if (sS_DimEnabled.equals(getString(R.string.open))) {
            strategy.setsS_DimValue(1);
        } else {
            strategy.setsS_DimValue(0);
        }
        // 回路开关
        if (sS_Switch.equals(getString(R.string.open))) {
            strategy.setsS_Switch(1);
        } else {
            strategy.setsS_Switch(0);
        }
        // 策略信息备注
        strategy.setsS_Description(sS_Description);
        // 策略编号
        strategy.setsS_Number(Integer.parseInt(sS_Number));
        // 调光值
        strategy.setsS_DimValue(Integer.parseInt(sS_DimValue));
    }

    //添加
    private void addStrategy() {
        final User user = TotalUrl.getUser();
        //设置对话框标题
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.Add))
                .setMessage(getResources().getString(R.string.WhetherToAdd))
                .setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = StrategyHttp.addStrategy(strategy, user);
                                if (b) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(AddStrategyActivity.this, R.string.Addsuccess, 1 * 1000);
                                            finish();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ActivityUtil.showToasts(AddStrategyActivity.this, R.string.Addfailed, 1 * 1000);
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }).setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
            }
        }).show();//在按键响应事件中显示此对话框
    }

}
