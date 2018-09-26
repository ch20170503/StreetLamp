package com.zzb.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zzb.bean.AndroidUserInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16 0016.
 */

public class UserDaoImpl implements UserDao{
    /* 定义数据库操作对象 */
    private SQLiteDatabase mDatabase;

    public UserDaoImpl(Context context) {
        // 创建 DBHelper 对象 创建数据库
        DBHelper dbHelper = new DBHelper(context,"sl.db",null,1);
        this.mDatabase = dbHelper.getWritableDatabase();
    }

    @Override
    public AndroidUserInfo findByName(String name) {
        String sql = "select * from user where userName=?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{name});
        List<AndroidUserInfo> androidUserInfo = cursor2Contact(cursor);
        return (androidUserInfo != null && androidUserInfo.size() > 0) ? androidUserInfo.get(0) : null;
    }
    @Override
    public boolean update(AndroidUserInfo androidUserInfo) {
        try {
            String sql = "update user set imgs=? where userName=?";
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            androidUserInfo.getImphoto().compress(Bitmap.CompressFormat.JPEG, 100, os);
            mDatabase.execSQL(sql, new Object[]{os.toByteArray(),androidUserInfo.getName()});
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(AndroidUserInfo androidUserInfo) {
        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            androidUserInfo.getImphoto().compress(Bitmap.CompressFormat.JPEG, 100, os);
            String sql = "replace into user(userName,imgs) values (?,?)";
            mDatabase.execSQL(sql, new Object[]{androidUserInfo.getName(),os.toByteArray()});
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




    /**
     * 将游标对象转换成具体的对象
     *
     * @param cursor
     * @return
     */
    public List<AndroidUserInfo> cursor2Contact(Cursor cursor) {
        if (cursor == null) return null;
        List<AndroidUserInfo> deviceInfo = new ArrayList<>();
        AndroidUserInfo deviceInfo1;
        while (cursor.moveToNext()) {
            deviceInfo1 = new AndroidUserInfo();
            deviceInfo1.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            deviceInfo1.setName(cursor.getString(cursor.getColumnIndex("userName")));
            byte[] a = cursor.getBlob(cursor.getColumnIndex("imgs"));
            Bitmap bmp = BitmapFactory.decodeByteArray(a, 0, a.length);
            deviceInfo1.setImphoto(bmp);
            deviceInfo.add(deviceInfo1);
        }
        return deviceInfo;
    }
}
