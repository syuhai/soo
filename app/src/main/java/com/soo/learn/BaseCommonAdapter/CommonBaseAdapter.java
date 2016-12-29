package com.soo.learn.BaseCommonAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by SongYuHai on 2016/12/5.
 */

public class CommonBaseAdapter<T> extends BaseAdapter {
    private List<T> itemList;
    private IViewHolder viewHolder;
    public CommonBaseAdapter(List<T> itemList,IViewHolder viewHolder) {
        this.itemList = itemList;
        this.viewHolder=viewHolder;
    }

    @Override
    public int getCount() {
        return this.itemList==null?0:this.itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return viewHolder.getView(i,view,viewGroup,itemList.get(i));
    }
}
