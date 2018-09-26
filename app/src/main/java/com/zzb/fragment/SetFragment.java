package com.zzb.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzb.bean.AndroidUserInfo;
import com.zzb.db.UserDao;
import com.zzb.db.UserDaoImpl;
import com.zzb.mainnavigatetabbar.widget.XCRoundImageView;
import com.zzb.sl.Login;
import com.zzb.sl.R;
import com.zzb.sl.UserActivity;
import com.zzb.sl.UserBasicsInfo.UserBasicsItemActivity;
import com.zzb.sl.deviceManagement.DeviceManagementActivity;
import com.zzb.sl.group.GroupHostActivity;
import com.zzb.sl.strategy.StrategyInfoActivity;
import com.zzb.util.ActivityUtil;
import com.maiml.library.BaseItemLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
public class SetFragment extends Fragment {
    private static final String TAG = "SetFragment";
    private BaseItemLayout layout;
    private TextView closeLogin,set_name;
    private XCRoundImageView imageView;
    private LinearLayout set_ll;
    //常量
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHONE = 2;
    private UserDao userDao;
    private AndroidUserInfo androidUserInfo;
    private String [] NnameString;
    //创建list集合
    private List<AndroidUserInfo> listUl = new ArrayList<>();
    public SetFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        set_name = (TextView) view.findViewById(R.id.set_name);
        Intent intent = getActivity().getIntent();
        String Nname = intent.getStringExtra("getsS_NickName");
         NnameString =  Nname.split(",");
        playDate(NnameString[1]);
        set_name.setText(NnameString[0]);
        imageView = (XCRoundImageView) view.findViewById(R.id.set_img);
        if(listUl.size() >=1 && listUl.get(0).getImphoto() != null){
            //清除之前加载过的图片缓存
            if(imageView.getDrawingCache() != null){
                imageView.refreshDrawableState();
                imageView.setImageBitmap(listUl.get(0).getImphoto());
            }else{
                imageView.setImageBitmap(listUl.get(0).getImphoto());
            }
        }
        set_ll = (LinearLayout) view.findViewById(R.id.set_ll);
        set_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UserActivity.class);
                intent.putExtra("USERACTIVITY",NnameString[1]);
                startActivity(intent);
            }
        });
        //获取控件ID
        layout = (BaseItemLayout) view.findViewById(R.id.layout);
        //存放Text显示(基础信息)
        String BasicInformation = getActivity().getResources().getString(R.string.BasicInformation);
        String deviceManagement = getActivity().getResources().getString(R.string.DeviceSettings);
        String PacketAllocation = getActivity().getResources().getString(R.string.GroupingSetting);
        String PolicyConfiguration = getActivity().getResources().getString(R.string.StrategySetting);
        final List<String> valueList = new ArrayList<>();
        valueList.add(BasicInformation);
        valueList.add(deviceManagement);
        valueList.add(PacketAllocation);
        valueList.add(PolicyConfiguration);
        //存放图片
        List<Integer> resIdList = new ArrayList<>();
        resIdList.add(R.drawable.sheets);
        resIdList.add(R.drawable.shape);
        resIdList.add(R.drawable.file);
        resIdList.add(R.drawable.computer);
        layout.setValueList(valueList) // 文字 list
                .setResIdList(resIdList) //设置图片
                .create();
        //点击item
        layout.setOnBaseItemClick(new BaseItemLayout.OnBaseItemClick() {
            @Override
            public void onItemClick(int position) {
                Log.e(TAG, "点击的是" + valueList.get(position));
                switch (position){
                    case 0:
                        Intent intent = new Intent(getActivity(), UserBasicsItemActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), DeviceManagementActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(), GroupHostActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3= new Intent(getActivity(), StrategyInfoActivity.class);
                        startActivity(intent3);
                        break;
                }
            }
        });
        closeLogin = (TextView) view.findViewById(R.id.set_close);
        closeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置对话框标题
                new AlertDialog.Builder(getActivity()).setTitle(getActivity().getResources().getString(R.string.SystemHint))
                        .setMessage(getActivity().getResources().getString(R.string.ConfirmExit))
                        .setPositiveButton(getActivity().getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                SharedPreferences sp ;
                                sp = getActivity().getSharedPreferences("userInfo", 0);
                                sp.edit().putBoolean("autologin",false).commit();
                                sp.edit().putBoolean("remember",false).commit();
                                Intent inte = new Intent(getActivity(), Login.class);
                                startActivity(inte);
                                getActivity().finish();
                            }
                        }).setNegativeButton(getActivity().getResources().getString(R.string.No),new DialogInterface.OnClickListener() {//添加返回按钮
                             @Override
                              public void onClick(DialogInterface dialog, int which) {//响应事件
                    }
                }).show();//在按键响应事件中显示此对话框

            }
        });
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ActivityUtil.doNext(requestCode, grantResults, getActivity());
    }
    //设置图片
   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       Log.e(TAG,"哈哈");
       super.onActivityResult(requestCode, resultCode, data);
       Log.e(TAG,"哈哈");
       imageView.refreshDrawableState();
       if(listUl.size() >=1){
           imageView.setImageBitmap(listUl.get(0).getImphoto());
       }
    }
    /**
     * 创建数据源
     */
    public void playDate(String name) {
        userDao = new UserDaoImpl(getActivity());
        androidUserInfo = userDao.findByName(name);
        if (androidUserInfo !=null ){
            //将数据保存到集合
            listUl.add(androidUserInfo);
            for (int i = 0; i < listUl.size(); i++) {
                 Log.e(TAG,"数据库:"+listUl.get(i).getName());
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listUl.clear();
        playDate(NnameString[1]);
        if(listUl.size() >= 1){
            Log.e(TAG,"重新加载:");
            imageView.refreshDrawableState();
            imageView.setImageBitmap(listUl.get(0).getImphoto());
        }


    }
}
