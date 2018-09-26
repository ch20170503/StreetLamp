package com.zzb.sl.TimeController.GroupController;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.zzb.bean.Group;
import com.zzb.bean.Host;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.GroupHttp;
import com.zzb.sl.R;
import com.zzb.sl.TimeController.GroupController.GroupCcheck.adapter.ListAdapter;
import com.zzb.sl.TimeController.GroupController.GroupCcheck.bean.SelectEvent;
import com.zzb.sl.TimeController.GroupController.GroupCcheck.widget.OnStartDragListener;
import com.zzb.sl.TimeController.GroupController.GroupCcheck.widget.SimpleItemTouchHelperCallback;
import com.zzb.util.ActivityUtil;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GrouplistConActivity extends SwipeBackActivity implements OnStartDragListener, View.OnClickListener {
    private static final String TAG ="GrouplistConActivity" ;
    private Host host = new Host();
    private boolean isPause;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private List<Group> grouplist = new ArrayList<>();

    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView recyclerView;
    private CheckBox checkbox;
    private TextView selected;
    private EventBus event;
    HashMap<Integer, Boolean> map;
    private boolean isChange = false;
    private  SwipeRefreshLayout group_con_srfl;
    public static PopupWindow mPopupWindow;
    private RelativeLayout activity_group_info_con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_con);
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_ONEGROUPHOSTC");
        recyclerView = (RecyclerView) findViewById(R.id.group_con_recyclerview);
        checkbox = (CheckBox) findViewById(R.id.group_con_checkbox);
        selected = (TextView) findViewById(R.id.group_con_selected);
        group_con_srfl = (SwipeRefreshLayout) findViewById(R.id.group_con_srfl);
        activity_group_info_con = (RelativeLayout) findViewById(R.id.activity_group_info_con);
        findViewById(R.id.group_con_list_img2).setOnClickListener(this);
        findViewById(R.id.group_con_list_img3).setOnClickListener(this);
        event = EventBus.getDefault();
        event.register(this);
        ButterKnife.bind(this);
        getData();
        group_con_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //初始化
                grouplist.clear();
                getData();
                group_con_srfl.setRefreshing(false);
            }
        });
    }


    //获取数据
    private void getData(){
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String pack = host.getsS_RegPackage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Group> groups = GroupHttp.selectOrPackG(user,pack);
                if (groups == null || groups.size() <1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GrouplistConActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                for (int i = 0; i <groups.size() ; i++) {
                    grouplist.add(groups.get(i));
                }
                Collections.sort(grouplist, new Comparator<Group>() {
                    @Override
                    public int compare(Group o1, Group o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ListAdapter adapter = new ListAdapter(grouplist, GrouplistConActivity.this, event);
                        recyclerView.setAdapter(adapter);
                        // recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                        recyclerView.setLayoutManager(new LinearLayoutManager(GrouplistConActivity.this));

                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                        mItemTouchHelper = new ItemTouchHelper(callback);
                        mItemTouchHelper.attachToRecyclerView(recyclerView);
                        checkbox.setChecked(true);
                        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                map = new HashMap<Integer, Boolean>();
                                try {
                                    int count = 0;
                                    if (isChecked) {
                                        isChange = false;
                                    }

                                    for (int i = 0, p = grouplist.size(); i < p; i++) {
                                        if (isChecked) {
                                            map.put(grouplist.get(i).getsS_Number(), true);

                                            count++;

                                        } else {
                                            if (!isChange) {
                                                map.put(grouplist.get(i).getsS_Number(), false);
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
                                Group group = grouplist.get(position);
                                Intent intent = new Intent();
                                intent.setClass(GrouplistConActivity.this, GroupControllerInfoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_ALLGROUPINFOCONTR", group);
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
        if (size < grouplist.size()) {
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

/*
    @Override
    protected void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isPause){ //判断是否暂停
                isPause = false;
                grouplist.clear();
                getData();
        }
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_con_list_img2:
                finish();
                break;
            case R.id.group_con_list_img3:
                if (map == null || map.size() < 1) {
                    ActivityUtil.showToasts(GrouplistConActivity.this, R.string.PleaseSelectData, 1 * 1000);
                    break;
                }
                String numb = "";
                for (Integer in : map.keySet()) {
                    boolean str = map.get(in);
                    if (str) {
                        numb += in + ",";
                    }
                }
                if (numb == null ||numb == ""){
                    break;
                }
                String[] S_SId = numb.split(",");
               // belowwindows(S_SId);
                belowwindows2(S_SId);
                break;
        }
    }
    //底部弹框
    public void belowwindows(final String [] S_SId) {
        activity_group_info_con.setBackgroundResource(R.color.colorTextGary);
        recyclerView.setVisibility(View.INVISIBLE);
        LampControlView tempControl;
        final WheelView wv;
        List<String> valueList = new ArrayList<>();
        valueList.add("1");
        valueList.add("2");
        valueList.add("3");
        //设置布局
        View popView = LayoutInflater.from(GrouplistConActivity.this).inflate(R.layout.activity_allgrouppw, null);
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
        mPopupWindow.showAtLocation(GrouplistConActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tempControl = (LampControlView) popView.findViewById(R.id.tempg_control22);
        wv = (WheelView) popView.findViewById(R.id.whev);
        wv.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wv.setSkin(WheelView.Skin.Common); // common皮肤
        wv.setWheelData(valueList);  // 数据集合.
        Log.e(TAG,"wv.getSelectionItem():"+wv.getSelectionItem());
        Log.e(TAG,"getSelection"+wv.getSelection());
        tempControl.setOnTempChangeListener(new LampControlView.OnTempChangeListener() {
            @Override
            public void change(int temp) {
                Log.e(TAG,"wv.getSelectionItem():"+wv.getSelectionItem());
                Log.e(TAG,"S_SId:"+S_SId.toString());
                //端口
                String S_LightHD = wv.getSelectionItem().toString();
                for (int i = 0; i <S_SId.length ; i++) {
                    for (int j = 0; j <grouplist.size() ; j++) {
                        if (S_SId[i].equals((grouplist.get(j).getsS_Number()+""))){
                            //注册包
                            String S_RegPackage = grouplist.get(j).getsSL_HostBase_S_RegPackage();
                            //项目
                            String org = grouplist.get(j).getsSL_Organize_S_Id();
                            String S_numb =  grouplist.get(j).getsS_Number()+"";
                            final String value = S_RegPackage+","+S_LightHD+","+temp+","+S_numb+","+org;
                            Log.e(TAG,"value:"+value);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    GroupHttp.sendOneGroup(value);
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
                activity_group_info_con.setBackgroundResource(R.color.huise);
                recyclerView.setVisibility(View.VISIBLE);

            }
        });
    }
    //底部弹框
    public void belowwindows2(final String [] S_SId) {
        activity_group_info_con.setBackgroundResource(R.color.colorTextGary);
        recyclerView.setVisibility(View.INVISIBLE);
        final SeekBar tempControl;
        final TextView huadong22;
        final WheelView wv;
        Button button1,button2;
        List<String> valueList = new ArrayList<>();
        valueList.add("1");
        valueList.add("2");
        valueList.add("3");
        //设置布局
        View popView = LayoutInflater.from(GrouplistConActivity.this).inflate(R.layout.activity_allgrouppw22, null);
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
        mPopupWindow.showAtLocation(GrouplistConActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tempControl = (SeekBar) popView.findViewById(R.id.tempg_control);
        huadong22= (TextView) popView.findViewById(R.id.huadong22);
        wv = (WheelView) popView.findViewById(R.id.whev);
        button1 = (Button) popView.findViewById(R.id.btn_1_closeag);
        button2 = (Button) popView.findViewById(R.id.btn_1_openag);
        wv.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wv.setSkin(WheelView.Skin.Common); // common皮肤
        wv.setWheelData(valueList);  // 数据集合.
        Log.e(TAG,"wv.getSelectionItem():"+wv.getSelectionItem());
        Log.e(TAG,"getSelection"+wv.getSelection());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huadong22.setText(""+0);
                tempControl.setProgress(0);
                //端口
                String S_LightHD = wv.getSelectionItem().toString();
                for (int i = 0; i <S_SId.length ; i++) {
                    for (int j = 0; j <grouplist.size() ; j++) {
                        if (S_SId[i].equals((grouplist.get(j).getsS_Number()+""))){
                            //注册包
                            String S_RegPackage = grouplist.get(j).getsSL_HostBase_S_RegPackage();
                            //项目
                            String org = grouplist.get(j).getsSL_Organize_S_Id();
                            String S_numb =  grouplist.get(j).getsS_Number()+"";
                            final String value = S_RegPackage+","+S_LightHD+","+"0"+","+S_numb+","+org;
                            Log.e(TAG,"value:"+value);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    GroupHttp.sendOneGroup(value);
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
                huadong22.setText(""+100);
                tempControl.setProgress(100);
                //端口
                String S_LightHD = wv.getSelectionItem().toString();
                for (int i = 0; i <S_SId.length ; i++) {
                    for (int j = 0; j <grouplist.size() ; j++) {
                        if (S_SId[i].equals((grouplist.get(j).getsS_Number()+""))){
                            //注册包
                            String S_RegPackage = grouplist.get(j).getsSL_HostBase_S_RegPackage();
                            //项目
                            String org = grouplist.get(j).getsSL_Organize_S_Id();
                            String S_numb =  grouplist.get(j).getsS_Number()+"";
                            final String value = S_RegPackage+","+S_LightHD+","+"100"+","+S_numb+","+org;
                            Log.e(TAG,"value:"+value);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    GroupHttp.sendOneGroup(value);
                                }
                            }).start();
                        }
                    }
                }
            }
        });
        tempControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                huadong22.setText(""+progress);
                Log.e(TAG, "progress:"+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "开始滑动");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                final String t = huadong22.getText().toString();
                Log.e(TAG,"wv.getSelectionItem():"+wv.getSelectionItem());
                Log.e(TAG,"S_SId:"+S_SId.toString());
                //端口
                String S_LightHD = wv.getSelectionItem().toString();
                for (int i = 0; i <S_SId.length ; i++) {
                    for (int j = 0; j <grouplist.size() ; j++) {
                        if (S_SId[i].equals((grouplist.get(j).getsS_Number()+""))){
                            //注册包
                            String S_RegPackage = grouplist.get(j).getsSL_HostBase_S_RegPackage();
                            //项目
                            String org = grouplist.get(j).getsSL_Organize_S_Id();
                            String S_numb =  grouplist.get(j).getsS_Number()+"";
                            final String value = S_RegPackage+","+S_LightHD+","+t+","+S_numb+","+org;
                            Log.e(TAG,"value:"+value);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    GroupHttp.sendOneGroup(value);
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
                activity_group_info_con.setBackgroundResource(R.color.huise);
                recyclerView.setVisibility(View.VISIBLE);

            }
        });
    }





}
