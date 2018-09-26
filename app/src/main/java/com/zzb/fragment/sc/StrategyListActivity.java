package com.zzb.fragment.sc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zzb.bean.Host;
import com.zzb.bean.Organ;
import com.zzb.bean.Strategy;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.fragment.sc.stl.adapter.ListAdapter;
import com.zzb.fragment.sc.stl.bean.SelectEvent;
import com.zzb.fragment.sc.stl.widget.OnStartDragListener;
import com.zzb.fragment.sc.stl.widget.SimpleItemTouchHelperCallback;
import com.zzb.http.OrganHttp;
import com.zzb.http.StrategyHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;
import com.zzb.util.GsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class StrategyListActivity extends SwipeBackActivity implements View.OnClickListener, OnStartDragListener {
    private static final String TAG = "StrategyListActivity";
    private Host host = new Host();
    private RecyclerView strategy_list_recyclerview;
    private SwipeRefreshLayout sfl;
    private EventBus event;
    private boolean isPause;
    private CheckBox checkbox;
    HashMap<String, Boolean> map = new HashMap<>();
    HashMap<String, Boolean> send ;
    private ItemTouchHelper mItemTouchHelper;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private List<Strategy> strategies = new ArrayList<>();
    private boolean isChange = false;
    private TextView selected;
    private List<Strategy> strategyList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_list);
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_STRATEGYHOST");
        strategy_list_recyclerview = (RecyclerView) findViewById(R.id.strategy_list_recyclerview);
        checkbox = (CheckBox) findViewById(R.id.strategy_list_checkbox);
        sfl = (SwipeRefreshLayout) findViewById(R.id.strategy_list_srfl);
        selected = (TextView) findViewById(R.id.strategy_list_selected);
        findViewById(R.id.strategy_list_img2).setOnClickListener(this);
        findViewById(R.id.strategy_list_img3).setOnClickListener(this);
        event = EventBus.getDefault();
        event.register(this);
        ButterKnife.bind(this);
        getData();
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selected.setText("");
                map.clear();
                if (strategyList ==null ){
                    getData();
                    sfl.setRefreshing(false);
                }
                if ( strategyList !=null || strategyList.size() >=1){
                    //初始化
                    strategyList.clear();
                    getData();
                    sfl.setRefreshing(false);
                }



            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.strategy_list_img2:
                finish();
                break;
            case R.id.strategy_list_img3:
                //点击下载
                if (map == null || map.size() < 1) {
                    ActivityUtil.showToasts(StrategyListActivity.this, R.string.PleaseSelectData, 1 * 1000);
                    break;
                }
                String id = "";
                //获取ID
                for (String in : map.keySet()) {
                    boolean str = map.get(in);
                    if (str) {
                        id += in + ",";
                        Log.e(TAG, "获取数据:" + id);
                    }
                }
                if (id == null || id == "") {
                    break;
                }
                String[] S_SId = id.split(",");
                send(S_SId);
                break;
        }
    }


    //查询所属策略
    private void getData() {
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String Pro = host.getsSL_Organize_S_Id();
        final String userPro = user.getdate().getOrganizeId();
        //查询项目信息

        new Thread(new Runnable() {
            @Override
            public void run() {
                strategies = StrategyHttp.SelectAllStrategy(user);
                final List<Organ> organList =  OrganHttp.selectAllProj(user);
                if (strategies == null || strategies.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyListActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });

                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                if (organList == null || organList.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(StrategyListActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                for (int i = 0; i < organList.size(); i++) {
                    String pro = organList.get(i).getsS_ParentId();
                    String proID = organList.get(i).getsS_Id();
                    if (pro.equals(userPro)){
                        for (int j = 0; j <strategies.size() ; j++) {
                            String org = strategies.get(j).getsSL_Organize_S_Id();
                            if (org.equals(proID)){
                                strategyList.add(strategies.get(j));
                            }
                        }
                    }
                }
                Collections.sort(strategies, new Comparator<Strategy>() {
                    @Override
                    public int compare(Strategy o1, Strategy o2) {
                        if (o1.getsS_Number() > o2.getsS_Number()) {
                            return 1;
                        }
                        if (o1.getsS_Number() == o2.getsS_Number()) {
                            return 1;
                        }
                        return -1;
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ListAdapter adapter = new ListAdapter(strategyList, StrategyListActivity.this, event);
                        strategy_list_recyclerview.setAdapter(adapter);
                        // recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                        strategy_list_recyclerview.setLayoutManager(new LinearLayoutManager(StrategyListActivity.this));
                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                        mItemTouchHelper = new ItemTouchHelper(callback);
                        mItemTouchHelper.attachToRecyclerView(strategy_list_recyclerview);
                        checkbox.setChecked(true);
                        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                try {
                                    int count = 0;
                                    if (isChecked) {
                                        isChange = false;
                                    }
                                    for (int i = 0, p = strategies.size(); i < p; i++) {
                                        if (isChecked) {
                                            map.put(strategies.get(i).getsS_Id(), true);
                                            count++;
                                        } else {
                                            if (!isChange) {
                                                map.put(strategies.get(i).getsS_Id(), false);
                                                count = 0;
                                            } else {
                                                map = adapter.getMap();
                                                count = map.size();
                                            }

                                        }
                                    }
                                    selected.setText(getString(R.string.Selected) + count + getString(R.string.item));
                                    adapter.setMap(map);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        adapter.setOnItemClickListener(new ListAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                                Strategy ho = strategies.get(position);
                                Log.e(TAG,"跳转的:"+ho);
                                Intent intent = new Intent();
                                intent.setClass(StrategyListActivity.this, StrategyInfoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_STRATEGYCONTREETINFO", ho);
                                bundle.putSerializable("SELECTUSER_STRATEGYCONTREETINFOHOST", host);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(RecyclerView.ViewHolder holder, int position) {

                            }
                        });
                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler.sendMessage(msg_listData);
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

    public void onEventMainThread(SelectEvent event) {
        int size = event.getSize();
        if (size < strategies.size()) {
            isChange = true;
            checkbox.setChecked(false);

        } else {
            checkbox.setChecked(true);
            isChange = false;
        }
        selected.setText(getString(R.string.Selected) + size + getString(R.string.item));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        event.unregister(this);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) { //判断是否暂停
            isPause = false;
            if(strategyList == null){
                return;
            }
            if (strategyList != null || strategyList.size()>=1){
                strategyList.clear();
            }
            selected.setText("");
            map.clear();
            getData();
        }
    }
    String logs = "";
    List<String> infoSOK;
    List<String> infoSNO;
    private void send(String S_SId[]) {
        infoSOK = new ArrayList<>();
        infoSNO = new ArrayList<>();
        logs = "";
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.download), getResources().getString(R.string.Downloading));
        send = new HashMap<>();
        for (int i = 0; i < S_SId.length; i++) {
            for (int j = 0; j < strategies.size(); j++) {
                if (S_SId[i].equals(strategies.get(j).getsS_Id())) {
                    logs = strategies.get(j).getsS_FullName();
                    //注册包
                    String pack = host.getsS_RegPackage();
                    //策略编号
                    String numb = strategies.get(j).getsS_Number() + "";
                    //使能
                    String S_Enabled = strategies.get(i).getsS_Enabled() + "";
                    //优先级
                    String S_Priority = strategies.get(i).getsS_Priority() + "";
                    //策略周期
                    String S_Cycle = strategies.get(i).getsS_Cycle() + "";
                    //起始年
                    String S_StartYear = String.valueOf(strategies.get(i).getsS_StartYear()).substring(2);
                    Log.e(TAG,"起始年:"+S_StartYear);
                    //月
                    String S_StartMonth = strategies.get(i).getsS_StartMonth() + "";
                    //日
                    String S_StartDay = strategies.get(i).getsS_StartDay() + "";
                    //周
                    String S_StartWeek = strategies.get(i).getsS_StartWeek() + "";
                    //结束年
                    String S_EndYear = String.valueOf(strategies.get(i).getsS_EndYear()).substring(2);
                    //月
                    String S_EndMonth = strategies.get(i).getsS_EndMonth() + "";
                    //日
                    String S_EndDay = strategies.get(i).getsS_EndDay() + "";
                    //周
                    String S_EndWeek = strategies.get(i).getsS_EndWeek() + "";
                    //参考时间
                    String S_Reference = strategies.get(i).getsS_Reference() + "";
                    //触发时
                    String S_Hour = strategies.get(i).getsS_Hour() + "";
                    //分
                    String S_Minute = strategies.get(i).getsS_Minute() + "";
                    // 分组
                    String a =strategies.get(i).getsS_GroupMask();
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
                    String S_DimEnabled = strategies.get(i).getsS_DimEnabled() + "";
                    //调光值
                    String S_DimValue = strategies.get(i).getsS_DimValue() + "";
                    //回路开关
                    String S_Switch = strategies.get(i).getsS_Switch() + "";
                    //回路掩码
                    String S_LoopMsk = strategies.get(i).getsS_LoopMsk() + "";
                    //机构
                    String org = strategies.get(i).getsSL_Organize_S_Id();
                    final String value = pack + "," + numb + "," + S_Enabled + "," + S_Priority + "," + S_Cycle + "," + S_StartYear + "." + S_StartMonth + "." + S_StartDay + "." + S_StartWeek + "," +
                            S_EndYear + "." + S_EndMonth + "." + S_EndDay + "." + S_EndWeek + "," + S_Reference + "," + S_Hour + "." + S_Minute + "," + xxx + "," + S_DimEnabled + "," +
                            S_DimValue + "," + S_Switch + "," + S_LoopMsk + "," + org;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                           boolean b = StrategyHttp.dowStrategy(value);
                            if(b){
                                infoSOK.add(logs);
                            }else{
                                infoSNO.add(logs);
                            }
                        }
                    }).start();
                }
            }
        }
        if (infoSOK != null || infoSOK.size()>=1){
            ActivityUtil.showToasts(StrategyListActivity.this,R.string.downloadOK,1*2000);
        }

        if (infoSNO != null || infoSNO.size()>=1){
            ActivityUtil.showToasts(StrategyListActivity.this,R.string.downloadNO,1*2000);
        }
        //发送handler
        Message msg_listData = new Message();
        msg_listData.what = MESSAGETYPE_01;
        handler2.sendMessage(msg_listData);
    }
    private Handler handler2 = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGETYPE_01:
                    //刷新UI，显示数据，并关闭进度条
                    progressDialog.dismiss(); //关闭进度条
                    break;
            }
        }
    };

}
