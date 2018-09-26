package com.zzb.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.bean.HostNh;
import com.zzb.bean.HostNh2;
import com.zzb.bean.Lampj;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.SubNh;
import com.zzb.bean.SubNh2;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.HostHttp;
import com.zzb.http.LampHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.SubHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;
import com.zzb.util.GsonUtil;
import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.bean.HostNh;
import com.zzb.bean.HostNh2;
import com.zzb.bean.Lampj;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.SubNh;
import com.zzb.bean.SubNh2;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.http.HostHttp;
import com.zzb.http.LampHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.SubHttp;
import com.zzb.http.UserHttp;
import com.zzb.util.ActivityUtil;
import com.zzb.util.GsonUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class  HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    //定义柱状图
    private BarChart barchart;
    private XAxis xAxis;         //X坐标轴
    private YAxis yAxis;         //Y坐标轴
    //设置颜色
    public static final int[] COLORFUL_COLORS = {Color.rgb(230, 169, 135)};
    private BarData data;
    private BarDataSet dataSet;

    //主机 分控 路灯
    private TextView host, sub, lamp;

    private static final int MESSAGETYPE_01 = 0x0001;
    private ProgressDialog progressDialog = null;
    private ArrayList<String> xVals = new ArrayList<String>();//横坐标标签
    private ArrayList<BarEntry> entries = new ArrayList<>();//显示条目
    private List<HostNh> listHostNew = new ArrayList<>();
    private List<SubNh> listSubNew = new ArrayList<>();


    //下拉刷新
    private SwipeRefreshLayout srl;


    //能耗
    private TextView by,sy,jn;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //获取控件ID
        barchart = (BarChart) view.findViewById(R.id.home_BarChart);

        srl = (SwipeRefreshLayout) view.findViewById(R.id.home_srfl);

        host = (TextView) view.findViewById(R.id.home_host);
        sub = (TextView) view.findViewById(R.id.home_sub);
        lamp = (TextView) view.findViewById(R.id.home_lamp);

        by = (TextView) view.findViewById(R.id.by);
        sy = (TextView) view.findViewById(R.id.sy);
        jn = (TextView) view.findViewById(R.id.jn);

        xAxis = barchart.getXAxis();
        yAxis = barchart.getAxisLeft();

        //用户权限
        User u = TotalUrl.getUser();
        if (u != null){
            int userQ = u.getdate().getSL_USER_RANKID();
            if(userQ == 2){
                getInfo();
                srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //初始化
                        listHostNew.clear();
                        listSubNew.clear();
                        entries.clear();
                        xVals.clear();
                        by.setText("0.0");
                        sy.setText("0.0");
                        jn.setText("0.0");
                        getInfo();
                        srl.setRefreshing(false);
                    }
                });
            }else{
                getInfo2();
                srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //初始化
                        listHostNew.clear();
                        listSubNew.clear();
                        entries.clear();
                        xVals.clear();
                        by.setText("0.0");
                        sy.setText("0.0");
                        jn.setText("0.0");
                        getInfo2();
                        srl.setRefreshing(false);
                    }
                });
            }
        }
        return view;
    }
    //获取数据(设备数量)Loading... Please wait!
    private void getInfo() {
        progressDialog = ProgressDialog.show(getActivity(),getActivity().getResources().getString(R.string.Loading), getActivity().getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        final User u = TotalUrl.getUser();
        final String userPro = u.getdate().getOrganizeId();
        final List<ControlS> cList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //主机
                List<Host> hoseList = HostHttp.SelectAllHost(u);
                //机构和项目
                List<Organ> organList = OrganHttp.selectAllProj(u);
                //能耗设置主机(能耗)
                List<HostNh> listHost = HostHttp.SelectHtNh(u);
                //分控能耗
                List<SubNh> listSub = SubHttp.SelectFkNh(u);

                //获取用户的主机
                int numbHost = 0;
                //获取用户的分控
                int numbSup = 0;
                //获取用户的灯具
                int numbLamp = 0;
                if (hoseList == null ||  organList == null) {
                    if(hoseList == null ){

                        hoseList = HostHttp.SelectAllHost(u);
                    }
                    if (organList == null){

                        organList = OrganHttp.selectAllProj(u);
                    }

                    if (hoseList == null ||organList == null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ActivityUtil.showToasts(getActivity(), R.string.Pleasecheckthelightsnetwork, 1 * 1000);
                                host.setText("0");
                                sub.setText("0");
                                lamp.setText("0");
                            }
                        });
                    }
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                } else {
                    final List<Host> hoseListLight = new ArrayList<>();
                    for (int i = 0; i < organList.size(); i++) {
                        String pro = organList.get(i).getsS_ParentId();
                        String proID = organList.get(i).getsS_Id();
                        if (userPro.equals(pro)) {
                            for (int j = 0; j < hoseList.size(); j++) {
                                String userOrg = hoseList.get(j).getsSL_Organize_S_Id();
                                if (userOrg.equals(proID)) {
                                    numbHost++;
                                    hoseListLight.add(hoseList.get(j));
                                }
                            }
                        }
                    }

                    List<Lampj> s ;
                    for (int i = 0; i <hoseListLight.size() ; i++) {
                        s = LampHttp.getHostLamp(hoseListLight.get(i).getsS_RegPackage(),u);
                        if (s == null){
                            s = LampHttp.getHostLamp(hoseListLight.get(i).getsS_RegPackage(),u);
                        }
                        numbLamp += s.size();
                    }

                    List<ControlS> c;
                    for (int i = 0; i <hoseListLight.size() ; i++) {
                        c = SubHttp.selectPackSub(u,hoseListLight.get(i).getsS_RegPackage());
                        if (c == null){
                            c = SubHttp.selectPackSub(u,hoseListLight.get(i).getsS_RegPackage());
                        }
                            cList.addAll(c);
                        numbSup+= c.size();
                    }
                    Log.e(TAG,"根据注册包查询灯具:"+numbLamp);
                    final int finalNumbHost = numbHost;
                    final int finalNumbSup = numbSup;
                    final int finalNumbLamp = numbLamp;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            host.setText(finalNumbHost + "");
                            sub.setText(finalNumbSup + "");
                            lamp.setText(finalNumbLamp + "");
                        }
                    });
                }

                if ( listHost == null|| listSub==null){
                    if (listHost == null){
                        //能耗设置主机(能耗)
                        listHost = HostHttp.SelectHtNh(u);
                    }
                    if (listSub==null){
                        //分控能耗
                        listSub = SubHttp.SelectFkNh(u);
                    }
                    if (listHost == null || listSub==null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ActivityUtil.showToasts(getActivity(), R.string.Currentlynoinformation, 1 * 1000);
                            }
                        });
                    }
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }

                //所属主机的分控耗能
                for (int i = 0; i < organList.size(); i++) {
                    String pro = organList.get(i).getsS_ParentId();
                    String proID = organList.get(i).getsS_Id();
                    if (userPro.equals(pro)) {
                        for (int j = 0; j < hoseList.size(); j++) {
                            String userOrg = hoseList.get(j).getsSL_Organize_S_Id();
                            String RegPackage = hoseList.get(j).getsS_RegPackage();
                            if (userOrg.equals(proID)) {
                                for (int k = 0; k < cList.size(); k++) {
                                    String RegPackageFk = cList.get(k).getsSL_HostBast_RegPackage();
                                    String SupID = cList.get(k).getsS_Id();
                                    if (RegPackage.equals(RegPackageFk)) {
                                        for (int l = 0; l < listSub.size(); l++) {
                                            String SupNumid = listSub.get(l).getsSL_SubController_S_Id();
                                            if (SupID.equals(SupNumid)) {
                                                //当前年份
                                                int y = getY();
                                                if (listSub.get(l).getsS_Year() == y){
                                                    listSubNew.add(listSub.get(l));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //项目所属主机的耗能
                for (int i = 0; i < organList.size(); i++) {
                    String pro = organList.get(i).getsS_ParentId();
                    String proID = organList.get(i).getsS_Id();
                    if (userPro.equals(pro)) {
                        for (int j = 0; j < hoseList.size(); j++) {
                            String userOrg = hoseList.get(j).getsSL_Organize_S_Id();
                            String RegPackage = hoseList.get(j).getsS_RegPackage();
                            if (userOrg.equals(proID)) {
                                for (int k = 0; k < listHost.size(); k++) {
                                    String RegPackageHt = listHost.get(k).getsSL_HostBase_RegPackage();
                                    if (RegPackage.equals(RegPackageHt)) {
                                        //当前年份
                                        int y = getY();
                                        if (listHost.get(k).getsS_Year() == y){
                                            listHostNew.add(listHost.get(k));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Log.e(TAG,"遍历后主机能耗："+listHostNew.toString());
                Log.e(TAG,"遍历后分控能耗："+listSubNew.toString());
                //主机能耗
                float S_January = 0;
                float S_February = 0;
                float S_March = 0;
                float S_April = 0;
                float S_May = 0;
                float S_June = 0;
                float S_July = 0;
                float S_August = 0;
                float S_September = 0;
                float S_October = 0;
                float S_November = 0;
                float S_December = 0;

                for (int i = 0; i < listHostNew.size(); i++) {
                    Log.e(TAG, "主机能耗：" + listHostNew.get(i));
                    S_January += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_January());
                    S_February += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_February());
                    S_March += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_March());
                    S_April += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_April());
                    S_May += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_May());
                    S_June += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_June());
                    S_July += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_July());
                    S_August += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_August());
                    S_September += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_September());
                    S_October += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_October());
                    S_November += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_November());
                    S_December += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_December());
                }
                final HostNh2 hostNh2 = new HostNh2();
                final SubNh2 subNh2 = new SubNh2();
                hostNh2.setsS_January(S_January);
                hostNh2.setsS_February(S_February);
                hostNh2.setsS_March(S_March);
                hostNh2.setsS_April(S_April);
                hostNh2.setsS_May(S_May);
                hostNh2.setsS_June(S_June);
                hostNh2.setsS_July(S_July);
                hostNh2.setsS_August(S_August);
                hostNh2.setsS_September(S_September);
                hostNh2.setsS_October(S_October);
                hostNh2.setsS_November(S_November);
                hostNh2.setsS_December(S_December);
                Log.e(TAG, "S_January:" + hostNh2.getsS_January());
                Log.e(TAG, "S_February:" + hostNh2.getsS_February());
                Log.e(TAG, "S_March:" + hostNh2.getsS_March());
                Log.e(TAG, "S_April:" + hostNh2.getsS_April());
                Log.e(TAG, "S_May:" + hostNh2.getsS_May());
                Log.e(TAG, "S_June:" + hostNh2.getsS_June());
                Log.e(TAG, "S_July:" + hostNh2.getsS_July());
                Log.e(TAG, "S_August:" + hostNh2.getsS_August());
                Log.e(TAG, "S_September:" + hostNh2.getsS_September());
                Log.e(TAG, "S_October:" + hostNh2.getsS_October());
                Log.e(TAG, "S_November:" + hostNh2.getsS_November());
                Log.e(TAG, "S_December:" + hostNh2.getsS_December());
                //分控能耗
                float S_January1 = 0;
                float S_February1 = 0;
                float S_March1 = 0;
                float S_April1 = 0;
                float S_May1 = 0;
                float S_June1 = 0;
                float S_July1 = 0;
                float S_August1 = 0;
                float S_September1 = 0;
                float S_October1 = 0;
                float S_November1 = 0;
                float S_December1 = 0;
                for (int i = 0; i < listSubNew.size(); i++) {
                    Log.e(TAG, "分控能耗：" + listSubNew.get(i));
                    S_January1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_January());
                    S_February1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_February());
                    S_March1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_March());
                    S_April1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_April());
                    S_May1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_May());
                    S_June1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_June());
                    S_July1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_July());
                    S_August1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_August());
                    S_September1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_September());
                    S_October1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_October());
                    S_November1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_November());
                    S_December1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_December());
                }


                subNh2.setsS_January(S_January1);
                subNh2.setsS_February(S_February1);
                subNh2.setsS_March(S_March1);
                subNh2.setsS_April(S_April1);
                subNh2.setsS_May(S_May1);
                subNh2.setsS_June(S_June1);
                subNh2.setsS_July(S_July1);
                subNh2.setsS_August(S_August1);
                subNh2.setsS_September(S_September1);
                subNh2.setsS_October(S_October1);
                subNh2.setsS_November(S_November1);
                subNh2.setsS_December(S_December1);
                Log.e(TAG, "S_January1:" + subNh2.getsS_January());
                Log.e(TAG, "S_February1:" + subNh2.getsS_February());
                Log.e(TAG, "S_March1:" + subNh2.getsS_March());
                Log.e(TAG, "S_April1:" + subNh2.getsS_April());
                Log.e(TAG, "S_May1:" + subNh2.getsS_May());
                Log.e(TAG, "S_June1:" + subNh2.getsS_June());
                Log.e(TAG, "S_July1:" + subNh2.getsS_July());
                Log.e(TAG, "S_August1:" + subNh2.getsS_August());
                Log.e(TAG, "S_September1:" + subNh2.getsS_September());
                Log.e(TAG, "S_October1:" + subNh2.getsS_October());
                Log.e(TAG, "S_November1:" + subNh2.getsS_November());
                Log.e(TAG, "S_December1:" + subNh2.getsS_December());
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler.sendMessage(msg_listData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 12; i++) {
                            xVals.add((i + 1) + "");

                        }
                        entries.add(new BarEntry(subNh2.getsS_January()+hostNh2.getsS_January(), 0));
                        entries.add(new BarEntry(subNh2.getsS_February()+hostNh2.getsS_February(), 1));
                        entries.add(new BarEntry(subNh2.getsS_March()+hostNh2.getsS_March(), 2));
                        entries.add(new BarEntry(subNh2.getsS_April()+hostNh2.getsS_April(), 3));
                        entries.add(new BarEntry(subNh2.getsS_May()+hostNh2.getsS_May(), 4));
                        entries.add(new BarEntry(subNh2.getsS_June()+hostNh2.getsS_June(), 5));
                        entries.add(new BarEntry(subNh2.getsS_July()+hostNh2.getsS_July(), 6));
                        entries.add(new BarEntry(subNh2.getsS_August()+hostNh2.getsS_August(), 7));
                        entries.add(new BarEntry(subNh2.getsS_September()+hostNh2.getsS_September(), 8));
                        entries.add(new BarEntry(subNh2.getsS_October()+hostNh2.getsS_October(), 9));
                        entries.add(new BarEntry(subNh2.getsS_November()+hostNh2.getsS_November(), 10));
                        entries.add(new BarEntry(subNh2.getsS_December()+hostNh2.getsS_December(), 11));
                        //数据
                        barchart.getAxisLeft().setDrawGridLines(false);//不设置Y轴网格
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        barchart.getAxisRight().setEnabled(false);//右侧不显示Y轴
                        dataSet = new BarDataSet(entries, getActivity().getResources().getString(R.string.StatisticsKW));
                        dataSet.setColors(COLORFUL_COLORS);

                        data = new BarData(xVals, dataSet);
                        barchart.setData(data);
                        //设置Y方向上动画animateY(int time);
                        barchart.animateY(3000);

                        //图表描述图表默认右下方的描述，参数是String对象
                        barchart.setDescription("  ");
                        float j = (subNh2.getsS_January()+hostNh2.getsS_January())+(subNh2.getsS_February()+hostNh2.getsS_February())+
                                (subNh2.getsS_March()+hostNh2.getsS_March())+(subNh2.getsS_April()+hostNh2.getsS_April())+
                                (subNh2.getsS_May()+hostNh2.getsS_May())+(subNh2.getsS_June()+hostNh2.getsS_June())+(subNh2.getsS_July()+hostNh2.getsS_July())+
                                (subNh2.getsS_August()+hostNh2.getsS_August())+(subNh2.getsS_September()+hostNh2.getsS_September())+(subNh2.getsS_October()+hostNh2.getsS_October())
                                +(subNh2.getsS_November()+hostNh2.getsS_November())+(subNh2.getsS_December()+hostNh2.getsS_December());

                        //当前月
                        int month  = getM();
                        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        switch (month){
                            case 1:
                                String b1=decimalFormat.format((subNh2.getsS_January()+hostNh2.getsS_January()));//format 返回的是字符串
                                String s1=decimalFormat.format((subNh2.getsS_December()+hostNh2.getsS_December()));//format 返回的是字符串
                                by.setText(b1);
                                sy.setText(s1);
                                break;
                            case 2:
                                String b2=decimalFormat.format((subNh2.getsS_February()+hostNh2.getsS_February()));//format 返回的是字符串
                                String s2=decimalFormat.format((subNh2.getsS_January()+hostNh2.getsS_January()));//format 返回的是字符串
                                by.setText(b2);
                                sy.setText(s2);

                                break;
                            case 3:
                                String b3=decimalFormat.format((subNh2.getsS_March()+hostNh2.getsS_March()));//format 返回的是字符串
                                String s3=decimalFormat.format((subNh2.getsS_February()+hostNh2.getsS_February()));//format 返回的是字符串
                                by.setText(b3);
                                sy.setText(s3);
                                break;
                            case 4:
                                String b4=decimalFormat.format( (subNh2.getsS_April()+hostNh2.getsS_April()));//format 返回的是字符串
                                String s4=decimalFormat.format((subNh2.getsS_March()+hostNh2.getsS_March()));//format 返回的是字符串
                                by.setText(b4);
                                sy.setText(s4);
                                break;
                            case 5:
                                String b5=decimalFormat.format((subNh2.getsS_May()+hostNh2.getsS_May()));//format 返回的是字符串
                                String s5=decimalFormat.format((subNh2.getsS_April()+hostNh2.getsS_April()));//format 返回的是字符串
                                by.setText(b5);
                                sy.setText(s5);
                                break;
                            case 6:
                                String b6=decimalFormat.format((subNh2.getsS_June()+hostNh2.getsS_June()));//format 返回的是字符串
                                String s6=decimalFormat.format((subNh2.getsS_May()+hostNh2.getsS_May()));//format 返回的是字符串
                                by.setText(b6);
                                sy.setText(s6);

                                break;
                            case 7:
                                String b7=decimalFormat.format((subNh2.getsS_July()+hostNh2.getsS_July()));//format 返回的是字符串
                                String s7=decimalFormat.format((subNh2.getsS_June()+hostNh2.getsS_June()));//format 返回的是字符串
                                by.setText(b7);
                                sy.setText(s7);
                                break;
                            case 8:
                                String b8=decimalFormat.format((subNh2.getsS_August()+hostNh2.getsS_August()));//format 返回的是字符串
                                String s8=decimalFormat.format((subNh2.getsS_July()+hostNh2.getsS_July()));//format 返回的是字符串
                                by.setText(b8);
                                sy.setText(s8);
                                break;
                            case 9:
                                String b9=decimalFormat.format((subNh2.getsS_September()+hostNh2.getsS_September()));//format 返回的是字符串
                                String s9=decimalFormat.format((subNh2.getsS_August()+hostNh2.getsS_August()));//format 返回的是字符串
                                by.setText(b9);
                                sy.setText(s9);
                                break;
                            case 10:
                                String b10=decimalFormat.format( (subNh2.getsS_October()+hostNh2.getsS_October()));//format 返回的是字符串
                                String s10=decimalFormat.format((subNh2.getsS_September()+hostNh2.getsS_September()));//format 返回的是字符串
                                by.setText(b10);
                                sy.setText(s10);
                                break;
                            case 11:
                                String b11=decimalFormat.format((subNh2.getsS_November()+hostNh2.getsS_November()));//format 返回的是字符串
                                String s11=decimalFormat.format((subNh2.getsS_October()+hostNh2.getsS_October()));//format 返回的是字符串
                                by.setText(b11);
                                sy.setText(s11);
                                break;
                            case 12:
                                String b12=decimalFormat.format((subNh2.getsS_December()+hostNh2.getsS_December()));//format 返回的是字符串
                                String s12=decimalFormat.format((subNh2.getsS_November()+hostNh2.getsS_November()));//format 返回的是字符串
                                by.setText(b12);
                                sy.setText(s12);
                                break;
                        }

                        //今年能耗
                        String jnn = decimalFormat.format(j);
                        jn.setText(jnn);

                    }
                });
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


    //获取数据 操作员(设备数量)
    private void getInfo2() {
        progressDialog = ProgressDialog.show(getActivity(),getActivity().getResources().getString(R.string.Loading), getActivity().getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

        final List<ControlS> cList = new ArrayList<>();
        final User u = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //用户信息
                List<SelectUser> selectUList1 = UserHttp.thisUser(u);
                if (selectUList1 == null || selectUList1.size()<1){
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                if(selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() ==""){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(getActivity(), "当前无信息", 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }else{
                    String [] proList;
                    if(selectUList1.get(0).getsS_Project().contains(",")){
                        proList = selectUList1.get(0).getsS_Project().split(",");
                    }else{
                        proList = (selectUList1.get(0).getsS_Project()+",").split(",");
                    }

                    //主机
                    List<Host> hoseList = HostHttp.SelectAllHost(u);
                    //机构和项目
                    List<Organ> organList = OrganHttp.selectAllProj(u);
                    //能耗设置主机(能耗)
                    List<HostNh> listHost = HostHttp.SelectHtNh(u);
                    //分控能耗
                    List<SubNh> listSub = SubHttp.SelectFkNh(u);

                    //获取用户的主机
                    int numbHost = 0;
                    //获取用户的分控
                    int numbSup = 0;
                    //获取用户的灯具
                    int numbLamp = 0;
                    if (hoseList == null ||  organList == null) {
                        if(hoseList == null ){

                            hoseList = HostHttp.SelectAllHost(u);
                        }

                        if (organList == null){

                            organList = OrganHttp.selectAllProj(u);
                        }

                        if (hoseList == null ||  organList == null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ActivityUtil.showToasts(getActivity(), R.string.Pleasecheckthelightsnetwork, 1 * 1000);
                                    host.setText("0");
                                    sub.setText("0");
                                    lamp.setText("0");
                                }
                            });
                        }
                        //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler2.sendMessage(msg_listData);
                        return;
                    }else{
                        final List<Host> hoseListLight = new ArrayList<>();
                        for (int a = 0; a <proList.length ; a++) {
                            for (int i = 0; i < organList.size(); i++) {
                                if (proList[a].equals(organList.get(i).getsS_Id())){
                                    String proID = organList.get(i).getsS_Id();
                                    for (int j = 0; j < hoseList.size(); j++) {
                                        String userOrg = hoseList.get(j).getsSL_Organize_S_Id();
                                        if (userOrg.equals(proID)) {
                                            numbHost++;
                                            hoseListLight.add(hoseList.get(j));
                                        }
                                    }
                                }
                            }
                        }
                        List<Lampj> s ;
                        for (int i = 0; i <hoseListLight.size() ; i++) {
                            s = LampHttp.getHostLamp(hoseListLight.get(i).getsS_RegPackage(),u);
                            numbLamp += s.size();
                        }
                        List<ControlS> c;
                        for (int i = 0; i <hoseListLight.size() ; i++) {
                            c = SubHttp.selectPackSub(u,hoseListLight.get(i).getsS_RegPackage());
                            if (c == null){
                                c = SubHttp.selectPackSub(u,hoseListLight.get(i).getsS_RegPackage());
                            }
                            cList.addAll(c);
                            numbSup+= c.size();
                        }
                        Log.e(TAG,"根据注册包查询灯具:"+numbLamp);
                        final int finalNumbHost = numbHost;
                        final int finalNumbSup = numbSup;
                        final int finalNumbLamp = numbLamp;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                host.setText(finalNumbHost + "");
                                sub.setText(finalNumbSup + "");
                                lamp.setText(finalNumbLamp + "");
                            }
                        });

                        if ( listHost == null|| listSub==null){
                            if (listHost == null){
                                //能耗设置主机(能耗)
                                listHost = HostHttp.SelectHtNh(u);
                            }
                            if (listSub==null){
                                //分控能耗
                                listSub = SubHttp.SelectFkNh(u);
                            }
                            if (listHost == null || listSub==null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ActivityUtil.showToasts(getActivity(), R.string.Currentlynoinformation, 1 * 1000);
                                    }
                                });
                            }
                            //发送handler
                            Message msg_listData = new Message();
                            msg_listData.what = MESSAGETYPE_01;
                            handler2.sendMessage(msg_listData);
                            return;
                        }

                        for (int a = 0; a <proList.length ; a++) {
                            //所属主机的分控耗能
                            for (int i = 0; i < organList.size(); i++) {
                                if (proList[a].equals(organList.get(i).getsS_Id())){
                                    String proID = organList.get(i).getsS_Id();
                                    for (int j = 0; j < hoseList.size(); j++) {
                                        String userOrg = hoseList.get(j).getsSL_Organize_S_Id();
                                        String RegPackage = hoseList.get(j).getsS_RegPackage();
                                        if (userOrg.equals(proID)) {
                                            for (int k = 0; k < cList.size(); k++) {
                                                String RegPackageFk = cList.get(k).getsSL_HostBast_RegPackage();
                                                String SupID = cList.get(k).getsS_Id();
                                                if (RegPackage.equals(RegPackageFk)) {
                                                    for (int l = 0; l < listSub.size(); l++) {
                                                        String SupNumid = listSub.get(l).getsSL_SubController_S_Id();
                                                        if (SupID.equals(SupNumid)) {
                                                            //当前年份
                                                            int y = getY();
                                                            if (listSub.get(l).getsS_Year() == y){
                                                                listSubNew.add(listSub.get(l));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }


                        //项目所属主机的耗能
                        for (int a = 0; a <proList.length ; a++) {
                            for (int i = 0; i < organList.size(); i++) {
                                if (proList[a].equals(organList.get(i).getsS_Id())){
                                    String proID = organList.get(i).getsS_Id();
                                    for (int j = 0; j < hoseList.size(); j++) {
                                        String userOrg = hoseList.get(j).getsSL_Organize_S_Id();
                                        String RegPackage = hoseList.get(j).getsS_RegPackage();
                                        if (userOrg.equals(proID)) {
                                            for (int k = 0; k < listHost.size(); k++) {
                                                String RegPackageHt = listHost.get(k).getsSL_HostBase_RegPackage();
                                                if (RegPackage.equals(RegPackageHt)) {
                                                    //当前年份
                                                    int y = getY();
                                                    if (listHost.get(k).getsS_Year() == y){

                                                        listHostNew.add(listHost.get(k));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }


                        Log.e(TAG,"遍历后主机能耗："+listHostNew.toString());
                        Log.e(TAG,"遍历后分控能耗："+listSubNew.toString());
                        //主机能耗
                        float S_January = 0;
                        float S_February = 0;
                        float S_March = 0;
                        float S_April = 0;
                        float S_May = 0;
                        float S_June = 0;
                        float S_July = 0;
                        float S_August = 0;
                        float S_September = 0;
                        float S_October = 0;
                        float S_November = 0;
                        float S_December = 0;

                        for (int i = 0; i < listHostNew.size(); i++) {
                            Log.e(TAG, "主机能耗：" + listHostNew.get(i));
                            S_January += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_January());
                            S_February += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_February());
                            S_March += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_March());
                            S_April += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_April());
                            S_May += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_May());
                            S_June += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_June());
                            S_July += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_July());
                            S_August += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_August());
                            S_September += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_September());
                            S_October += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_October());
                            S_November += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_November());
                            S_December += GsonUtil.getHostFkNh(listHostNew.get(i).getsS_December());
                        }
                        final HostNh2 hostNh2 = new HostNh2();
                        final SubNh2 subNh2 = new SubNh2();
                        hostNh2.setsS_January(S_January);
                        hostNh2.setsS_February(S_February);
                        hostNh2.setsS_March(S_March);
                        hostNh2.setsS_April(S_April);
                        hostNh2.setsS_May(S_May);
                        hostNh2.setsS_June(S_June);
                        hostNh2.setsS_July(S_July);
                        hostNh2.setsS_August(S_August);
                        hostNh2.setsS_September(S_September);
                        hostNh2.setsS_October(S_October);
                        hostNh2.setsS_November(S_November);
                        hostNh2.setsS_December(S_December);
                        Log.e(TAG, "S_January:" + hostNh2.getsS_January());
                        Log.e(TAG, "S_February:" + hostNh2.getsS_February());
                        Log.e(TAG, "S_March:" + hostNh2.getsS_March());
                        Log.e(TAG, "S_April:" + hostNh2.getsS_April());
                        Log.e(TAG, "S_May:" + hostNh2.getsS_May());
                        Log.e(TAG, "S_June:" + hostNh2.getsS_June());
                        Log.e(TAG, "S_July:" + hostNh2.getsS_July());
                        Log.e(TAG, "S_August:" + hostNh2.getsS_August());
                        Log.e(TAG, "S_September:" + hostNh2.getsS_September());
                        Log.e(TAG, "S_October:" + hostNh2.getsS_October());
                        Log.e(TAG, "S_November:" + hostNh2.getsS_November());
                        Log.e(TAG, "S_December:" + hostNh2.getsS_December());
                        //分控能耗
                        float S_January1 = 0;
                        float S_February1 = 0;
                        float S_March1 = 0;
                        float S_April1 = 0;
                        float S_May1 = 0;
                        float S_June1 = 0;
                        float S_July1 = 0;
                        float S_August1 = 0;
                        float S_September1 = 0;
                        float S_October1 = 0;
                        float S_November1 = 0;
                        float S_December1 = 0;

                        for (int i = 0; i < listSubNew.size(); i++) {
                            Log.e(TAG, "分控能耗：" + listSubNew.get(i));
                            S_January1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_January());
                            S_February1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_February());
                            S_March1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_March());
                            S_April1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_April());
                            S_May1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_May());
                            S_June1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_June());
                            S_July1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_July());
                            S_August1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_August());
                            S_September1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_September());
                            S_October1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_October());
                            S_November1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_November());
                            S_December1 += GsonUtil.getHostFkNh(listSubNew.get(i).getsS_December());
                        }
                        subNh2.setsS_January(S_January1);
                        subNh2.setsS_February(S_February1);
                        subNh2.setsS_March(S_March1);
                        subNh2.setsS_April(S_April1);
                        subNh2.setsS_May(S_May1);
                        subNh2.setsS_June(S_June1);
                        subNh2.setsS_July(S_July1);
                        subNh2.setsS_August(S_August1);
                        subNh2.setsS_September(S_September1);
                        subNh2.setsS_October(S_October1);
                        subNh2.setsS_November(S_November1);
                        subNh2.setsS_December(S_December1);
                        Log.e(TAG, "S_January1:" + subNh2.getsS_January());
                        Log.e(TAG, "S_February1:" + subNh2.getsS_February());
                        Log.e(TAG, "S_March1:" + subNh2.getsS_March());
                        Log.e(TAG, "S_April1:" + subNh2.getsS_April());
                        Log.e(TAG, "S_May1:" + subNh2.getsS_May());
                        Log.e(TAG, "S_June1:" + subNh2.getsS_June());
                        Log.e(TAG, "S_July1:" + subNh2.getsS_July());
                        Log.e(TAG, "S_August1:" + subNh2.getsS_August());
                        Log.e(TAG, "S_September1:" + subNh2.getsS_September());
                        Log.e(TAG, "S_October1:" + subNh2.getsS_October());
                        Log.e(TAG, "S_November1:" + subNh2.getsS_November());
                        Log.e(TAG, "S_December1:" + subNh2.getsS_December());

                        //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler.sendMessage(msg_listData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 12; i++) {
                                    xVals.add((i + 1) + "");

                                }
                                entries.add(new BarEntry(subNh2.getsS_January()+hostNh2.getsS_January(), 0));
                                entries.add(new BarEntry(subNh2.getsS_February()+hostNh2.getsS_February(), 1));
                                entries.add(new BarEntry(subNh2.getsS_March()+hostNh2.getsS_March(), 2));
                                entries.add(new BarEntry(subNh2.getsS_April()+hostNh2.getsS_April(), 3));
                                entries.add(new BarEntry(subNh2.getsS_May()+hostNh2.getsS_May(), 4));
                                entries.add(new BarEntry(subNh2.getsS_June()+hostNh2.getsS_June(), 5));
                                entries.add(new BarEntry(subNh2.getsS_July()+hostNh2.getsS_July(), 6));
                                entries.add(new BarEntry(subNh2.getsS_August()+hostNh2.getsS_August(), 7));
                                entries.add(new BarEntry(subNh2.getsS_September()+hostNh2.getsS_September(), 8));
                                entries.add(new BarEntry(subNh2.getsS_October()+hostNh2.getsS_October(), 9));
                                entries.add(new BarEntry(subNh2.getsS_November()+hostNh2.getsS_November(), 10));
                                entries.add(new BarEntry(subNh2.getsS_December()+hostNh2.getsS_December(), 11));
                                //数据
                                barchart.getAxisLeft().setDrawGridLines(false);//不设置Y轴网格
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                barchart.getAxisRight().setEnabled(false);//右侧不显示Y轴
                                dataSet = new BarDataSet(entries, getActivity().getResources().getString(R.string.StatisticsKW));
                                dataSet.setColors(COLORFUL_COLORS);

                                data = new BarData(xVals, dataSet);
                                barchart.setData(data);
                                //设置Y方向上动画animateY(int time);
                                barchart.animateY(3000);

                                //图表描述图表默认右下方的描述，参数是String对象
                                barchart.setDescription("  ");
                                float j = (subNh2.getsS_January()+hostNh2.getsS_January())+(subNh2.getsS_February()+hostNh2.getsS_February())+
                                        (subNh2.getsS_March()+hostNh2.getsS_March())+(subNh2.getsS_April()+hostNh2.getsS_April())+
                                        (subNh2.getsS_May()+hostNh2.getsS_May())+(subNh2.getsS_June()+hostNh2.getsS_June())+(subNh2.getsS_July()+hostNh2.getsS_July())+
                                        (subNh2.getsS_August()+hostNh2.getsS_August())+(subNh2.getsS_September()+hostNh2.getsS_September())+(subNh2.getsS_October()+hostNh2.getsS_October())
                                        +(subNh2.getsS_November()+hostNh2.getsS_November())+(subNh2.getsS_December()+hostNh2.getsS_December());
                                //当前月
                                int month  = getM();
                                DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                                switch (month){
                                    case 1:
                                        String b1=decimalFormat.format((subNh2.getsS_January()+hostNh2.getsS_January()));//format 返回的是字符串
                                        String s1=decimalFormat.format((subNh2.getsS_December()+hostNh2.getsS_December()));//format 返回的是字符串
                                        by.setText(b1);
                                        sy.setText(s1);
                                        break;
                                    case 2:
                                        String b2=decimalFormat.format((subNh2.getsS_February()+hostNh2.getsS_February()));//format 返回的是字符串
                                        String s2=decimalFormat.format((subNh2.getsS_January()+hostNh2.getsS_January()));//format 返回的是字符串
                                        by.setText(b2);
                                        sy.setText(s2);

                                        break;
                                    case 3:
                                        String b3=decimalFormat.format((subNh2.getsS_March()+hostNh2.getsS_March()));//format 返回的是字符串
                                        String s3=decimalFormat.format((subNh2.getsS_February()+hostNh2.getsS_February()));//format 返回的是字符串
                                        by.setText(b3);
                                        sy.setText(s3);
                                        break;
                                    case 4:
                                        String b4=decimalFormat.format( (subNh2.getsS_April()+hostNh2.getsS_April()));//format 返回的是字符串
                                        String s4=decimalFormat.format((subNh2.getsS_March()+hostNh2.getsS_March()));//format 返回的是字符串
                                        by.setText(b4);
                                        sy.setText(s4);
                                        break;
                                    case 5:
                                        String b5=decimalFormat.format((subNh2.getsS_May()+hostNh2.getsS_May()));//format 返回的是字符串
                                        String s5=decimalFormat.format((subNh2.getsS_April()+hostNh2.getsS_April()));//format 返回的是字符串
                                        by.setText(b5);
                                        sy.setText(s5);
                                        break;
                                    case 6:
                                        String b6=decimalFormat.format((subNh2.getsS_June()+hostNh2.getsS_June()));//format 返回的是字符串
                                        String s6=decimalFormat.format((subNh2.getsS_May()+hostNh2.getsS_May()));//format 返回的是字符串
                                        by.setText(b6);
                                        sy.setText(s6);

                                        break;
                                    case 7:
                                        String b7=decimalFormat.format((subNh2.getsS_July()+hostNh2.getsS_July()));//format 返回的是字符串
                                        String s7=decimalFormat.format((subNh2.getsS_June()+hostNh2.getsS_June()));//format 返回的是字符串
                                        by.setText(b7);
                                        sy.setText(s7);
                                        break;
                                    case 8:
                                        String b8=decimalFormat.format((subNh2.getsS_August()+hostNh2.getsS_August()));//format 返回的是字符串
                                        String s8=decimalFormat.format((subNh2.getsS_July()+hostNh2.getsS_July()));//format 返回的是字符串
                                        by.setText(b8);
                                        sy.setText(s8);
                                        break;
                                    case 9:
                                        String b9=decimalFormat.format((subNh2.getsS_September()+hostNh2.getsS_September()));//format 返回的是字符串
                                        String s9=decimalFormat.format((subNh2.getsS_August()+hostNh2.getsS_August()));//format 返回的是字符串
                                        by.setText(b9);
                                        sy.setText(s9);
                                        break;
                                    case 10:
                                        String b10=decimalFormat.format( (subNh2.getsS_October()+hostNh2.getsS_October()));//format 返回的是字符串
                                        String s10=decimalFormat.format((subNh2.getsS_September()+hostNh2.getsS_September()));//format 返回的是字符串
                                        by.setText(b10);
                                        sy.setText(s10);
                                        break;
                                    case 11:
                                        String b11=decimalFormat.format((subNh2.getsS_November()+hostNh2.getsS_November()));//format 返回的是字符串
                                        String s11=decimalFormat.format((subNh2.getsS_October()+hostNh2.getsS_October()));//format 返回的是字符串
                                        by.setText(b11);
                                        sy.setText(s11);
                                        break;
                                    case 12:
                                        String b12=decimalFormat.format((subNh2.getsS_December()+hostNh2.getsS_December()));//format 返回的是字符串
                                        String s12=decimalFormat.format((subNh2.getsS_November()+hostNh2.getsS_November()));//format 返回的是字符串
                                        by.setText(b12);
                                        sy.setText(s12);
                                        break;
                                }
                                //今年能耗
                                String jnn = decimalFormat.format(j);
                                jn.setText(jnn);

                            }
                        });
                    }
                }
            }
        }).start();
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
    //获取当前系统月份
    private int getM(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        Log.e(TAG,"当前月："+month);
        return month;
    }
    //获取当前系统年份
    private int getY(){
        Calendar cal = Calendar.getInstance();
        int year  = cal.get(Calendar.YEAR);
        Log.e(TAG,"当前年："+year);
        return year ;
    }
}