package com.zzb.sl.TimeController.LoopController;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzb.bean.Host;
import com.zzb.bean.HostDataSet;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.TypeBean;
import com.zzb.bean.User;
import com.zzb.http.HostHttp;
import com.zzb.http.LoopHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.sl.TimeController.LoopController.LoopCheck.adapter.ListAdapter;
import com.zzb.sl.TimeController.LoopController.LoopCheck.bean.SelectEvent;
import com.zzb.sl.TimeController.LoopController.LoopCheck.widget.OnStartDragListener;
import com.zzb.sl.TimeController.LoopController.LoopCheck.widget.SimpleItemTouchHelperCallback;
import com.zzb.util.ActivityUtil;
import com.zzb.util.GsonUtil;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class LoopControllerActivity extends SwipeBackActivity implements OnStartDragListener, View.OnClickListener {
    private static final String TAG = "LoopControllerActivity";
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    public static PopupWindow mPopupWindow;

    private List<HostDataSet> hostDS = new ArrayList<>();
    private List<Host> hostList = new ArrayList<>();

    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView recyclerView;
    private CheckBox checkbox;
    private TextView selected;
    private EventBus event;
    HashMap<String, Boolean> map;
    private boolean isChange = false;
    private String as = "";
    private SwipeRefreshLayout sfl;
    private boolean isPause;
    private RelativeLayout activity_one_lamp_c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_controller);
        recyclerView = (RecyclerView) findViewById(R.id.loop_list_recyclerview);
        activity_one_lamp_c = (RelativeLayout) findViewById(R.id.loopyc);
        checkbox = (CheckBox) findViewById(R.id.loop_list_checkbox);
        selected = (TextView) findViewById(R.id.loop_list_selected);
        sfl = (SwipeRefreshLayout) findViewById(R.id.loop_list_srfl);
        event = EventBus.getDefault();
        event.register(this);
        ButterKnife.bind(this);
        User user = TotalUrl.getUser();
        if(user == null){
            return;
        }
        int userQ = user.getdate().getSL_USER_RANKID();

        if (userQ == 2) {
            getData();
            sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    hostDS.clear();
                    getData();
                    sfl.setRefreshing(false);
                }
            });
        } else {
            getData2();
            sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    hostDS.clear();
                    getData2();
                    sfl.setRefreshing(false);
                }
            });
        }
        findViewById(R.id.loop_list_img3).setOnClickListener(this);
        findViewById(R.id.loop_list_img2).setOnClickListener(this);
    }


    //获取数据
    private void getData() {
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String userPro = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询所有主机状态信息
                List<HostDataSet> hdList = LoopHttp.SelectAllHostDataSet(user);
                if (hdList == null || hdList.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LoopControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                //查询项目信息
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null || organList.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LoopControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                //主机
                List<Host> hoseLists = HostHttp.SelectAllHost(user);
                if (hoseLists == null || hoseLists.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LoopControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
              /*  for (int i = 0; i < hdList.size(); i++) {
                    String loopString = hdList.get(i).getsS_LoopState();
                    hdList.get(i).setsS_LoopState(GsonUtil.getLoop(loopString));
                }*/
                for (int i = 0; i < organList.size(); i++) {
                    String pro = organList.get(i).getsS_ParentId();
                    String proID = organList.get(i).getsS_Id();
                    if (pro.equals(userPro)) {
                        for (int j = 0; j < hoseLists.size(); j++) {
                            String org = hoseLists.get(j).getsSL_Organize_S_Id();
                            if (org.equals(proID)) {
                                hostList.add(hoseLists.get(j));
                                for (int k = 0; k < hdList.size(); k++) {
                                    if (hoseLists.get(j).getsS_RegPackage().equals(hdList.get(k).getsS_RegPackage())) {
                                        hdList.get(k).setSL_Organize_S_Id(hoseLists.get(j).getsSL_Organize_S_Id());
                                        hdList.get(k).setsS_Fname(hoseLists.get(j).getsS_FullName());
                                        hostDS.add(hdList.get(k));
                                    }
                                }
                            }
                        }
                    }
                }
                Log.e(TAG, "hostList:" + hostList);
                Log.e(TAG, "hostDS:" + hostDS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ListAdapter adapter = new ListAdapter(hostDS, LoopControllerActivity.this, event);
                        recyclerView.setAdapter(adapter);
                        // recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                        recyclerView.setLayoutManager(new LinearLayoutManager(LoopControllerActivity.this));

                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                        mItemTouchHelper = new ItemTouchHelper(callback);
                        mItemTouchHelper.attachToRecyclerView(recyclerView);
                        checkbox.setChecked(true);
                        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                map = new HashMap<String, Boolean>();
                                try {
                                    int count = 0;
                                    if (isChecked) {
                                        isChange = false;
                                    }

                                    for (int i = 0, p = hostDS.size(); i < p; i++) {
                                        if (isChecked) {
                                            map.put(hostDS.get(i).getsS_RegPackage(), true);

                                            count++;
                                            as += hostDS.get(i).getsS_RegPackage() + ",";
                                        } else {
                                            if (!isChange) {
                                                map.put(hostDS.get(i).getsS_RegPackage(), false);
                                                count = 0;
                                                as = "";
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
                            public void onItemClick(RecyclerView.ViewHolder holder, int positon) {
                                Log.e("onItemClick", "" + positon);
                                HostDataSet hostDataSet = hostDS.get(positon);
                                Intent intent = new Intent(LoopControllerActivity.this, LoopInfoControllerActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_HOSTDATASET", hostDataSet);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(final RecyclerView.ViewHolder holder, final int positon) {
                                Log.e("onItemLongClick", "" + positon);

                            }
                        });
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

    //获取数据
    private void getData2() {
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String userPro = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询所有主机状态信息
                List<HostDataSet> hdList = LoopHttp.SelectAllHostDataSet(user);
                if (hdList == null || hdList.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LoopControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                //查询项目信息
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null || organList.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LoopControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                //主机
                List<Host> hoseLists = HostHttp.SelectAllHost(user);
                if (hoseLists == null || hoseLists.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LoopControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                //用户信息
                List<SelectUser> selectUList1 = UserHttp.thisUser(user);
                if (selectUList1 == null || selectUList1.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LoopControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                if (selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() == "") {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(LoopControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                String[] proList;
                if (selectUList1.get(0).getsS_Project().contains(",")) {
                    proList = selectUList1.get(0).getsS_Project().split(",");
                } else {
                    proList = (selectUList1.get(0).getsS_Project() + ",").split(",");
                }

               /* for (int i = 0; i < hdList.size(); i++) {
                    String loopString = hdList.get(i).getsS_LoopState();
                    hdList.get(i).setsS_LoopState(GsonUtil.getLoop(loopString));
                }*/
                for (int a = 0; a < proList.length; a++) {
                    for (int i = 0; i < organList.size(); i++) {
                        if (proList[a].equals(organList.get(i).getsS_Id())) {
                            String proID = organList.get(i).getsS_Id();
                            for (int j = 0; j < hoseLists.size(); j++) {
                                String userOrg = hoseLists.get(j).getsSL_Organize_S_Id();
                                if (userOrg.equals(proID)) {
                                    hostList.add(hoseLists.get(j));
                                    for (int k = 0; k < hdList.size(); k++) {
                                        if (hoseLists.get(j).getsS_RegPackage().equals(hdList.get(k).getsS_RegPackage())) {
                                            hdList.get(k).setSL_Organize_S_Id(hoseLists.get(j).getsSL_Organize_S_Id());
                                            hdList.get(k).setsS_Fname(hoseLists.get(j).getsS_FullName());
                                            hostDS.add(hdList.get(k));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Log.e(TAG, "hostList:" + hostList);
                Log.e(TAG, "hostDS:" + hostDS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ListAdapter adapter = new ListAdapter(hostDS, LoopControllerActivity.this, event);
                        recyclerView.setAdapter(adapter);
                        // recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                        recyclerView.setLayoutManager(new LinearLayoutManager(LoopControllerActivity.this));

                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                        mItemTouchHelper = new ItemTouchHelper(callback);
                        mItemTouchHelper.attachToRecyclerView(recyclerView);
                        checkbox.setChecked(true);
                        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                map = new HashMap<String, Boolean>();
                                try {
                                    int count = 0;
                                    if (isChecked) {
                                        isChange = false;
                                    }

                                    for (int i = 0, p = hostDS.size(); i < p; i++) {
                                        if (isChecked) {
                                            map.put(hostDS.get(i).getsS_RegPackage(), true);

                                            count++;
                                            as += hostDS.get(i).getsS_RegPackage() + ",";
                                        } else {
                                            if (!isChange) {
                                                map.put(hostDS.get(i).getsS_RegPackage(), false);
                                                count = 0;
                                                as = "";
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
                            public void onItemClick(RecyclerView.ViewHolder holder, int positon) {
                                HostDataSet hostDataSet = hostDS.get(positon);
                                Intent intent = new Intent(LoopControllerActivity.this, LoopInfoControllerActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_HOSTDATASET", hostDataSet);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }

                            @Override
                            public void onItemLongClick(final RecyclerView.ViewHolder holder, final int positon) {
                                Log.e("onItemLongClick", "" + positon);

                            }
                        });
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


    public void onEventMainThread(SelectEvent event) {
        int size = event.getSize();
        if (size < hostDS.size()) {
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


    //开启回路
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loop_list_img2:
                finish();
                break;
            case R.id.loop_list_img3:
                //查询用户所属信息中的所属项目字段
                User user = TotalUrl.getUser();
                String userPro = user.getdate().getOrganizeId();
                String numb = "";
                if (map == null || map.size() < 1) {
                    ActivityUtil.showToasts(LoopControllerActivity.this, R.string.PleaseSelectData, 1 * 1000);
                } else {
                    for (String in : map.keySet()) {
                        boolean str = map.get(in);
                        if (str) {
                            numb += in + ",";
                            Log.e(TAG, "获取数据:" + numb);
                        }
                    }
                    ArrayList<TypeBean> S_LoopMskList = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        S_LoopMskList.add(new TypeBean((i + 1), (getString(R.string.Loop) + (i + 1))));
                        Log.e(TAG, "回路id:" + S_LoopMskList.get(i).getId() + "---回路名称:" + S_LoopMskList.get(i).getName());
                    }
                    //showMutilAlertDialog(S_LoopMskList, numb, userPro);
                    loogC(numb, userPro);
                }
                break;
        }

    }

    // 多选提示框
    private AlertDialog alertDialog3;
    private String a = "";
    private String b = "";

    public void showMutilAlertDialog(List<TypeBean> S_GroupMaskList, final String numb, final String orgid) {
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoopControllerActivity.this);
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
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.openLoop), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //TODO 业务逻辑代码
                String selectedStr = "";
                if (proj2.length < 1) {
                    ActivityUtil.showToasts(LoopControllerActivity.this, R.string.PleaseSelectTheCorrectInformation, 1 * 1000);
                    return;
                }
                for (int i = 0; i < selected2.length; i++) {
                    if (selected2[i] == true) {
                        selectedStr += proj2[i] + ",";
                    }
                }
                if (selectedStr == "," || selectedStr == "") {
                    ActivityUtil.showToasts(LoopControllerActivity.this, R.string.PleaseSelectTheCorrectInformation, 1 * 1000);
                    return;
                }
                Log.e(TAG, "点击确定：" + selectedStr);
                //开启回路

                //回路值
                String loopNumbString = GsonUtil.getLamp(selectedStr);
                //开启
                String openL = "01";

                //转换数据
                final String[] S_RegPackage = numb.split(",");
                if (S_RegPackage.length <= 1) {
                    final String value = S_RegPackage[0] + "," + openL + "," + loopNumbString + "," + orgid;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean b = LoopHttp.OpenOrCloseLoop(value);
                            if (b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ActivityUtil.showToasts(LoopControllerActivity.this, R.string.openOK, 1 * 1500);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ActivityUtil.showToasts(LoopControllerActivity.this, R.string.openNO, 1 * 1500);
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    //用于以后群控完成后的提示
                    final HashMap<String, Boolean> map = new HashMap<String, Boolean>();
                    for (int i = 0; i < S_RegPackage.length; i++) {
                        final String value = S_RegPackage[i] + "," + openL + "," + loopNumbString + "," + orgid;
                        final int a = (i + 1);
                        final String z = S_RegPackage[i];
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = LoopHttp.OpenOrCloseLoop(value);
                                if (b) {
                                    map.put(z, true);
                                } else {
                                    map.put(z, false);
                                }
                            }
                        }).start();
                    }
                }
                // 关闭提示框
                alertDialog3.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.closeLoop), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO 业务逻辑代码
                String selectedStr = "";
                if (proj2.length < 1) {
                    ActivityUtil.showToasts(LoopControllerActivity.this, R.string.PleaseSelectTheCorrectInformation, 1 * 1000);
                    return;
                }
                for (int i = 0; i < selected2.length; i++) {
                    if (selected2[i] == true) {
                        selectedStr += proj2[i] + ",";
                    }
                }
                if (selectedStr == "," || selectedStr == "") {
                    ActivityUtil.showToasts(LoopControllerActivity.this, R.string.PleaseSelectTheCorrectInformation, 1 * 1000);
                    return;
                }
                //关闭回路
                //回路值
                String loopNumbString = GsonUtil.getLamp(selectedStr);
                //开启
                String closeL = "00";

                //转换数据
                final String[] S_RegPackage = numb.split(",");
                if (S_RegPackage.length <= 1) {
                    final String value = S_RegPackage[0] + "," + closeL + "," + loopNumbString + "," + orgid;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean b = LoopHttp.OpenOrCloseLoop(value);
                            if (b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ActivityUtil.showToasts(LoopControllerActivity.this, R.string.closeOK, 1 * 1500);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ActivityUtil.showToasts(LoopControllerActivity.this, R.string.closeNO, 1 * 1500);
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    //用于以后群控完成后的提示
                    final HashMap<String, Boolean> map = new HashMap<String, Boolean>();
                    for (int i = 0; i < S_RegPackage.length; i++) {
                        final String value = S_RegPackage[i] + "," + closeL + "," + loopNumbString + "," + orgid;
                        final String z = S_RegPackage[i];
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = LoopHttp.OpenOrCloseLoop(value);
                                if (b) {
                                    map.put(z, true);
                                } else {
                                    map.put(z, false);
                                }
                            }
                        }).start();
                    }
                }
                // 关闭提示框
                alertDialog3.dismiss();
            }
        });
        alertDialog3 = alertDialogBuilder.create();
        alertDialog3.show();
    }

/*    @Override
    protected void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) { //判断是否暂停
            User user = TotalUrl.getUser();
            int userQ = user.getdate().getSL_USER_RANKID();
            if (userQ == 2) {
                isPause = false;
                hostDS.clear();
                getData();
            } else {
                isPause = false;
                hostDS.clear();
                getData2();
            }
        }
    }*/


    private List<SwitchButton> li;

    //回路
    public void loogC( final String numb, final String orgid) {
        activity_one_lamp_c.setBackgroundResource(R.color.colorTextGary);
        recyclerView.setVisibility(View.INVISIBLE);
        final SwitchButton switch_buttonmap_all, switch_buttonmap_loop1, switch_buttonmap_loop2,
                switch_buttonmap_loop3, switch_buttonmap_loop4, switch_buttonmap_loop5,
                switch_buttonmap_loop6, switch_buttonmap_loop7, switch_buttonmap_loop8;
        li = new ArrayList<>();
        //设置布局
        View popView = LayoutInflater.from(LoopControllerActivity.this).inflate(R.layout.activity_maploop, null);
        //设置宽高
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置允许在外点击消失
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //设置背景颜色
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x30000000));
        //设置动画
        mPopupWindow.setAnimationStyle(R.style.Animation_Button_Dialog);
        //参数1:根视图，整个Window界面的最顶层View 参数2:显示位置
        mPopupWindow.showAtLocation(LoopControllerActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        switch_buttonmap_all = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_all);
        switch_buttonmap_loop1 = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_loop1);
        switch_buttonmap_loop2 = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_loop2);
        switch_buttonmap_loop3 = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_loop3);
        switch_buttonmap_loop4 = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_loop4);
        switch_buttonmap_loop5 = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_loop5);
        switch_buttonmap_loop6 = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_loop6);
        switch_buttonmap_loop7 = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_loop7);
        switch_buttonmap_loop8 = (SwitchButton) popView.findViewById(R.id.switch_buttonmap_loop8);
        li.add(switch_buttonmap_loop1);
        li.add(switch_buttonmap_loop2);
        li.add(switch_buttonmap_loop3);
        li.add(switch_buttonmap_loop4);
        li.add(switch_buttonmap_loop5);
        li.add(switch_buttonmap_loop6);
        li.add(switch_buttonmap_loop7);
        li.add(switch_buttonmap_loop8);
        //全部
        switch_buttonmap_all.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                switch_buttonmap_loop1.setOnCheckedChangeListener(null);
                switch_buttonmap_loop2.setOnCheckedChangeListener(null);
                switch_buttonmap_loop3.setOnCheckedChangeListener(null);
                switch_buttonmap_loop4.setOnCheckedChangeListener(null);
                switch_buttonmap_loop8.setOnCheckedChangeListener(null);
                switch_buttonmap_loop6.setOnCheckedChangeListener(null);
                switch_buttonmap_loop7.setOnCheckedChangeListener(null);
                switch_buttonmap_loop8.setOnCheckedChangeListener(null);
                        if (isChecked) {
                            for (int i = 0; i < li.size(); i++) {
                                li.get(i).setChecked(true);
                            }
                            String[] S_RegPackage = numb.split(",");
                            for (int i = 0; i <S_RegPackage.length ; i++) {
                                oPenOrClose("1,2,3,4,5,6,7,8,","01",S_RegPackage[i],orgid);
                            }
                        } else {
                            for (int i = 0; i < li.size(); i++) {
                                li.get(i).setChecked(false);
                            }
                            String[] S_RegPackage = numb.split(",");
                            for (int i = 0; i <S_RegPackage.length ; i++) {
                                oPenOrClose("1,2,3,4,5,6,7,8,","00",S_RegPackage[i],orgid);
                            }
                        }
            }
        });
        // 1
        switch_buttonmap_loop1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("1","01",S_RegPackage[i],orgid);
                    }
                }else{
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("1","00",S_RegPackage[i],orgid);
                    }
                }
            }
        });
        // 1
        switch_buttonmap_loop2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("2","01",S_RegPackage[i],orgid);
                    }
                }else{
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("2","00",S_RegPackage[i],orgid);
                    }
                }
            }
        });
        // 1
        switch_buttonmap_loop3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("3","01",S_RegPackage[i],orgid);
                    }
                }else{
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("3","00",S_RegPackage[i],orgid);
                    }
                }
            }
        });
        // 1
        switch_buttonmap_loop4.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("4","01",S_RegPackage[i],orgid);
                    }
                }else{
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("4","00",S_RegPackage[i],orgid);
                    }
                }
            }
        });
        // 1
        switch_buttonmap_loop5.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("5","01",S_RegPackage[i],orgid);
                    }
                }else{
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("5","00",S_RegPackage[i],orgid);
                    }
                }
            }
        });
        // 1
        switch_buttonmap_loop6.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("6","01",S_RegPackage[i],orgid);
                    }
                }else{
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("6","00",S_RegPackage[i],orgid);
                    }
                }
            }
        });
        // 1
        switch_buttonmap_loop7.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("7","01",S_RegPackage[i],orgid);
                    }
                }else{
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("7","00",S_RegPackage[i],orgid);
                    }
                }
            }
        });
        // 1
        switch_buttonmap_loop8.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("8","01",S_RegPackage[i],orgid);
                    }
                }else{
                    String[] S_RegPackage = numb.split(",");
                    for (int i = 0; i <S_RegPackage.length ; i++) {
                        //开启回路
                        oPenOrClose("8","00",S_RegPackage[i],orgid);
                    }
                }
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                activity_one_lamp_c.setBackgroundResource(R.color.huise);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

    }


    //开启或者关闭回路
    private void oPenOrClose(final String loop, final String openOrClose, final String S_RegPackage, final String SL_Organize_S_Id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String loopNumbString = GsonUtil.getLamp(loop);
                String value = S_RegPackage + "," + openOrClose + "," + loopNumbString + "," + SL_Organize_S_Id;
                LoopHttp.OpenOrCloseLoop(value);
            }
        }).start();
    }
}
