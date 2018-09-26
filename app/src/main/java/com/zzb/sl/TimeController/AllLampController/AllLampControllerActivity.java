package com.zzb.sl.TimeController.AllLampController;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zzb.CustomView.LampControlView;
import com.zzb.bean.Host;
import com.zzb.bean.Lampj;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.HostHttp;
import com.zzb.http.LampHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.sl.TimeController.AllLampController.AllLampCheck.adapter.ListAdapter;
import com.zzb.sl.TimeController.AllLampController.AllLampCheck.bean.SelectEvent;
import com.zzb.sl.TimeController.AllLampController.AllLampCheck.widget.OnStartDragListener;
import com.zzb.sl.TimeController.AllLampController.AllLampCheck.widget.SimpleItemTouchHelperCallback;
import com.zzb.sl.TimeController.OneLampController.OneLampCActivity;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AllLampControllerActivity extends SwipeBackActivity implements OnStartDragListener, View.OnClickListener {

    private static final String TAG ="AllLampControllerActivity" ;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
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
    public static PopupWindow mPopupWindow;
    private RelativeLayout activity_all_lamp_controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lamp_controller);
        recyclerView = (RecyclerView) findViewById(R.id.alllamp_rlv);
        checkbox = (CheckBox) findViewById(R.id.alllamp_list_checkbox);
        selected = (TextView) findViewById(R.id.alllamp_list_selected);
        sfl = (SwipeRefreshLayout) findViewById(R.id.Alllamp_srfl);
        activity_all_lamp_controller = (RelativeLayout) findViewById(R.id.activity_all_lamp_controller);
        findViewById(R.id.Alllamp_list_img2).setOnClickListener(this);
        findViewById(R.id.Alllamp_list_img3).setOnClickListener(this);
        event = EventBus.getDefault();
        event.register(this);
        ButterKnife.bind(this);
        User user = TotalUrl.getUser();
        int userQ = user.getdate().getSL_USER_RANKID();
        if (userQ == 2){
            getData();
            sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    hostList.clear();
                    getData();
                    sfl.setRefreshing(false);
                }
            });
        }else{
            getData2();
            sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    hostList.clear();
                    getData2();
                    sfl.setRefreshing(false);
                }
            });
        }
    }
    //获取数据
    private void getData(){
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String userPro = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询项目信息
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null || organList.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AllLampControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
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
                            ActivityUtil.showToasts(AllLampControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
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
                    if (pro.equals(userPro)) {
                        for (int j = 0; j < hoseLists.size(); j++) {
                            String org = hoseLists.get(j).getsSL_Organize_S_Id();
                            if (org.equals(proID)) {
                                hostList.add(hoseLists.get(j));
                            }
                        }
                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ListAdapter adapter = new ListAdapter(hostList, AllLampControllerActivity.this, event);
                        recyclerView.setAdapter(adapter);
                        // recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                        recyclerView.setLayoutManager(new LinearLayoutManager(AllLampControllerActivity.this));

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

                                    for (int i = 0, p = hostList.size(); i < p; i++) {
                                        if (isChecked) {
                                            map.put(hostList.get(i).getsS_RegPackage(), true);

                                            count++;
                                            as += hostList.get(i).getsS_RegPackage() + ",";
                                        } else {
                                            if (!isChange) {
                                                map.put(hostList.get(i).getsS_RegPackage(), false);
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
                            public void onItemClick(RecyclerView.ViewHolder holder, int position) {

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
                return;
            }
        }).start();
    }
    //获取数据
    private void getData2(){
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String userPro = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询项目信息
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null || organList.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AllLampControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
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
                            ActivityUtil.showToasts(AllLampControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
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
                if (selectUList1 == null || selectUList1.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AllLampControllerActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                if(selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() ==""){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(AllLampControllerActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                String [] proList;
                if(selectUList1.get(0).getsS_Project().contains(",")){
                    proList = selectUList1.get(0).getsS_Project().split(",");
                }else{
                    proList = (selectUList1.get(0).getsS_Project()+",").split(",");
                }

                for (int a = 0; a <proList.length ; a++) {
                    for (int i = 0; i <organList.size() ; i++) {
                        if (proList[a].equals(organList.get(i).getsS_Id())){
                            String proID = organList.get(i).getsS_Id();
                            for (int j = 0; j < hoseLists.size(); j++) {
                                String userOrg = hoseLists.get(j).getsSL_Organize_S_Id();
                                if (userOrg.equals(proID)) {
                                    hostList.add(hoseLists.get(j));
                                }
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ListAdapter adapter = new ListAdapter(hostList, AllLampControllerActivity.this, event);
                        recyclerView.setAdapter(adapter);
                        // recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                        recyclerView.setLayoutManager(new LinearLayoutManager(AllLampControllerActivity.this));

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

                                    for (int i = 0, p = hostList.size(); i < p; i++) {
                                        if (isChecked) {
                                            map.put(hostList.get(i).getsS_RegPackage(), true);

                                            count++;
                                            as += hostList.get(i).getsS_RegPackage() + ",";
                                        } else {
                                            if (!isChange) {
                                                map.put(hostList.get(i).getsS_RegPackage(), false);
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
                            public void onItemClick(RecyclerView.ViewHolder holder, int position) {

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
        if (size < hostList.size()) {
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
        if (isPause){ //判断是否暂停
            User user = TotalUrl.getUser();
            int userQ = user.getdate().getSL_USER_RANKID();
            if (userQ == 2){
                isPause = false;
                hostList.clear();
                getData();
            }else {
                isPause = false;
                hostList.clear();
                getData2();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Alllamp_list_img2:
                finish();
                break;
            case R.id.Alllamp_list_img3:

                if (map == null || map.size() < 1) {
                    ActivityUtil.showToasts(AllLampControllerActivity.this, R.string.PleaseSelectData, 1 * 1000);
                    break;
                }
                String numb = "";
                for (String in : map.keySet()) {
                    boolean str = map.get(in);
                    if (str) {
                        numb += in + ",";
                    }
                }
                if (numb == null ||numb == ""){
                    break;
                }
                String[] S_SId = numb.split(",");
                //belowwindows(S_SId);
                belowwindows2(S_SId);
                break;

        }
    }

    //底部弹框
    public void belowwindows(final String [] S_SId) {
        activity_all_lamp_controller.setBackgroundResource(R.color.colorTextGary);
        LampControlView tempControl;
        recyclerView.setVisibility(View.INVISIBLE);
        //设置布局
        View popView = LayoutInflater.from(AllLampControllerActivity.this).inflate(R.layout.activity_onelamppw, null);
        //设置宽高
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置允许在外点击消失
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //设置背景颜色
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x30000000));
        //设置动画
        mPopupWindow.setAnimationStyle(R.style.Animation_Button_Dialog);
        //参数1:根视图，整个Window界面的最顶层View 参数2:显示位置
        mPopupWindow.showAtLocation(AllLampControllerActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tempControl = (LampControlView) popView.findViewById(R.id.temp_control);
        tempControl.setOnTempChangeListener(new LampControlView.OnTempChangeListener() {
            @Override
            public void change(int temp) {
                for (int i = 0; i <S_SId.length ; i++) {
                    for (int j = 0; j <hostList.size() ; j++) {
                        if (S_SId[i].equals(hostList.get(j).getsS_RegPackage())){
                            //注册包
                            String S_RegPackage = hostList.get(j).getsS_RegPackage();
                            //项目
                            String org = hostList.get(j).getsSL_Organize_S_Id();
                            //调光端口
                            String S_LightHD = "3";
                            final String value = S_RegPackage+","+S_LightHD+","+temp+","+","+org;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    LampHttp.sendAllLamp(value);
                                }
                            }).start();
                        }
                    }
                }
            }
        });
        tempControl.setOnSlideListener(new LampControlView.OnSlideListener() {
            @Override
            public void Slide(int temp) {

            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                activity_all_lamp_controller.setBackgroundResource(R.color.huise);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }









    //底部弹框
    public void belowwindows2(final String [] S_SId) {
        activity_all_lamp_controller.setBackgroundResource(R.color.colorTextGary);
        recyclerView.setVisibility(View.INVISIBLE);
        final SeekBar tempControl;
        final TextView huadong;
        Button button1,button2;
        //设置布局
        View popView = LayoutInflater.from(AllLampControllerActivity.this).inflate(R.layout.activity_onelamppw22, null);
        //设置宽高
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置允许在外点击消失
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //设置背景颜色
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x30000000));
        //设置动画
        mPopupWindow.setAnimationStyle(R.style.Animation_Button_Dialog);
        //参数1:根视图，整个Window界面的最顶层View 参数2:显示位置
        mPopupWindow.showAtLocation(AllLampControllerActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tempControl = (SeekBar) popView.findViewById(R.id.temp_control);
        huadong = (TextView) popView.findViewById(R.id.huadong);
        button1 = (Button) popView.findViewById(R.id.btn_1_close);
        button2 = (Button) popView.findViewById(R.id.btn_1_open);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huadong.setText(""+0);
                tempControl.setProgress(0);
                for (int i = 0; i <S_SId.length ; i++) {
                    for (int j = 0; j <hostList.size() ; j++) {
                        if (S_SId[i].equals(hostList.get(j).getsS_RegPackage())){
                            //注册包
                            String S_RegPackage = hostList.get(j).getsS_RegPackage();
                            //项目
                            String org = hostList.get(j).getsSL_Organize_S_Id();
                            //调光端口
                            String S_LightHD = "3";
                            final String value = S_RegPackage+","+S_LightHD+","+"0"+","+","+org;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    LampHttp.sendAllLamp(value);
                                }
                            }).start();
                        }
                    }
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huadong.setText(""+100);
                tempControl.setProgress(100);
                for (int i = 0; i <S_SId.length ; i++) {
                    for (int j = 0; j <hostList.size() ; j++) {
                        if (S_SId[i].equals(hostList.get(j).getsS_RegPackage())){
                            //注册包
                            String S_RegPackage = hostList.get(j).getsS_RegPackage();
                            //项目
                            String org = hostList.get(j).getsSL_Organize_S_Id();
                            //调光端口
                            String S_LightHD = "3";
                            final String value = S_RegPackage+","+S_LightHD+","+"100"+","+","+org;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    LampHttp.sendAllLamp(value);
                                }
                            }).start();
                        }
                    }
                }
            }
        });
        tempControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //当进度改变时，参数fromUser判断是不是进度的改变由用户手动引起
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                huadong.setText(""+progress);
                Log.e(TAG, "progress:"+progress);
            }
            //当用户开始滑动时
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "开始滑动");
            }
            //滑动结束
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                final String t = huadong.getText().toString();
                for (int i = 0; i <S_SId.length ; i++) {
                    for (int j = 0; j <hostList.size() ; j++) {
                        if (S_SId[i].equals(hostList.get(j).getsS_RegPackage())){
                            //注册包
                            String S_RegPackage = hostList.get(j).getsS_RegPackage();
                            //项目
                            String org = hostList.get(j).getsSL_Organize_S_Id();
                            //调光端口
                            String S_LightHD = "3";
                            final String value = S_RegPackage+","+S_LightHD+","+t+","+","+org;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    LampHttp.sendAllLamp(value);
                                }
                            }).start();
                        }
                    }
                }

            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                activity_all_lamp_controller.setBackgroundResource(R.color.huise);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }







}
