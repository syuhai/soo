package com.soothe.baseadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by SongYuHai on 2016/12/5.
 * 通用基类adapter
 */

public abstract class CommBaseAdapter<T> extends BaseAdapter {
    protected List<T> itemList;
    protected int layoutId=-1;
    public CommBaseAdapter(){
    }
    public CommBaseAdapter(List<T> itemList, int layoutId){
        this.itemList=itemList;
        this.layoutId=layoutId;
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
        ViewHolder vh=ViewHolder.getInstance(view,layoutId==-1?getLayoutId():layoutId,viewGroup.getContext());
        convert(vh,itemList.get(i));
        return vh.getView();
    }

    protected abstract void convert(ViewHolder vh, T item);

    protected  int getLayoutId(){
     return -1;
    }

}
