package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.Host;
import com.zzb.bean.HostNh;
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

public class HostHttp {

    private static final String TAG = "HostHttp";

    /**
     * 查询所有用户信息（get）
     *
     * @param u 用户登录信息
     * @param
     * @return
     */
    public static List<Host> SelectAllHost(User u) {
        String selcetHostUrl = TotalUrl.getsURLHEAD() + "/Host/BaseQuerys?PageSize=999999&PageIndex=1&Order=1";

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selcetHostUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selcetHostUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo = response.body().string();
               // Log.e(TAG, "LoginInfo:" + LoginInfo);
                Gson g = new Gson();
                List<Host> host = g.fromJson(LoginInfo, new TypeToken<List<Host>>() {
                }.getType());
                //Log.e(TAG, "查询主机:" + host.get(0).getsS_EnabledMark());
                //Log.e(TAG, "请求服务器成功，做相应处理");
                //Log.e(TAG, "查询主机" + String.valueOf(host));
                if (host != null) {
                    return host;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String LoginInfo = response.body().string();
                    // Log.e(TAG, "LoginInfo:" + LoginInfo);
                    Gson g = new Gson();
                    List<Host> host = g.fromJson(LoginInfo, new TypeToken<List<Host>>() {
                    }.getType());
                    //Log.e(TAG, "查询主机:" + host.get(0).getsS_EnabledMark());
                    //Log.e(TAG, "请求服务器成功，做相应处理");
                    //Log.e(TAG, "查询主机" + String.valueOf(host));
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

    /***
     * 查询主机能耗
     *
     * @param u
     * @return
     */
    public static List<HostNh> SelectHtNh(User u) {
        String selcetHostNhUrl = TotalUrl.getsURLHEAD() + "/Host/NergQuerys?PageSize=999999&PageIndex=1&Order=1";

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selcetHostNhUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selcetHostNhUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo = response.body().string();
                Log.e(TAG, "LoginInfo:" + LoginInfo);
                Gson g = new Gson();
                List<HostNh> nh = g.fromJson(LoginInfo, new TypeToken<List<HostNh>>() {
                }.getType());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, "查询主机能耗" + String.valueOf(nh));
                if (nh != null) {
                    return nh;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String LoginInfo = response.body().string();
                    Log.e(TAG, "LoginInfo:" + LoginInfo);
                    Gson g = new Gson();
                    List<HostNh> nh = g.fromJson(LoginInfo, new TypeToken<List<HostNh>>() {
                    }.getType());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, "查询主机能耗" + String.valueOf(nh));
                    if (nh != null) {
                        return nh;
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

    /**
     * 添加
     * @param host
     * @param user
     * @return
     */
    public static boolean adHost(Host host, User user) {
        //创建URL
        String addHostUrl =TotalUrl.getsURLHEAD() + "/Host/Add?fullName=" + host.getsS_FullName()
                + "&regPackage=" + host.getsS_RegPackage()
                + "&heart=" + host.getsS_Heart()
                + "&longitude=" + host.getsS_Longitude()
                + "&latitude=" + host.getsS_Latitude()
                + "&phone=" + host.getsS_Phone()
                + "&address=" + host.getsS_Address()
                + "&description=" + host.getsS_Description()
                + "&enabledMark=" + host.getsS_EnabledMark()
                + "&organize_Id=" + host.getsSL_Organize_S_Id();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(addHostUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+addHostUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String addHost =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(addHost, User.class);
                Log.e(TAG, String.valueOf(user1));
                int code = user1.getCode();
                String a = user1.getMessage();
                String b = "S_Year";
                if (a.contains(b)){
                    return true;
                }
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
                    String a = user1.getMessage();
                    String b = "S_Year";
                    if (a.contains(b)){
                        return true;
                    }
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

    /**
     * 删除
     * @param deleteID
     * @param user
     * @return
     */
    public static boolean DeleteHost(String deleteID, User user) {
        String deleteHostUrl=TotalUrl.getsURLHEAD()+"/Host/Remove?pack="+deleteID;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(deleteHostUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+deleteHostUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String deleteInfo =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(deleteInfo, User.class);
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
                    String deleteInfo =  response.body().string();
                    User user1 = GsonUtil.parseJsonWithGson(deleteInfo, User.class);
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
    public static boolean UpdataHost(Host host,User user) {
        String updataHostUrl = TotalUrl.getsURLHEAD() + "/Host/Modify?id=" + host.getsS_Id()
                + "&fullName=" + host.getsS_FullName()
                + "&regPackage=" + host.getsS_RegPackage()
                + "&heart=" + host.getsS_Heart()
                + "&longitude=" + host.getsS_Longitude()
                + "&latitude=" + host.getsS_Latitude()
                + "&phone=" + host.getsS_Phone()
                + "&address=" + host.getsS_Address()
                + "&description=" + host.getsS_Description()
                + "&enabledMark=" + host.getsS_EnabledMark()
                + "&organize_Id=" + host.getsSL_Organize_S_Id();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(updataHostUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+updataHostUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String updInfo =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(updInfo, User.class);
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
                    String updInfo =  response.body().string();
                    User user1 = GsonUtil.parseJsonWithGson(updInfo, User.class);
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
}
