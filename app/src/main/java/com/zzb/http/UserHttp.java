package com.zzb.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzb.bean.SelectUser;
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
 * Created by Administrator on 2017/8/16 0016.
 */

public class UserHttp {
    private static final String TAG = "UserHttp";

    /**
     * 根据id查询用户信息(get)
     */
    public static List<SelectUser> thisUser(User u) {
        String url = TotalUrl.getsURLHEAD() + "/Account/Query?id=" + u.getdate().getUserID();
        Log.e(TAG,url);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + url);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo = response.body().string();
                Log.e(TAG, "LoginInfo:" + LoginInfo);
                Gson g = new Gson();
                List<SelectUser> user = g.fromJson(LoginInfo, new TypeToken<List<SelectUser>>() {
                }.getType());
                Log.e(TAG, "查询:" + user.get(0).getsS_EnabledMark());
                Log.e(TAG, "请求服务器成功，做相应处理");

                Log.e(TAG, String.valueOf(user));
                if (user != null) {
                    return user;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String LoginInfo = response.body().string();
                    Log.e(TAG, "LoginInfo:" + LoginInfo);
                    Gson g = new Gson();
                    List<SelectUser> user = g.fromJson(LoginInfo, new TypeToken<List<SelectUser>>() {
                    }.getType());
                    Log.e(TAG, "查询:" + user.get(0).getsS_EnabledMark());
                    Log.e(TAG, "请求服务器成功，做相应处理");

                    Log.e(TAG, String.valueOf(user));
                    if (user != null) {
                        return user;
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
     *添加用户信息（post）
     * @param user 用户登录信息
     * @param selectUser 添加用户信息
     * @return
     */
    public static boolean addUser(User user, SelectUser selectUser){
        String url_adduser = TotalUrl.getsURLHEAD()+"/Account/Add?account=" +selectUser.getsS_Account()+
                "&password=" +selectUser.getsS_Password()+
                "&nickName=" +selectUser.getsS_NickName()+
                "&headIcon=" +selectUser.getsS_HeadIcon()+
                "&realName=" +selectUser.getsS_RealName()+
                "&mobilePhone=" +selectUser.getsS_MobilePhone()+
                "&email=" +selectUser.getsS_Email()+
                "&enabledMark="+selectUser.getsS_EnabledMark()+
                "&description=" +selectUser.getsS_Description()+
                "&organize_Id=" +selectUser.getsSL_Organize_S_Id()+
                "&role_Id=" +selectUser.getsSL_Role_S_Id()+
                "&uSER_RANKID="+selectUser.getsS_USER_RANKID();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(url_adduser)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + user.getdate().getApiKey())
                    .addHeader("roleid", user.getdate().getRoleId())
                    .addHeader("userid", user.getdate().getUserID())
                    .addHeader("organizeid", user.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+url_adduser);
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

    /**
     * 查询所有用户信息（get）
     * @param u 用户登录信息
     * @param
     * @return
     */
    public static List<SelectUser> getAllUser(User u){
        String allUserUrl =TotalUrl.getsURLHEAD()+"/Account/Querys?PageSize=9999999&PageIndex=1"+"&Order=0";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(allUserUrl)
                    .addHeader("Authorization", "1 " + u.getdate().getApiKey())
                    .addHeader("roleid", u.getdate().getRoleId())
                    .addHeader("userid", u.getdate().getUserID())
                    .addHeader("organizeid", u.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG, "URL:" + allUserUrl);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String LoginInfo = response.body().string();
                Log.e(TAG, "LoginInfo:" + LoginInfo);
                Gson g = new Gson();
                List<SelectUser> user = g.fromJson(LoginInfo, new TypeToken<List<SelectUser>>() {
                }.getType());
                Log.e(TAG, "查询:" + user.get(0).getsS_EnabledMark());
                Log.e(TAG, "请求服务器成功，做相应处理");
                Log.e(TAG, String.valueOf(user));
                if (user != null) {
                    return user;
                }
            } else {
                Log.e(TAG,"重新登录进来了");
                User users = LoginHttp.LoginPost(TotalUrl.Name,TotalUrl.getPassWord(),TotalUrl.getsURLHEAD());
                TotalUrl.setUser(users);
                if (response.isSuccessful()) {
                    String LoginInfo = response.body().string();
                    Log.e(TAG, "LoginInfo:" + LoginInfo);
                    Gson g = new Gson();
                    List<SelectUser> user = g.fromJson(LoginInfo, new TypeToken<List<SelectUser>>() {
                    }.getType());
                    Log.e(TAG, "查询:" + user.get(0).getsS_EnabledMark());
                    Log.e(TAG, "请求服务器成功，做相应处理");
                    Log.e(TAG, String.valueOf(user));
                    if (user != null) {
                        return user;
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
     * 修改用户信息（post）
     * @param updUser 修改用户信息
     * @param s 登录用户信息
     * @return
     */
    public static boolean UpdataUser(SelectUser updUser,User s) {
        String updataUserUrl=TotalUrl.getsURLHEAD()+"/Account/Modify?id="+updUser.getsS_Id()+"&account="+updUser.getsS_Account()+"&password="+updUser.getsS_Password()+"&nickName="+updUser.getsS_NickName()+"&headIcon="+updUser.getsS_HeadIcon()+"&realName="+updUser.getsS_RealName()+"&mobilePhone="+updUser.getsS_MobilePhone()+"&email="+updUser.getsS_Email()+"&enabledMark="+updUser.getsS_EnabledMark()+"&description="+updUser.getsS_Description()+"&organize_Id="+updUser.getsSL_Organize_S_Id()+"&role_Id="+updUser.getsSL_Role_S_Id()+"&uSER_RANKID="+updUser.getsS_USER_RANKID();

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(updataUserUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + s.getdate().getApiKey())
                    .addHeader("roleid", s.getdate().getRoleId())
                    .addHeader("userid", s.getdate().getUserID())
                    .addHeader("organizeid", s.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+updataUserUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String updaInfo =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(updaInfo, User.class);
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
                    String updaInfo =  response.body().string();
                    User user1 = GsonUtil.parseJsonWithGson(updaInfo, User.class);
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
    /**
     * 删除用户 （post）
     * @param id 用户ID
     * @param s
     * @return
     */
    public static boolean DeleteUser(String id,User s) {
        String deleteUrl = TotalUrl.getsURLHEAD()+"/Account/Remove?ID="+id;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(deleteUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + s.getdate().getApiKey())
                    .addHeader("roleid", s.getdate().getRoleId())
                    .addHeader("userid", s.getdate().getUserID())
                    .addHeader("organizeid", s.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+deleteUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String deleteInfo =  response.body().string();
                User deleteUser = GsonUtil.parseJsonWithGson(deleteInfo, User.class);
                Log.e(TAG, String.valueOf(deleteUser));
                int code = deleteUser.getCode();
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
                    User deleteUser = GsonUtil.parseJsonWithGson(deleteInfo, User.class);
                    Log.e(TAG, String.valueOf(deleteUser));
                    int code = deleteUser.getCode();
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
     * 修改用户信息（post）
     * @param updUser 修改用户信息
     * @param s 登录用户信息
     * @return
     */
    public static boolean UpdataUserProj(SelectUser updUser,User s) {
        String updataUserProjUrl=TotalUrl.getsURLHEAD()+"/Account/Modify?id="+updUser.getsS_Id()+"&project="+updUser.getsS_Project()+"&account="+updUser.getsS_Account()+"&password="+updUser.getsS_Password()+"&nickName="+updUser.getsS_NickName()+"&headIcon="+updUser.getsS_HeadIcon()+"&realName="+updUser.getsS_RealName()+"&mobilePhone="+updUser.getsS_MobilePhone()+"&email="+updUser.getsS_Email()+"&enabledMark="+updUser.getsS_EnabledMark()+"&description="+updUser.getsS_Description()+"&organize_Id="+updUser.getsSL_Organize_S_Id()+"&role_Id="+updUser.getsSL_Role_S_Id()+"&uSER_RANKID="+updUser.getsS_USER_RANKID();

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(updataUserProjUrl)
                    .post(requestBody)
                    .addHeader("Authorization", "1 " + s.getdate().getApiKey())
                    .addHeader("roleid", s.getdate().getRoleId())
                    .addHeader("userid", s.getdate().getUserID())
                    .addHeader("organizeid", s.getdate().getOrganizeId())
                    .addHeader("timestamp", "1")
                    .build();
            Log.e(TAG,"URL:"+updataUserProjUrl);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String updaInfo =  response.body().string();
                User user1 = GsonUtil.parseJsonWithGson(updaInfo, User.class);
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
                    String updaInfo =  response.body().string();
                    User user1 = GsonUtil.parseJsonWithGson(updaInfo, User.class);
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



