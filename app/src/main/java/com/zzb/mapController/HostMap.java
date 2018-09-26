package com.zzb.mapController;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.zzb.sl.R;
import com.zzb.sl.deviceManagement.HostManage.HostManageInfoActivity;
import com.zzb.util.ActivityUtil;

import java.util.Locale;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class HostMap extends SwipeBackActivity implements AMap.OnMapClickListener, View.OnClickListener {
    private static final String TAG = "HostMap";
    //地图
    private MapView mMapView;
    private AMap mAMap;
    private LatLng latLng = new LatLng(34.341568, 108.940174);
    private MarkerOptions markerOptions;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private EditText host_info_lognitude,host_info_latitude;



    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_map);
        host_info_lognitude = (EditText) findViewById(R.id.host_info_lognitude);
        host_info_latitude = (EditText) findViewById(R.id.host_info_latitude);
        findViewById(R.id.host_jw_list_img2).setOnClickListener(this);
        //地图
        mMapView = (MapView) findViewById(R.id.host_map);
        mMapView.onCreate(savedInstanceState);
        init();
    }

    //地图
    private void init() {
        if (mAMap == null) {
            // 初始化地图
            mAMap = mMapView.getMap();
            boolean b = isZh(this);
            if(b == false){
                mAMap.setMapLanguage("en");
            }
            mAMap.setOnMapClickListener(this);
        }
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

    //地图点击事件
    @Override
    public void onMapClick(LatLng latLng) {
        getJw(latLng, HostManageInfoActivity.class);
    }

    private void getJw(LatLng latLng, final Class c){
        //点击地图后清理图层插上图标，在将其移动到中心位置
        mAMap.clear();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue));
        otMarkerOptions.position(latLng);
        mAMap.addMarker(otMarkerOptions);
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        Log.e(TAG,"经纬度:"+latitude+","+longitude);
        ActivityUtil.showToasts(HostMap.this,"经纬度["+latitude+","+longitude+"]",1*1500);
        //设置对话框标题
        new AlertDialog.Builder(HostMap.this).setTitle("经纬度设置")
                .setMessage("经纬度["+latitude+","+longitude+"]")
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        String latitudes = String.valueOf(longitude);
                        String longitudes = String.valueOf(latitude);
                        Intent inte = new Intent(HostMap.this, c);
                        inte.putExtra("latitudes",latitudes+","+longitudes);
                        setResult(1,inte);
                        finish();
                    }
                }).setNegativeButton(getResources().getString(R.string.No),new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
            }
        }).show();//在按键响应事件中显示此对话框
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
