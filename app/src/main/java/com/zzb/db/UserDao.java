package com.zzb.db;

import com.zzb.bean.AndroidUserInfo;

/**
 * Created by Administrator on 2017/8/16 0016.
 */

public interface UserDao {
    /**
     * 根据 用户名 查询对象
     *
     * @param name
     * @return
     */
    AndroidUserInfo findByName(String name);

    /**
     * 修改数据
     *
     * @param androidUserInfo
     */
    boolean update(AndroidUserInfo androidUserInfo);

    /**
     * 添加数据
     *
     * @param androidUserInfo
     */
    boolean save(AndroidUserInfo androidUserInfo);
}
