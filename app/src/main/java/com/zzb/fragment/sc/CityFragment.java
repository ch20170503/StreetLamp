package com.zzb.fragment.sc;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zzb.bean.Host;
import com.zzb.bean.Organ;
import com.zzb.bean.SelectUser;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.fragment.sc.stController.ItemRemoveRecyclerView;
import com.zzb.fragment.sc.stController.MyAdapter;
import com.zzb.fragment.sc.stController.OnItemClickListener;
import com.zzb.http.HostHttp;
import com.zzb.http.OrganHttp;
import com.zzb.http.UserHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * hc 2017-09-01
 */
public class CityFragment extends Fragment {
    private static final String TAG = "onelamphostActivity";
    private ItemRemoveRecyclerView itrr;
    private SwipeRefreshLayout proj_srfl;
    private List<Host> hostList = new ArrayList<>();
    private boolean isPause;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    private ImageView onelamphost_list_img2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        proj_srfl = (SwipeRefreshLayout) view.findViewById(R.id.st_srfl);
        itrr = (ItemRemoveRecyclerView) view.findViewById(R.id.st_container);
        User user = TotalUrl.getUser();
        if (user == null){
            return view;
        }
        int userQ = user.getdate().getSL_USER_RANKID();
        if(userQ == 0){
            return view;
        }
        if (userQ == 2) {
            getData();
            proj_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //初始化
                    hostList.clear();
                    getData();
                    proj_srfl.setRefreshing(false);
                }
            });
        } else {
            getData2();
            proj_srfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.e(TAG, "333333");
                    //初始化
                    hostList.clear();
                    getData2();
                    proj_srfl.setRefreshing(false);
                }
            });
        }
        return view;
    }

    private void getData() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String userPro = user.getdate().getOrganizeId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主机
                List<Host> hoseLists = HostHttp.SelectAllHost(user);
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (hoseLists == null || hoseLists.size() < 1) {
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

                if (organList == null || organList.size() < 1) {
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
                if (hostList == null || hostList.size() < 1) {
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

                Log.e(TAG, "遍历完成后的:" + hostList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(getActivity(), hostList);
                        itrr.setLayoutManager(new LinearLayoutManager(getActivity()));
                        itrr.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(getActivity());
                        itrr.setLayoutManager(l);
                        itrr.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Host ho = hostList.get(position);
                                Log.e(TAG, "跳转的:" + ho);
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), StrategyListActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_STRATEGYHOST", ho);
                                intent.putExtras(bundle);
                                startActivity(intent);
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

    //操作员
    private void getData2() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主机
                List<Host> hoseLists = HostHttp.SelectAllHost(user);
                //用户信息
                List<SelectUser> selectUList1 = UserHttp.thisUser(user);
                List<Organ> organList = OrganHttp.selectAllProj(user);
                if (organList == null || organList.size() < 1) {
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
                if (hoseLists == null || hoseLists.size() < 1) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(getActivity(), R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                if (selectUList1 == null || selectUList1.size() < 1) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(getActivity(), R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                if (selectUList1.get(0).getsS_Project() == null || selectUList1.get(0).getsS_Project() == "") {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(getActivity(), R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }
                String[] proList;
                if (selectUList1.get(0).getsS_Project().contains(",")) {
                    proList = selectUList1.get(0).getsS_Project().split(",");
                } else {
                    proList = (selectUList1.get(0).getsS_Project() + ",").split(",");
                }
                for (int a = 0; a < proList.length; a++) {
                    for (int i = 0; i < organList.size(); i++) {
                        if (proList[a].equals(organList.get(i).getsS_Id())) {
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
                if (hostList == null || hostList.size() < 1) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(getActivity(), R.string.Currentlynoinformation, 1 * 1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler2.sendMessage(msg_listData);
                    return;
                }

                Log.e(TAG, "遍历完成后的:" + hostList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MyAdapter adapter = new MyAdapter(getActivity(), hostList);
                        itrr.setLayoutManager(new LinearLayoutManager(getActivity()));
                        itrr.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(getActivity());
                        itrr.setLayoutManager(l);
                        itrr.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Host ho = hostList.get(position);
                                Log.e(TAG, "跳转的:" + ho);
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), StrategyListActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SELECTUSER_STRATEGYHOST", ho);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler2.sendMessage(msg_listData);
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
}
