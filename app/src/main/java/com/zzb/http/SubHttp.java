package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.ControlS;
import com.zzb.bean.SubGroupConfigInfo;
import com.zzb.bean.SubNh;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.util.GsonUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/21 0021.
 */

public class SubHttp {
    private static final String TAG = "SubHttp";

    // 查询分控信息
    public static List<ControlS> SelectAllSub(User u) {
        String selcetSubUrl = TotalUrl.getsURLHEAD()+"/Sub/Querys?PageSize=999999&PageIndex=1&Order=1";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selcetSubUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selcetSubUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String SubInfo = response.body().string();
                Gson g = new Gson();
                List<ControlS> controlS = g.fromJson(SubInfo, new TypeToken<List<ControlS>>() {
                }.getType());
                //Log.e(TAG, "请求服务器成功，做相应处理");
                //Log.e(TAG, "查询分控："+String.valueOf(controlS));
                if (controlS != null) {
                    return controlS;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String SubInfo = response.body().string();
                    Gson g = new Gson();
                    List<ControlS> controlS = g.fromJson(SubInfo, new TypeToken<List<ControlS>>() {
                    }.getType());
                    //Log.e(TAG, "请求服务器成功，做相应处理");
                    //Log.e(TAG, "查询分控："+String.valueOf(controlS));
                    if (controlS != null) {
                        return controlS;
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


    // 查询分控能耗信息
    public static List<SubNh> SelectFkNh(User u) {
        String selcetSubNhUrl = TotalUrl.getsURLHEAD()+"/Sub/NergQuerys?PageSize=99999999&PageIndex=1&Order=1";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selcetSubNhUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selcetSubNhUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String subInfo = response.body().string();
                Gson g = new Gson();
                List<SubNh> subNh = g.fromJson(subInfo, new TypeToken<List<SubNh>>() {
                }.getType());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, "查询分控能耗："+String.valueOf(subNh));
                if (subNh != null) {
                    return subNh;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String subInfo = response.body().string();
                    Gson g = new Gson();
                    List<SubNh> subNh = g.fromJson(subInfo, new TypeToken<List<SubNh>>() {
                    }.getType());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, "查询分控能耗："+String.valueOf(subNh));
                    if (subNh != null) {
                        return subNh;
                    }
                }
            }
        }catch (SocketTimeoutException e){
            e.printStackTrace();
            return null;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //根据注册包查询分控信息
    public static List<ControlS> selectPackSub(User u,String pack){
        String selectOneContUrl =TotalUrl.getsURLHEAD()+"/Sub/Query?Pack="+pack;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selectOneContUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selectOneContUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String SubInfo = response.body().string();
                Gson g = new Gson();
                List<ControlS> controlS = g.fromJson(SubInfo, new TypeToken<List<ControlS>>() {
                }.getType());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, "根据注册包查询分控："+String.valueOf(controlS));
                if (controlS != null) {
                    return controlS;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String SubInfo = response.body().string();
                    Gson g = new Gson();
                    List<ControlS> controlS = g.fromJson(SubInfo, new TypeToken<List<ControlS>>() {
                    }.getType());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, "根据注册包查询分控："+String.valueOf(controlS));
                    if (controlS != null) {
                        return controlS;
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











    //添加分控信息
    public static boolean addContl(ControlS contr,User user) {
        //创建URL
        String addContlUrl=TotalUrl.getsURLHEAD()+"/Sub/Add?id="+contr.getsS_Id()
                + "&number="+contr.getsS_Number()
                + "&fullName="+contr.getsS_FullName()
                + "&poleName="+contr.getsS_PoleName()
                + "&address="+contr.getsS_Address()
                + "&description="+contr.getsS_Description()
                + "&enabledMark="+contr.getsS_EnabledMark()
                + "&longitude="+contr.getsS_Longitude()
                + "&latitude="+contr.getsS_Latitude()
                + "&hostBast_RegPackage="+contr.getsSL_HostBast_RegPackage();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(addContlUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+addContlUrl);
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



    //删除(post)
    public static boolean DeleteSub(String deleteID, User user) {
        String deleteSubUrl=TotalUrl.getsURLHEAD()+"/Sub/Remove?id="+deleteID;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(deleteSubUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+deleteSubUrl);
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

    //修改
    public static boolean UpdataSub(ControlS controlS,User user ) {
        String updataSubUrl=TotalUrl.getsURLHEAD()+"/Sub/Modify?id="+controlS.getsS_Id()
                + "&number="+controlS.getsS_Number()
                + "&fullName="+controlS.getsS_FullName()
                + "&poleName="+controlS.getsS_PoleName()
                + "&address="+controlS.getsS_Address()
                + "&description="+controlS.getsS_Description()
                + "&enabledMark="+controlS.getsS_EnabledMark()
                + "&longitude="+controlS.getsS_Longitude()
                + "&latitude="+controlS.getsS_Latitude()
                + "&hostBast_RegPackage="+controlS.getsSL_HostBast_RegPackage();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(updataSubUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+updataSubUrl);
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
    //获取分控信息
    public static boolean getInfoSub(String value) {
        String getSubInfo=TotalUrl.getsURLHEAD()+"/Send/ReadSubControl?Value="+value;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(getSubInfo)
                    .post(requestBody)
                    .build();
            Log.e(TAG,"URL:"+getSubInfo);
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
