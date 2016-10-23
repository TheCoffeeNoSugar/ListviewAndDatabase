package com.chen.m1511.mylistview;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by m1511 on 2016/10/16.
 */

public class MyListviewActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Map<String, String>> mStringList = new ArrayList<>();
    private ListView mListView;
    private Button mBtn_insert;
    private Button mBtn_query;
    private EditText mEt_songName;
    private EditText mEt_singer;
    private EditText mEt_input;
    private EditText mEt_dialog_songName;
    private EditText mEt_dialog_singer;
    private Toolbar mToolbar;
    private SimpleCursorAdapter mSimpleCursorAdapter;
    private SQLiteDatabase mDbWriter;
    private SQLiteDatabase mDbReader;
    private MySQLite mMySQLite;
    private String TAG = "TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        initView();
        initEvent();

        //单击修改item
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                View mView = View.inflate(MyListviewActivity.this, R.layout.dialog_et_layout, null);       //将放置了两个EditText的布局dialog_et_layout渲染成view对象
                mEt_dialog_songName = (EditText) mView.findViewById(R.id.et_dialog_songname);       //要用对应布局的view对象去findViewById获取控件对象
                mEt_dialog_singer = (EditText) mView.findViewById(R.id.et_dialog_singer);
                mEt_dialog_songName.setText(((TextView) view.findViewById(R.id.songname)).getText());   //获取并显示原来的歌曲
                mEt_dialog_singer.setText(((TextView) view.findViewById(R.id.singer)).getText());       //获取并显示原来的歌手
                new AlertDialog.Builder(MyListviewActivity.this)
                        .setTitle("提示")
                        .setMessage("是否修改数据")
                        .setView(mView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateData(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });


        //长按删除item
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(MyListviewActivity.this)
                        .setTitle("提示")
                        .setMessage("是否删除该项")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteData(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });

        //ListviewActivity与RecyclerviewActivity的切换
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listview_menu:
                        startActivity(new Intent(MyListviewActivity.this, MyListviewActivity.class));
                        break;
                    case R.id.recyclerview_menu:
                        startActivity(new Intent(MyListviewActivity.this, MyRecyclerviewActivity.class));
                        break;
                }
                return true;
            }
        });


    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mListView = (ListView) findViewById(R.id.myListview);
        mBtn_insert = (Button) findViewById(R.id.btn_insert);
        mBtn_query = (Button) findViewById(R.id.btn_query);
        mEt_songName = (EditText) findViewById(R.id.et_songname);
        mEt_singer = (EditText) findViewById(R.id.et_singer);
        mEt_input = (EditText) findViewById(R.id.et_query);

    }

    private void initEvent() {
        setSupportActionBar(mToolbar);
        mBtn_insert.setOnClickListener(this);
        mBtn_query.setOnClickListener(this);

        mMySQLite = new MySQLite(this);
        mDbWriter = mMySQLite.getWritableDatabase();
        mDbReader = mMySQLite.getReadableDatabase();

        mSimpleCursorAdapter = new SimpleCursorAdapter(MyListviewActivity.this, R.layout.listview_sql_item, null,
                new String[]{"songname", "singer"}, new int[]{R.id.songname, R.id.singer}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        mListView.setAdapter(mSimpleCursorAdapter);     //给ListView设置适配器
        refreshListview();      //自定义的方法，用于当数据列表改变时刷新ListView

    }

    //刷新数据列表
    public void refreshListview() {
        Cursor mCursor = mDbWriter.query("music_msg", null, null, null, null, null, null);
        mSimpleCursorAdapter.changeCursor(mCursor);
    }

    //增
    public void insertData() {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("songname", mEt_songName.getText().toString().trim());
        mContentValues.put("singer", mEt_singer.getText().toString().trim());
        mDbWriter.insert("music_msg", null, mContentValues);
        refreshListview();
    }

    //删
    public void deleteData(int positon) {
        Cursor mCursor = mSimpleCursorAdapter.getCursor();
        mCursor.moveToPosition(positon);
        int itemId = mCursor.getInt(mCursor.getColumnIndex("_id"));
        mDbWriter.delete("music_msg", "_id=?", new String[]{itemId + ""});
        refreshListview();
    }

    //改
    public void updateData(int positon) {
        Cursor mCursor = mSimpleCursorAdapter.getCursor();
        mCursor.moveToPosition(positon);
        int itemId = mCursor.getInt(mCursor.getColumnIndex("_id"));
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("songname", mEt_dialog_songName.getText().toString().trim());
        mContentValues.put("singer", mEt_dialog_singer.getText().toString().trim());
        mDbWriter.update("music_msg", mContentValues, "_id=?", new String[]{itemId + ""});
        refreshListview();
    }

    //查
    public void queryData() {
        String mInput = mEt_input.getText().toString().trim();
        //第二个参数是你需要查找的列
        //第三和第四个参数确定是从哪些行去查找第二个参数的列
        Cursor mCursor1 = mDbReader.query("music_msg", new String[]{"singer"}, "songname=?", new String[]{mInput}, null, null, null);
        Cursor mCursor2 = mDbReader.query("music_msg", new String[]{"songname"}, "singer=?", new String[]{mInput}, null, null, null);
        if (mCursor1.getCount() > 0) {
            mStringList.clear();        //清空List
            while (mCursor1.moveToNext()) {     //游标总是在查询到的上一行
                Map<String, String> mMap = new HashMap<>();
                String output_singer = mCursor1.getString(mCursor1.getColumnIndex("singer"));
                mMap.put("tv1", mInput);
                mMap.put("tv2", output_singer);
                mStringList.add(mMap);
            }
            mCursor1.close();
        } else if (mCursor2.getCount() > 0) {
            mStringList.clear();        //清空List
            while (mCursor2.moveToNext()) {     //游标总是在查询到的上一行
                Map<String, String> mMap = new HashMap<>();
                String output_songname = mCursor2.getString(mCursor2.getColumnIndex("songname"));
                mMap.put("tv1", output_songname);
                mMap.put("tv2", mInput);
                mStringList.add(mMap);
            }
            mCursor2.close();
        } else {
            mStringList.clear();        //清空List
            Map<String, String> mMap = new HashMap<>();
            mMap.put("tv1", "未能查询到结果");
            mStringList.add(mMap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //点击添加按钮
            case R.id.btn_insert:
                insertData();
                mEt_songName.setText("");
                mEt_singer.setText("");
                break;
            //点击查询按钮
            case R.id.btn_query:
                queryData();
                mEt_input.setText("");      //清空输入框
                new AlertDialog.Builder(MyListviewActivity.this)
                        .setTitle("查询结果")
                        .setAdapter(new SimpleAdapter(MyListviewActivity.this, mStringList, R.layout.dialog_tv_layout, new String[]{"tv1", "tv2"}, new int[]{R.id.tv1_dialog, R.id.tv2_dialog}), null)
                        .setPositiveButton("确定", null)
                        .show();
                break;

        }
    }

}
