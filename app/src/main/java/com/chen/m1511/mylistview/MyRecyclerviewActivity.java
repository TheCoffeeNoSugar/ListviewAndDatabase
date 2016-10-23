package com.chen.m1511.mylistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chen.m1511.mylistview.adapter.RecyclerviewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m1511 on 2016/10/16.
 */

public class MyRecyclerviewActivity extends AppCompatActivity {

    private List<String> mStringList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerviewAdapter mRecyclerviewAdapter;
    private Button mButton;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        mRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerview);
        mButton = (Button) findViewById(R.id.add_recy);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listview_menu:
                        startActivity(new Intent(MyRecyclerviewActivity.this, MyListviewActivity.class));
                        break;
                    case R.id.recyclerview_menu:
                        startActivity(new Intent(MyRecyclerviewActivity.this, MyRecyclerviewActivity.class));
                        break;
                }
                return true;
            }
        });

        for (int i = 1; i < 15; i++) {
            mStringList.add("recyclerview的数据" + i);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerviewAdapter = new RecyclerviewAdapter(mStringList);

        mRecyclerView.setAdapter(mRecyclerviewAdapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStringList.add("recyclerview_new");
                mRecyclerviewAdapter.notifyItemChanged(mStringList.size());
                mRecyclerView.scrollToPosition(mStringList.size() - 1);     //position的位置从0算起，所以要减一
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
