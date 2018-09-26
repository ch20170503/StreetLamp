package com.zzb.fragment.message;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzb.bean.Alarm;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.fragment.message.messageitem.ItemRemoveRecyclerView;
import com.zzb.fragment.message.messageitem.MyAdapter;
import com.zzb.fragment.message.messageitem.OnItemClickListener;
import com.zzb.http.AlertHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;
import com.zzb.util.LinearLayoutManagerWrapper;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {
    private static final String TAG = "onelamphostActivity";
    private ItemRemoveRecyclerView itrr;
    private SwipeRefreshLayout proj_srfl;
    private List<Alarm> hostList = new ArrayList<>();
    private boolean isPause;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        proj_srfl = (SwipeRefreshLayout) view.findViewById(R.id.al_srfl);
        itrr = (ItemRemoveRecyclerView) view.findViewById(R.id.al_itr);
        setData();
        proj_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //初始化
                hostList.clear();
                setData();
                proj_srfl.setRefreshing(false);
            }
        });
        return view;
    }

    //设置数据
    private void setData(){
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Alarm> alarmList = AlertHttp.getAlerHttp(user);
                if (alarmList == null || alarmList.size()<1){
                    alarmList = AlertHttp.getAlerHttp(user);
                    if (alarmList == null || alarmList.size()<1){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ActivityUtil.showToasts(getActivity(), R.string.Currentlynoinformation, 1 * 1000);
                            }
                        });
                        //发送handler
                        Message msg_listData = new Message();
                        msg_listData.what = MESSAGETYPE_01;
                        handler.sendMessage(msg_listData);
                        return;
                    }
                }
                hostList = alarmList;
                //Log.e(TAG, "遍历完成后的:" + hostList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(getActivity(), hostList);
                        itrr.setLayoutManager(new LinearLayoutManagerWrapper(getActivity()));
                        itrr.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(getActivity());
                        itrr.setLayoutManager(l);
                        itrr.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
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
}
