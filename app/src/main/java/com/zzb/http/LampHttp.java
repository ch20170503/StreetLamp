package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.Lampj;
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
 * Created by Administrator on 2017/8/21 0021.
 */

public class LampHttp {
    private static final String TAG = "LampHttp";

    //查询灯具信息
    public static List<Lampj> SelectAlllamp(User u) {
        String selcetLampUrl = TotalUrl.getsURLHEAD()+"/Light/Querys?PageSize=999999&PageIndex=1"+ "&Order=1";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selcetLampUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selcetLampUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LampInfo = response.body().string();
                //Log.e(TAG, "OrgPInfo:" + LampInfo);
                Gson g = new Gson();
                List<Lampj> lampj = g.fromJson(LampInfo, new TypeToken<List<Lampj>>() {
                }.getType());
               // Log.e(TAG, "请求服务器成功，做相应处理");
                //Log.e(TAG, "查询灯具："+String.valueOf(lampj));
                if (lampj != null) {
                    return lampj;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String LampInfo = response.body().string();
                    //Log.e(TAG, "OrgPInfo:" + LampInfo);
                    Gson g = new Gson();
                    List<Lampj> lampj = g.fromJson(LampInfo, new TypeToken<List<Lampj>>() {
                    }.getType());
                    // Log.e(TAG, "请求服务器成功，做相应处理");
                    //Log.e(TAG, "查询灯具："+String.valueOf(lampj));
                    if (lampj != null) {
                        return lampj;
                    }
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


    //根据主机注册包查询所属灯光(post)
    public static List<Lampj> getHostLamp(String pack,User u) {
        String selectOneContUrl = TotalUrl.getsURLHEAD()+"/Host/GetHostLights?pack="+pack+"&PageSize=999999&PageIndex=1&Order=1";

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(selectOneContUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+selectOneContUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String LampInfo = response.body().string();
                Log.e(TAG, "LampHostInfo:" + LampInfo);
                Gson g = new Gson();
                List<Lampj> lampj = g.fromJson(LampInfo, new TypeToken<List<Lampj>>() {
                }.getType());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, "根据注册包查询灯具："+String.valueOf(lampj));
                if (lampj != null) {
                    return lampj;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String LampInfo = response.body().string();
                    Log.e(TAG, "LampHostInfo:" + LampInfo);
                    Gson g = new Gson();
                    List<Lampj> lampj = g.fromJson(LampInfo, new TypeToken<List<Lampj>>() {
                    }.getType());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, "根据注册包查询灯具："+String.valueOf(lampj));
                    if (lampj != null) {
                        return lampj;
                    }
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

    //添加灯具信息
    public static boolean addLamp(Lampj lamp,User user) {
        //创建URL
        String addLampUrl = TotalUrl.getsURLHEAD()+"/Light/Add?id="+lamp.getsS_Id()
                + "&number="+lamp.getsS_Number()
                + "&lightHD="+lamp.getsS_LightHD()
                + "&type="+lamp.getsS_Type()
                + "&subController_Id="+lamp.getsSL_SubController_Id()
                + "&subNum="+lamp.getsS_Number();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(addLampUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+addLampUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addInfo =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(addInfo, User.class);
                Log.e(TAG, String.valueOf(user1));
                int code = user1.getCode();
                if(code == 0){
                    return true;
                }else{
                    return false;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String addInfo =  response.body().string();
                    User user1 = GsonUtil.parseJsonWithGson(addInfo, User.class);
                    Log.e(TAG, String.valueOf(user1));
                    int code = user1.getCode();
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



    //单灯
    public static boolean sendOneLamp(String value) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String sedOneLamp=TotalUrl.getsURLHEAD()+"/Send/SingleLamp?Value="+value;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(sedOneLamp)
                    .post(requestBody)
                    .build();
            Log.e(TAG,"URL:"+sedOneLamp);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addInfo =  response.body().string();
                SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addInfo, SubGroupConfigInfo.class);
                Log.e(TAG, String.valueOf(scf));
                int code = scf.getErrNum();
                if(code == 0){
                    return true;
                }else{
                    return false;
                }
            } else {
                return false;
                //throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }


    //群灯
    public static boolean sendAllLamp(String value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String sedalllamp=TotalUrl.getsURLHEAD()+"/Send/ManyLamp?Value="+value;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(sedalllamp)
                    .post(requestBody)
                    .build();
            Log.e(TAG,"URL:"+sedalllamp);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addInfo =  response.body().string();
                SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addInfo, SubGroupConfigInfo.class);
                Log.e(TAG, String.valueOf(scf));
                int code = scf.getErrNum();
                if(code == 0){
                    return true;
                }else{
                    return false;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String addInfo =  response.body().string();
                    SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addInfo, SubGroupConfigInfo.class);
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
