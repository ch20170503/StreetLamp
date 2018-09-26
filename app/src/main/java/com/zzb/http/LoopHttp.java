package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.HostDataSet;
import com.zzb.bean.SubGroupConfigInfo;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.util.GsonUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/5 0005.
 */

public class LoopHttp {
    private static final String TAG = "LoopHttp";

    // 查询主机状态信息
    public static List<HostDataSet> SelectAllHostDataSet(User u) {
        String HostDataSeturl = TotalUrl.getsURLHEAD() +"/Host/DateQuerys?PageSize=999999&PageIndex=1&Order=1";
        Log.e(TAG,"User:"+u);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(HostDataSeturl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + HostDataSeturl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo = response.body().string();
                Log.e(TAG, "LoginInfo:" + LoginInfo);
                Gson g = new Gson();
                List<HostDataSet> hostdata = g.fromJson(LoginInfo, new TypeToken<List<HostDataSet>>() {
                }.getType());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, "查询主机状态" + String.valueOf(hostdata));
                if (hostdata != null) {
                    return hostdata;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String LoginInfo = response.body().string();
                    Log.e(TAG, "LoginInfo:" + LoginInfo);
                    Gson g = new Gson();
                    List<HostDataSet> hostdata = g.fromJson(LoginInfo, new TypeToken<List<HostDataSet>>() {
                    }.getType());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, "查询主机状态" + String.valueOf(hostdata));
                    if (hostdata != null) {
                        return hostdata;
                    }
                }else{
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    //开启关闭回路
    public static boolean OpenOrCloseLoop(String value) {
        String openCloseLoopUrl=TotalUrl.getsURLHEAD()+"/Send/Loops?Value="+value;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(openCloseLoopUrl)
                    .post(requestBody)
                    .build();
            Log.e(TAG,"URL:"+openCloseLoopUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addHost =  response.body().string();
                Log.e(TAG,"开启或者关闭回路的结果为" + addHost);
                SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                Log.e(TAG, String.valueOf(scf));
                int code = scf.getErrNum();
                if(code == 0){
                    return true;
                }else{
                    return false;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String addHost =  response.body().string();
                    Log.e(TAG,"开启或者关闭回路的结果为" + addHost);
                    SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                    Log.e(TAG, String.valueOf(scf));
                    int code = scf.getErrNum();
                    if(code == 0){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
