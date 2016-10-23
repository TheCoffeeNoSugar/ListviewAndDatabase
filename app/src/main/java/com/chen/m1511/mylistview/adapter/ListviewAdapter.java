package com.chen.m1511.mylistview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chen.m1511.mylistview.R;

import java.util.List;

/**
 * Created by m1511 on 2016/10/12.
 */

public class ListviewAdapter extends BaseAdapter {

    private List<String> mStringList;
    private Context mContext;

    public ListviewAdapter(Context mContext, List<String> stringList) {
        this.mContext = mContext;
        this.mStringList = stringList;
    }

    @Override
    public int getCount() {
        return mStringList.size();
    }

    @Override
    public Object getItem(int i) {
        return mStringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder mMyViewHolder = null;
        if (view == null) {
            mMyViewHolder = new MyViewHolder();
            view = View.inflate(mContext, R.layout.mylistview_item, null);
            mMyViewHolder.item_tv = (TextView) view.findViewById(R.id.item_tv);
            view.setTag(mMyViewHolder);
        } else {
            mMyViewHolder = (MyViewHolder) view.getTag();
        }
        mMyViewHolder.item_tv.setText(mStringList.get(i));
        return view;
    }

    private final class MyViewHolder {
        TextView item_tv;
    }
}
