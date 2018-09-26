package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.Group;
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
 * Created by Administrator on 2017/9/1 0001.
 */

public class GroupHttp {
    private static final String TAG ="GroupHttp" ;

    /***
     * 分组配置
     * @param value
     * @return
     */
        public static boolean SubGroupConfig(String value) {
            String SubGroupConfigUrl = TotalUrl.getsURLHEAD()+"/Send/SubGroupConfig?Value=" + value;
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .build();
                Request request = new Request.Builder()
                        .url(SubGroupConfigUrl)
                        .post(requestBody)
                        .build();
                Log.e(TAG,"URL:"+SubGroupConfigUrl);
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String addHost =  response.body().string();
                    SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                    System.out.println(scf);
                    System.err.println("请求服务器成功，做相应处理");
                    Log.e(TAG, "配置"+String.valueOf(scf));
                    int code = scf.getErrNum();
                    if (code == 0) {
                        return true;
                    } else {
                        return false;
                    }

                } else {
                    Log.e(TAG,"重新登录进来了");
                    User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                    TotalUrl.setUser(user);
                    if (response.isSuccessful()) {
                        String addHost =  response.body().string();
                        SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                        System.out.println(scf);
                        System.err.println("请求服务器成功，做相应处理");
                        Log.e(TAG, "配置"+String.valueOf(scf));
                        int code = scf.getErrNum();
                        if (code == 0) {
                            return true;
                        } else {
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


    /***
     * 分组信息获取
     * @param value
     * @return
     */
    public static boolean SubGroup(String value) {
        String SubGroupConfigUrl = TotalUrl.getsURLHEAD()+"/Send/SubGroup?Value=" + value;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(SubGroupConfigUrl)
                    .post(requestBody)
                    .build();
            Log.e(TAG,"URL:"+SubGroupConfigUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addHost =  response.body().string();
                SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                System.out.println(scf);
                System.err.println("请求服务器成功，做相应处理");
                Log.e(TAG, "配置"+String.valueOf(scf));
                int code = scf.getErrNum();
                if (code == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String addHost =  response.body().string();
                    SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                    System.out.println(scf);
                    System.err.println("请求服务器成功，做相应处理");
                    Log.e(TAG, "配置"+String.valueOf(scf));
                    int code = scf.getErrNum();
                    if (code == 0) {
                        return true;
                    } else {
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


    // 根据注册包查询分组信息
    public static List<Group> selectOrPackG(User s, String pack) {
        String selectGroupUrl = TotalUrl.getsURLHEAD()+"/Group/QueryPack?pack=" + pack;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(selectGroupUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + s.getdate().getApiKey())
                    .addHeader("roleid", s.getdate().getRoleId())
                    .addHeader("userid", s.getdate().getUserID())
                    .addHeader("organizeid", s.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+selectGroupUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String updaInfo =  response.body().string();
                Gson g = new Gson();
                List<Group> group = g.fromJson(updaInfo, new TypeToken<List<Group>>() {
                }.getType());
                Log.e(TAG, String.valueOf(group));
                if (group != null) {
                    return group;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String updaInfo =  response.body().string();
                    Gson g = new Gson();
                    List<Group> group = g.fromJson(updaInfo, new TypeToken<List<Group>>() {
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

    //分组
    public static boolean sendOneGroup(String value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String sendGroupUrl= TotalUrl.getsURLHEAD()+"/Send/Grouping?Value="+value;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(sendGroupUrl)
                    .post(requestBody)
                    .build();
            Log.e(TAG,"URL:"+sendGroupUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addHost =  response.body().string();
                SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                System.out.println(scf);
                System.err.println("请求服务器成功，做相应处理");
                Log.e(TAG, "發送:"+String.valueOf(scf));
                int code = scf.getErrNum();
                if (code == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String addHost =  response.body().string();
                    SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                    System.out.println(scf);
                    System.err.println("请求服务器成功，做相应处理");
                    Log.e(TAG, "發送:"+String.valueOf(scf));
                    int code = scf.getErrNum();
                    if (code == 0) {
                        return true;
                    } else {
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
