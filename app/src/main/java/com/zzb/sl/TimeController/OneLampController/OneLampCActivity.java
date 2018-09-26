package com.zzb.sl.TimeController.OneLampController;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.bean.Lampj;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.LampHttp;
import com.zzb.http.SubHttp;
import com.zzb.sl.R;
import com.zzb.sl.TimeController.OneLampController.OneLampCheck.adapter.ListAdapter;
import com.zzb.sl.TimeController.OneLampController.OneLampCheck.bean.SelectEvent;
import com.zzb.sl.TimeController.OneLampController.OneLampCheck.widget.OnStartDragListener;
import com.zzb.sl.TimeController.OneLampController.OneLampCheck.widget.SimpleItemTouchHelperCallback;
import com.zzb.util.ActivityUtil;
import com.zzb.util.LinearLayoutManagerWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OneLampCActivity extends SwipeBackActivity implements OnStartDragListener, View.OnClickListener {
    private static final String TAG = "OneLampCActivity";
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private Host host = new Host();
    private List<Lampj> lampjList = new ArrayList<>();


    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView recyclerView;
    private CheckBox checkbox;
    private TextView selected;
    private EventBus event;
    HashMap<String, Boolean> map = new HashMap<>();
    private boolean isChange = false;
    private SwipeRefreshLayout sfl;
    private boolean isPause;
    private RelativeLayout activity_one_lamp_c;
    public static PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_lamp_c);
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_ONELAMOHOST");
        recyclerView = (RecyclerView) findViewById(R.id.onelampc_list_recyclerview);
        checkbox = (CheckBox) findViewById(R.id.onelampc_list_checkbox);
        selected = (TextView) findViewById(R.id.onelampc_list_selected);
        sfl = (SwipeRefreshLayout) findViewById(R.id.onelampc_list_srfl);
        findViewById(R.id.onelampc_list_img2).setOnClickListener(this);
        findViewById(R.id.onelampc_list_img3).setOnClickListener(this);
        activity_one_lamp_c = (RelativeLayout) findViewById(R.id.activity_one_lamp_c);
        event = EventBus.getDefault();
        event.register(this);
        ButterKnife.bind(this);
        getData();
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selected.setText("");
                map.clear();
                //初始化
                lampjList.clear();
                getData();
                sfl.setRefreshing(false);
            }
        });
    }
    //查询信息
    private void getData(){
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        String userPro = user.getdate().getOrganizeId();
        final String pack = host.getsS_RegPackage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Lampj> lampjL = LampHttp.getHostLamp(pack, user);
                if (lampjL == null || lampjL.size() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(OneLampCActivity.this, R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }

                for (int l = 0; l < lampjL.size(); l++) {
                    //之后修改时间
                    String times = lampjL.get(l).getsS_UpdateTime();
                    String tm1 = times.replaceAll("T", " ");
                    String tmmm;
                    if( tm1.indexOf(".") != -1){
                        Log.e(TAG,"出错tm1:"+tm1);
                        tmmm = tm1.substring(0, tm1.indexOf("."));
                        Log.e(TAG,"出错tmmm:"+tmmm);

                    }else{
                        tmmm = tm1;
                        Log.e(TAG,"tmmm:"+tmmm);
                    }
                    Log.e(TAG,"最后修改时间:"+tmmm);
                    //获取系统当前时间
                    Date day = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String times2 = df.format(day);
                    Log.e(TAG,"系统当前时间:" + times2);
                    try {
                        long m = df.parse(times2).getTime() - df.parse(tmmm).getTime();

                        Log.e(TAG,"相差毫秒数:" + m);
                        Log.e(TAG,"相差分钟数:" + (m / (1000 * 60)));
                        long m1 = (m / (1000 * 60));
                        Log.e(TAG,"相差天数:" + (m / (1000 * 60 * 60 * 24)));
                        if (m1 > 30) {
                            Log.e(TAG,"灯光不在线:");
                            lampjL.get(l).setsS_line("0");
                        } else {
                            Log.e(TAG,"灯光在线:");
                            lampjL.get(l).setsS_line("1");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    lampjList.add(lampjL.get(l));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ListAdapter adapter = new ListAdapter(lampjList, OneLampCActivity.this, event);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(OneLampCActivity.this,LinearLayoutManager.VERTICAL, false));
                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                        mItemTouchHelper = new ItemTouchHelper(callback);

                        mItemTouchHelper.attachToRecyclerView(recyclerView);
                        checkbox.setChecked(true);
                        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                try {
                                    int count = 0;
                                    if (isChecked) {
                                        isChange = false;
                                    }
                                    for (int i = 0, p = lampjList.size(); i < p; i++) {
                                        if (isChecked) {
                                            map.put(lampjList.get(i).getsS_Id(), true);
                                            count++;
                                        } else {
                                            if (!isChange) {
                                                map.put(lampjList.get(i).getsS_Id(), false);
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
                            public void onItemClick(RecyclerView.ViewHolder holder, int positon) {
                                Log.e("onItemClick", "" + positon);
                                Lampj lampj = lampjList.get(positon);
                                Intent intent = new Intent(OneLampCActivity.this, OneLampInfoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_ONELAMPJAMPJ", lampj);
                                bundle.putSerializable("SELECTUSER_ONELAMPJAMPJHOST", host);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(final RecyclerView.ViewHolder holder, final int positon) {
                                Log.e("onItemLongClick", "" + positon);

                            }
                        });
                        //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler.sendMessage(msg_listData);
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
        if (size < lampjList.size()) {
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


/*    @Override
    protected void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isPause){ //判断是否暂停
            isPause = false;
            lampjList.clear();
            selected.setText("");
            map.clear();
            getData();
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.onelampc_list_img2:
                finish();
                break;
            case R.id.onelampc_list_img3:
                if (map == null || map.size() < 1) {
                    ActivityUtil.showToasts(OneLampCActivity.this, R.string.PleaseSelectData, 1 * 1000);
                  break;
                }

                String numb = "";
                String sS_RegPackage=host.getsS_RegPackage();
                String orgprj = host.getsSL_Organize_S_Id();
                for (String in : map.keySet()) {
                    boolean str = map.get(in);
                    if (str) {
                        numb += in + ",";
                        Log.e(TAG, "获取数据:" + numb);
                    }
                }
                if (numb == null ||numb == ""){
                    break;
                }
                String[] S_SId = numb.split(",");
              /*  if (S_SId.length > 10){
                    ActivityUtil.showToasts(OneLampCActivity.this, R.string.Not5, 1 * 1000);
                    break;
                }*/
                belowwindows2(S_SId,sS_RegPackage,orgprj);
               //belowwindows(S_SId,sS_RegPackage,orgprj);
                break;
        }
    }






    //底部弹框
    public void belowwindows(final String [] S_SId, final String sS_RegPackage, final String orgprj) {
        activity_one_lamp_c.setBackgroundResource(R.color.colorTextGary);
        recyclerView.setVisibility(View.INVISIBLE);
        LampControlView tempControl;
        //设置布局
        View popView = LayoutInflater.from(OneLampCActivity.this).inflate(R.layout.activity_onelamppw, null);
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
        mPopupWindow.showAtLocation(OneLampCActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tempControl = (LampControlView) popView.findViewById(R.id.temp_control);
        tempControl.setOnTempChangeListener(new LampControlView.OnTempChangeListener() {
            @Override
            public void change(final int temp) {
                final List<Lampj> lampjList2 = new ArrayList<>();
                for (int i = 0; i < S_SId.length; i++) {
                    String id = S_SId[i];
                    for (int j = 0; j <lampjList.size() ; j++) {
                        if (id.equals(lampjList.get(j).getsS_Id())){
                            lampjList2.add(lampjList.get(j));
                        }
                    }
                }
                Collections.sort(lampjList2, new Comparator<Lampj>() {
                    @Override
                    public int compare(Lampj o1, Lampj o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });
                for(Object o : lampjList2){
                    Log.e(TAG,"排序:"+o);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <lampjList2.size() ; i++) {
                            final String value = sS_RegPackage+","+lampjList2.get(i).getsS_Number()+","+lampjList2.get(i).getsS_LightHD()+","+temp+","+orgprj;
                            boolean b = LampHttp.sendOneLamp(value);
                            if(b == false){
                                LampHttp.sendOneLamp(value);
                            }
                        }
                    }
                }).start();
            }
        });
        tempControl.setOnSlideListener(new LampControlView.OnSlideListener() {
            @Override
            public void Slide(int temp) {
                Log.e(TAG,"temp:"+temp);
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
















    //底部弹框
    public void belowwindows2(final String [] S_SId, final String sS_RegPackage, final String orgprj) {
        activity_one_lamp_c.setBackgroundResource(R.color.colorTextGary);
        recyclerView.setVisibility(View.INVISIBLE);
        final SeekBar tempControl;
        final TextView huadong;
        Button button1,button2;
        //设置布局
        View popView = LayoutInflater.from(OneLampCActivity.this).inflate(R.layout.activity_onelamppw22, null);
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
        mPopupWindow.showAtLocation(OneLampCActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tempControl = (SeekBar) popView.findViewById(R.id.temp_control);
        huadong = (TextView) popView.findViewById(R.id.huadong);
        button1 = (Button) popView.findViewById(R.id.btn_1_close);
        button2 = (Button) popView.findViewById(R.id.btn_1_open);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huadong.setText(""+0);
                tempControl.setProgress(0);
                final List<Lampj> lampjList2 = new ArrayList<>();
                for (int i = 0; i < S_SId.length; i++) {
                    String id = S_SId[i];
                    for (int j = 0; j <lampjList.size() ; j++) {
                        if (id.equals(lampjList.get(j).getsS_Id())){
                            lampjList2.add(lampjList.get(j));
                        }
                    }
                }
                Collections.sort(lampjList2, new Comparator<Lampj>() {
                    @Override
                    public int compare(Lampj o1, Lampj o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });
                for(Object o : lampjList2){
                    Log.e(TAG,"排序:"+o);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <lampjList2.size() ; i++) {
                            final String value = sS_RegPackage+","+lampjList2.get(i).getsS_Number()+","+lampjList2.get(i).getsS_LightHD()+","+"0"+","+orgprj;
                            boolean b = LampHttp.sendOneLamp(value);
                            if(b == false){
                                LampHttp.sendOneLamp(value);
                            }
                        }
                    }
                }).start();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huadong.setText(""+100);
                tempControl.setProgress(100);
                final List<Lampj> lampjList2 = new ArrayList<>();
                for (int i = 0; i < S_SId.length; i++) {
                    String id = S_SId[i];
                    for (int j = 0; j <lampjList.size() ; j++) {
                        if (id.equals(lampjList.get(j).getsS_Id())){
                            lampjList2.add(lampjList.get(j));
                        }
                    }
                }
                Collections.sort(lampjList2, new Comparator<Lampj>() {
                    @Override
                    public int compare(Lampj o1, Lampj o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });
                for(Object o : lampjList2){
                    Log.e(TAG,"排序:"+o);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <lampjList2.size() ; i++) {
                            final String value = sS_RegPackage+","+lampjList2.get(i).getsS_Number()+","+lampjList2.get(i).getsS_LightHD()+","+"100"+","+orgprj;
                            boolean b = LampHttp.sendOneLamp(value);
                            if(b == false){
                                LampHttp.sendOneLamp(value);
                            }
                        }
                    }
                }).start();
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
                final List<Lampj> lampjList2 = new ArrayList<>();
                for (int i = 0; i < S_SId.length; i++) {
                    String id = S_SId[i];
                    for (int j = 0; j <lampjList.size() ; j++) {
                        if (id.equals(lampjList.get(j).getsS_Id())){
                            lampjList2.add(lampjList.get(j));
                        }
                    }
                }
                Collections.sort(lampjList2, new Comparator<Lampj>() {
                    @Override
                    public int compare(Lampj o1, Lampj o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });
                for(Object o : lampjList2){
                    Log.e(TAG,"排序:"+o);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <lampjList2.size() ; i++) {
                            final String value = sS_RegPackage+","+lampjList2.get(i).getsS_Number()+","+lampjList2.get(i).getsS_LightHD()+","+t+","+orgprj;
                            boolean b = LampHttp.sendOneLamp(value);
                            if(b == false){
                                LampHttp.sendOneLamp(value);
                            }
                        }
                    }
                }).start();
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





}
