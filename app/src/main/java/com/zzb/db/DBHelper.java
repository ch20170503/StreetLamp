package com.zzb.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/16 0016.
 */

public class DBHelper  extends SQLiteOpenHelper {
    //创建数据库表
    public static final String CREATE_GROUP = "create table user("
            + "_id integer primary key autoincrement,"
            + "userName text," //用户账号
            + "imgs blob)";//保存为binary格式
    private Context mcontext;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GROUP);
     /*   initDataBase(db, mcontext);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = " DROP TABLE IF EXISTS user";
        db.execSQL(sql);
        onCreate(db);
    }
    //添加默认数据

/*    //将转换后的图片存入到数据库中(1)
    private void initDataBase(SQLiteDatabase db, Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.timg);
        ContentValues cv = new ContentValues();
        cv.put("userName","ceshi");
        cv.put("imgs", getPicture(drawable));
        db.insert("user", null, cv);
    }*/
/*    //将drawable转换成可以用来存储的byte[]类型
    private byte[] getPicture(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        return os.toByteArray();
    }*/
}
