package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.Alarm;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/15 0015.
 *
 */

public class AlertHttp {
    private static final String TAG = "AlertHttp";

    public static List<Alarm> getAlerHttp(User user){
        String getAlerUrl = TotalUrl.getsURLHEAD()+"/Prompt/AccountMsg?PageSize=999999&PageIndex=1&Order=1&Account="+TotalUrl.getName();
       // String getAlerUrl = "http://192.168.1.90:8011/Prompt/AccountMsg?PageSize=999999&PageIndex=1&Order=1&Account="+TotalUrl.getName();

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(getAlerUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+getAlerUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String updaInfo =  response.body().string();
                Log.e(TAG,"alarm:"+updaInfo);
                Gson g = new Gson();
                List<Alarm> group = g.fromJson(updaInfo, new TypeToken<List<Alarm>>() {
                }.getType());
                Log.e(TAG, String.valueOf(group));
                if (group != null) {
                    return group;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User uss = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(uss);
                if (response.isSuccessful()) {
                    String updaInfo =  response.body().string();
                    Gson g = new Gson();
                    List<Alarm> group = g.fromJson(updaInfo, new TypeToken<List<Alarm>>() {
                    }.getType());
                    Log.e(TAG, String.valueOf(group));
                    if (group != null) {
                        return group;
                    }
                }else{
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
