package com.soo.learn;

import com.soo.learn.BaseCommonAdapter.CommBaseAdapter;
import com.soo.learn.BaseCommonAdapter.ViewHolder;

import java.util.List;

/**
 * Created by SongYuHai on 2016/12/6.
 */

public class PageAdapter extends CommBaseAdapter<String> {
    public PageAdapter(List<String> itemList) {
        this.itemList=itemList;
    }

    public PageAdapter(List<String> itemList,int layoutId) {
        this.itemList=itemList;
        this.layoutId=layoutId;
    }

    public PageAdapter() {
    }


    public void refreshView(List<String> itemList){
        this.itemList=itemList;
        notifyDataSetChanged();
    }


    @Override
    protected void convert(ViewHolder vh, String item) {
        vh.setText(R.id.title,item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_item;
    }
}
