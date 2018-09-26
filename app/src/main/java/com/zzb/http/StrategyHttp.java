package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.Strategy;
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

public class StrategyHttp {

    private static final String TAG = "StrategyHttp";

    // 查询策略信息
    public static List<Strategy> SelectAllStrategy(User u) {
        String selcetStrategyUrl = TotalUrl.getsURLHEAD() +"/Strategy/Querys?PageSize=999999&PageIndex=1&Order=1";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selcetStrategyUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selcetStrategyUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo = response.body().string();
                Log.e(TAG, "LoginInfo:" + LoginInfo);
                Gson g = new Gson();
                List<Strategy> host = g.fromJson(LoginInfo, new TypeToken<List<Strategy>>() {
                }.getType());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, "查询策略" + String.valueOf(host));
                if (host != null) {
                    return host;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String LoginInfo = response.body().string();
                    Log.e(TAG, "LoginInfo:" + LoginInfo);
                    Gson g = new Gson();
                    List<Strategy> host = g.fromJson(LoginInfo, new TypeToken<List<Strategy>>() {
                    }.getType());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, "查询策略" + String.valueOf(host));
                    if (host != null) {
                        return host;
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


    // 添加策略信息
    public static boolean addStrategy(Strategy strategy, User user) {

        // 创建URL
        String addStrategyUrl = TotalUrl.getsURLHEAD()+"/Strategy/Add?value.number=" + strategy.getsS_Number()
                + "&value.fullName=" + strategy.getsS_FullName() + "&value.enabled=" + strategy.getsS_Enabled()
                + "&value.priority=" + strategy.getsS_Priority() + "&value.cycle=" + strategy.getsS_Cycle()
                + "&value.startYear=" + strategy.getsS_StartYear() + "&value.startMonth=" + strategy.getsS_StartMonth()
                + "&value.startDay=" + strategy.getsS_StartDay() + "&value.startWeek=" + strategy.getsS_StartWeek()
                + "&value.endYear=" + strategy.getsS_EndYear() + "&value.endMonth=" + strategy.getsS_EndMonth()
                + "&value.endDay=" + strategy.getsS_EndDay() + "&value.endWeek=" + strategy.getsS_EndWeek()
                + "&value.reference=" + strategy.getsS_Reference() + "&value.hour=" + strategy.getsS_Hour()
                + "&value.minute=" + strategy.getsS_Minute() + "&value.groupMask=" + strategy.getsS_GroupMask()
                + "&value.dimEnabled=" + strategy.getsS_DimEnabled() + "&value.dimValue=" + strategy.getsS_DimValue()
                + "&value.switch=" + strategy.getsS_Switch() + "&value.loopMsk=" + strategy.getsS_LoopMsk()
                + "&value.description=" + strategy.getsS_Description() + "&value.organize_Id="
                + strategy.getsSL_Organize_S_Id();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(addStrategyUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+addStrategyUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addHost =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(addHost, User.class);
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
                    String addHost =  response.body().string();
                    User user1 = GsonUtil.parseJsonWithGson(addHost, User.class);
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
    public static boolean UpdataStrategy(Strategy strategy, User user) {
        // 创建URL
        String updStrategyUrl = TotalUrl.getsURLHEAD()+"/Strategy/Modify?value.id=" + strategy.getsS_Id()
                + "&value.number=" + strategy.getsS_Number() + "&value.fullName=" + strategy.getsS_FullName()
                + "&value.enabled=" + strategy.getsS_Enabled() + "&value.priority=" + strategy.getsS_Priority()
                + "&value.cycle=" + strategy.getsS_Cycle() + "&value.startYear=" + strategy.getsS_StartYear()
                + "&value.startMonth=" + strategy.getsS_StartMonth() + "&value.startDay=" + strategy.getsS_StartDay()
                + "&value.startWeek=" + strategy.getsS_StartWeek() + "&value.endYear=" + strategy.getsS_EndYear()
                + "&value.endMonth=" + strategy.getsS_EndMonth() + "&value.endDay=" + strategy.getsS_EndDay()
                + "&value.endWeek=" + strategy.getsS_EndWeek() + "&value.reference=" + strategy.getsS_Reference()
                + "&value.hour=" + strategy.getsS_Hour() + "&value.minute=" + strategy.getsS_Minute()
                + "&value.groupMask=" + strategy.getsS_GroupMask() + "&value.dimEnabled=" + strategy.getsS_DimEnabled()
                + "&value.dimValue=" + strategy.getsS_DimValue() + "&value.switch=" + strategy.getsS_Switch()
                + "&value.loopMsk=" + strategy.getsS_LoopMsk() + "&value.description=" + strategy.getsS_Description()
                + "&value.organize_Id=" + strategy.getsSL_Organize_S_Id();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(updStrategyUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+updStrategyUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addHost =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(addHost, User.class);
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
                    String addHost =  response.body().string();
                    User user1 = GsonUtil.parseJsonWithGson(addHost, User.class);
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


    // 删除
    public static boolean DeleteStrategy(String deleteID, User user) {
        String deleteGroupUrl = TotalUrl.getsURLHEAD()+"/Strategy/Remove?id=" + deleteID;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(deleteGroupUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+deleteGroupUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addHost =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(addHost, User.class);
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
                    String addHost =  response.body().string();
                    User user1 = GsonUtil.parseJsonWithGson(addHost, User.class);
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
    //根据项目ID查询策略信息查询所属
    public static List<Strategy> getIDProStrategy(User u,String proid) {
        String selcetProStUrl =TotalUrl.getsURLHEAD()+"/Strategy/QuerysProject?ProjectID="+proid+"&PageSize=999999&PageIndex=1&Order=1";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selcetProStUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selcetProStUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo = response.body().string();
                Log.e(TAG, "LoginInfo:" + LoginInfo);
                Gson g = new Gson();
                List<Strategy> host = g.fromJson(LoginInfo, new TypeToken<List<Strategy>>() {
                }.getType());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, "查询策略" + String.valueOf(host));
                if (host != null) {
                    return host;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String LoginInfo = response.body().string();
                    Log.e(TAG, "LoginInfo:" + LoginInfo);
                    Gson g = new Gson();
                    List<Strategy> host = g.fromJson(LoginInfo, new TypeToken<List<Strategy>>() {
                    }.getType());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, "查询策略" + String.valueOf(host));
                    if (host != null) {
                        return host;
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

    public static boolean dowStrategy(String value) {
        String dowUrl = TotalUrl.getsURLHEAD()+"/Send/StrategyLoad?Value="+value;
        // 发送URL
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(dowUrl)
                    .post(requestBody)
                    .build();
            Log.e(TAG,"URL:"+dowUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addHost =  response.body().string();
                SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                System.out.println(scf);
                System.err.println("请求服务器成功，做相应处理");
                int code = scf.getErrNum();
                if (code == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String addHost =  response.body().string();
                    SubGroupConfigInfo scf = GsonUtil.parseJsonWithGson(addHost, SubGroupConfigInfo.class);
                    System.out.println(scf);
                    System.err.println("请求服务器成功，做相应处理");
                    int code = scf.getErrNum();
                    if(code == 0){
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
