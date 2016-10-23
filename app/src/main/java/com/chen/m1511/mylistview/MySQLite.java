package com.chen.m1511.mylistview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by m1511 on 2016/10/16.
 */

class MySQLite extends SQLiteOpenHelper {
    private Context mContext;

    MySQLite(Context context) {
        super(context, "music_msg.db", null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table music_msg(_id integer primary key autoincrement, singer varchar, songname varchar, url varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(mContext, "onUpgrade", Toast.LENGTH_SHORT).show();
    }
}
