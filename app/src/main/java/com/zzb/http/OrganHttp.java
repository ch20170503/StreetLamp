package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.Organ;
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

public class OrganHttp {
    private static final String TAG = "OrganHttp";

    // 查询机构信息
    public static List<Organ> SelectOrg(User u) {
        String selcetOrgPUrl = TotalUrl.getsURLHEAD()+"/Schema/Querys?PageSize=999999&PageIndex=1"+"&Order=1";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selcetOrgPUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selcetOrgPUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String OrgPInfo = response.body().string();
                Log.e(TAG, "OrgPInfo:" + OrgPInfo);
                Gson g = new Gson();
                List<Organ> organ = g.fromJson(OrgPInfo, new TypeToken<List<Organ>>() {
                }.getType());
                Log.e(TAG, "查询机构:" + organ.get(0).getsS_EnabledMark());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, "查询机构"+organ.toString());
                if (organ != null) {
                    return organ;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String OrgPInfo = response.body().string();
                    Log.e(TAG, "OrgPInfo:" + OrgPInfo);
                    Gson g = new Gson();
                    List<Organ> organ = g.fromJson(OrgPInfo, new TypeToken<List<Organ>>() {
                    }.getType());
                    Log.e(TAG, "查询机构:" + organ.get(0).getsS_EnabledMark());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, "查询机构"+organ.toString());
                    if (organ != null) {
                        return organ;
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



    //查询所有项目信息
    public static List<Organ> selectAllProj(User u){
        String selectAllProjUrl=TotalUrl.getsURLHEAD()+"/Schema/QuerysProjectt?CategoryId=1&PageSize=999999&PageIndex=1&Order=1";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(selectAllProjUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + selectAllProjUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String OrgPInfo = response.body().string();
                //Log.e(TAG, "OrgPInfo:" + OrgPInfo);
                Gson g = new Gson();
                List<Organ> organ = g.fromJson(OrgPInfo, new TypeToken<List<Organ>>() {
                }.getType());
               Log.e(TAG, "查询项目:" + organ.get(0).getsS_Id());
                //Log.e(TAG, "请求服务器成功，做相应处理");
                //Log.e(TAG, "查询项目"+String.valueOf(organ));
                if (organ != null) {
                    return organ;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User user = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(user);
                if (response.isSuccessful()) {
                    String OrgPInfo = response.body().string();
                    //Log.e(TAG, "OrgPInfo:" + OrgPInfo);
                    Gson g = new Gson();
                    List<Organ> organ = g.fromJson(OrgPInfo, new TypeToken<List<Organ>>() {
                    }.getType());
                    // Log.e(TAG, "查询项目:" + organ.get(0).getsS_EnabledMark());
                    //Log.e(TAG, "请求服务器成功，做相应处理");
                    //Log.e(TAG, "查询项目"+String.valueOf(organ));
                    if (organ != null) {
                        return organ;
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
     *  添加项目信息
     * @param user 用户信息
     * @param org 项目信息
     * @return
     */
    public static boolean addOrg(User user, Organ org){
        //创建URL
        String addOrgUrl = TotalUrl.getsURLHEAD()+"/Schema/Add?"
                + "parentId="+org.getsS_ParentId()
                + "&fullName="+org.getsS_FullName()
                + "&categoryId="+org.getsS_CategoryId()
                + "&managerId="+org.getsS_ManagerId()
                + "&telePhone="+org.getsS_TelePhone()
                + "&address="+org.getsS_Address()
                + "&enabledMark="+org.getsS_EnabledMark()
                + "&description="+org.getsS_Description()
                + "&layers="+org.getsS_Layers();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(addOrgUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+addOrgUrl);
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


    /***
     * 修改
     * @param org 项目信息
     * @param user 用户信息
     * @return
     */
    public static boolean UpdataOrg(Organ org,User user) {
        String upDataOrgUrl =TotalUrl.getsURLHEAD()+"/Schema/Modify?"
                + "id="+org.getsS_Id()
                + "&parentId="+org.getsS_ParentId()
                + "&fullName="+org.getsS_FullName()
                + "&categoryId="+org.getsS_CategoryId()
                + "&managerId="+org.getsS_ManagerId()
                + "&telePhone="+org.getsS_TelePhone()
                + "&address="+org.getsS_Address()
                + "&enabledMark="+org.getsS_EnabledMark()
                + "&description="+org.getsS_Description()
                + "&layers="+org.getsS_Layers();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(upDataOrgUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+upDataOrgUrl);
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





    /***
     * 删除
     * @param deleteID 项目id
     * @param user
     * @return
     */
    public static boolean DeleteOrgan(String deleteID, User user) {
        String deleteOrganUrl=TotalUrl.getsURLHEAD()+"/Schema/Remove?ID="+deleteID;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(deleteOrganUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+deleteOrganUrl);
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
}
