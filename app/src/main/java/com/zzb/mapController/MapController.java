package com.zzb.mapController;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.zzb.CustomView.LampControlView;
import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.bean.HostDataSet;
import com.zzb.bean.Lampj;
import com.zzb.http.LampHttp;
import com.zzb.http.LoopHttp;
import com.zzb.sl.R;
import com.zzb.util.GsonUtil;
import com.suke.widget.SwitchButton;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapController extends AppCompatActivity implements AMap.OnMarkerClickListener {

    private static final String TAG = "MapController";
    private MapView mMapView;
    private AMap mAMap;
    private LatLng latLng;
    private MarkerOptions markerOptions;
    private List<ControlS> contrlist = new ArrayList<>();
    private List<Lampj> lamplist = new ArrayList<>();
    private List<Host> hostList = new ArrayList<>();
    private List<HostDataSet> hostdsList = new ArrayList<>();
    public static PopupWindow mPopupWindow;
    public static PopupWindow mPopupWindows;
    private RelativeLayout activity_group_info_con;
    private int tag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_controller);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        init();
        //获取数据
        Intent intent = getIntent();
        contrlist = (List<ControlS>) intent.getSerializableExtra("MAPSUB");
        lamplist = (List<Lampj>) intent.getSerializableExtra("MAPLAMP");
        hostList = (List<Host>) intent.getSerializableExtra("MAPHOST");
        hostdsList = (List<HostDataSet>) intent.getSerializableExtra("MAPHOSTDS");
        addMarkersToMap();
    }

    private void init() {
        if (mAMap == null) {
            // 初始化地图
            mAMap = mMapView.getMap();
            boolean b = isZh(this);
            if (b == false) {
                mAMap.setMapLanguage("en");
            }

        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。


    }

    //获取当前系统语言
    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }


    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();

        mMapView.onDestroy();
    }

    //添加标记点
    private void addMarkersToMap() {
        for (int i = 0; i < hostList.size(); i++) {
            if (!hostList.get(i).getsS_Longitude().equals("-") && !hostList.get(i).getsS_Latitude().equals("-")) {
                String pack = hostList.get(i).getsS_RegPackage();
                for (int j = 0; j < hostdsList.size(); j++) {
                    if (pack.equals(hostdsList.get(j).getsS_RegPackage())) {
                        latLng = new LatLng(Double.parseDouble(hostList.get(i).getsS_Latitude()),Double.parseDouble(hostList.get(i).getsS_Longitude()));
                        markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        if (hostdsList.get(j).getsS_Online() == 1) {
                            markerOptions.title(hostList.get(i).getsS_FullName()).snippet("h"+","+hostList.get(i).getsS_RegPackage()+",\r\n"+hostList.get(i).getsSL_Organize_S_Id());
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.hostmap2)));
                            mAMap.addMarker(markerOptions);
                            mAMap.setOnMarkerClickListener(this);
                        } else {
                            markerOptions.title(hostList.get(i).getsS_FullName()).snippet("h"+","+"No");
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.hostmap2)));
                            mAMap.addMarker(markerOptions);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < contrlist.size(); i++) {
            if (!contrlist.get(i).getsS_Longitude().equals("-") && !contrlist.get(i).getsS_Latitude().equals("-")) {
                String pack = contrlist.get(i).getsSL_HostBast_RegPackage();
                for (int j = 0; j < hostdsList.size(); j++) {
                    if (pack.equals(hostdsList.get(j).getsS_RegPackage())) {
                        latLng = new LatLng( Double.parseDouble(contrlist.get(i).getsS_Latitude()),Double.parseDouble(contrlist.get(i).getsS_Longitude()));
                        markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        if(hostdsList.get(j).getsS_Online() == 1){
                            markerOptions.title(contrlist.get(i).getsS_FullName())
                                    .snippet("f"+","+String.valueOf(contrlist.get(i).getsS_Number()) + "," + contrlist.get(i).getsSL_HostBast_RegPackage());
                            tag = 3; //在线
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ring_592)));
                            mAMap.addMarker(markerOptions);
                        }else{
                            markerOptions.title(contrlist.get(i).getsS_FullName()).snippet("f"+","+"No");
                            tag = 4; //不在线
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ring_3x2)));
                            mAMap.addMarker(markerOptions);
                        }
                    }
                }

            }

        }
        mAMap.setOnMarkerClickListener(this);
    }
    //底部弹框(亮度)
    public void belowwindows(final String S_Number, final String RegPackage) {
        LampControlView tempControl;
        final WheelView wv;
        List<String> valueList = new ArrayList<>();
        valueList.add("1");
        valueList.add("2");
        valueList.add("3");
        //设置布局
        View popView = LayoutInflater.from(MapController.this).inflate(R.layout.activity_allgrouppw, null);
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
        mPopupWindow.showAtLocation(MapController.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tempControl = (LampControlView) popView.findViewById(R.id.tempg_control22);
        wv = (WheelView) popView.findViewById(R.id.whev);
        wv.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wv.setSkin(WheelView.Skin.Common); // common皮肤
        wv.setWheelData(valueList);  // 数据集合.
        Log.e(TAG, "wv.getSelectionItem():" + wv.getSelectionItem());
        Log.e(TAG, "getSelection" + wv.getSelection());
        tempControl.setOnTempChangeListener(new LampControlView.OnTempChangeListener() {
            @Override
            public void change(int temp) {
                Log.e(TAG, "wv.getSelectionItem():" + wv.getSelectionItem());
                //注册包
                String pack = RegPackage;
                //编号
                String numb = S_Number;
                //端口
                String S_LightHD = wv.getSelectionItem().toString();
                //亮度temp
                //机构
                String org = hostList.get(0).getsSL_Organize_S_Id();
                final String value = pack + "," + numb + "," + S_LightHD + "," + temp + "," + org;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LampHttp.sendOneLamp(value);
                    }
                }).start();
            }
        });
        tempControl.setOnSlideListener(new LampControlView.OnSlideListener() {
            @Override
            public void Slide(int temp) {

            }
        });
    }

    private List<SwitchButton> li;
    //回路
    public void loogC(final String S_RegPackage, final String SL_Organize_S_Id) {
        Log.e(TAG,"JINTAI");
        SwitchButton switch_buttonmap_all, switch_buttonmap_loop1, switch_buttonmap_loop2,
                switch_buttonmap_loop3, switch_buttonmap_loop4, switch_buttonmap_loop5,
                switch_buttonmap_loop6, switch_buttonmap_loop7, switch_buttonmap_loop8;
        li = new ArrayList<>();
        //设置布局
        View popView = LayoutInflater.from(MapController.this).inflate(R.layout.activity_maploop, null);
        //设置宽高
        mPopupWindows = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置允许在外点击消失
        mPopupWindows.setOutsideTouchable(true);
        mPopupWindows.setFocusable(true);
        //设置背景颜色
        mPopupWindows.setBackgroundDrawable(new ColorDrawable(0x30000000));
        //设置动画
        mPopupWindows.setAnimationStyle(R.style.Animation_Button_Dialog);
        //参数1:根视图，整个Window界面的最顶层View 参数2:显示位置
        mPopupWindows.showAtLocation(MapController.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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
        Log.e(TAG,"JINTAI");
        switch_buttonmap_all.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < li.size(); i++) {
                        li.get(i).setChecked(true);
                    }
                    oPenOrClose("1,2,3,4,5,6,7,8,","01",S_RegPackage,SL_Organize_S_Id);
                } else {
                    for (int i = 0; i < li.size(); i++) {
                        li.get(i).setChecked(false);
                    }
                    oPenOrClose("1,2,3,4,5,6,7,8,","00",S_RegPackage,SL_Organize_S_Id);
                }
            }
        });
        switch_buttonmap_loop1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    //开启回路
                    oPenOrClose("1,","01",S_RegPackage,SL_Organize_S_Id);
                }else{
                    //关闭回路
                    oPenOrClose("1,","00",S_RegPackage,SL_Organize_S_Id);
                }

            }
        });
        switch_buttonmap_loop2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    //开启回路
                    oPenOrClose("2,","01",S_RegPackage,SL_Organize_S_Id);
                }else{
                    //关闭回路
                    oPenOrClose("2,","00",S_RegPackage,SL_Organize_S_Id);
                }
            }
        });
        switch_buttonmap_loop3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    //开启回路
                    oPenOrClose("3,","01",S_RegPackage,SL_Organize_S_Id);
                }else{
                    //关闭回路
                    oPenOrClose("3,","00",S_RegPackage,SL_Organize_S_Id);
                }
            }
        });
        switch_buttonmap_loop4.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    //开启回路
                    oPenOrClose("4,","01",S_RegPackage,SL_Organize_S_Id);
                }else{
                    //关闭回路
                    oPenOrClose("4,","00",S_RegPackage,SL_Organize_S_Id);
                }
            }
        });
        switch_buttonmap_loop5.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    //开启回路
                    oPenOrClose("5,","01",S_RegPackage,SL_Organize_S_Id);
                }else{
                    //关闭回路
                    oPenOrClose("5,","00",S_RegPackage,SL_Organize_S_Id);
                }
            }
        });
        switch_buttonmap_loop6.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    //开启回路
                    oPenOrClose("6,","01",S_RegPackage,SL_Organize_S_Id);
                }else{
                    //关闭回路
                    oPenOrClose("6,","00",S_RegPackage,SL_Organize_S_Id);
                }
            }
        });
        switch_buttonmap_loop7.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    //开启回路
                    oPenOrClose("7,","01",S_RegPackage,SL_Organize_S_Id);
                }else{
                    //关闭回路
                    oPenOrClose("7,","00",S_RegPackage,SL_Organize_S_Id);
                }
            }
        });
        switch_buttonmap_loop8.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    //开启回路
                    oPenOrClose("8,","01",S_RegPackage,SL_Organize_S_Id);
                }else{
                    //关闭回路
                    oPenOrClose("8,","00",S_RegPackage,SL_Organize_S_Id);
                }
            }
        });
    }

    //开启或者关闭回路
    private void oPenOrClose(final String loop, final String openOrClose, final String S_RegPackage, final String SL_Organize_S_Id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String loopNumbString = GsonUtil.getLamp(loop);
                String value = S_RegPackage + "," + openOrClose + "," + loopNumbString + "," + SL_Organize_S_Id;
                LoopHttp.OpenOrCloseLoop(value);
            }
        }).start();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String value = marker.getSnippet();
        String[] valueString = value.split(",");
        if (valueString[0].equals("h")){
            //主机
            if (!valueString[1].equals("No")){
                //弹出
                loogC(valueString[1],valueString[2]);
            }
        }
        if (valueString[0].equals("f")){
            //分控
            if (!valueString[1].equals("No")){
                //弹出
                belowwindows(valueString[1],valueString[2]);
            }
        }
        return false;
    }
}
