package com.soo.learn.recylerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soo.learn.R;

/**
 * Created by SongYuHai on 2016/12/7.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView tv;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        tv= (TextView) itemView.findViewById(R.id.id_item_list_title);
    }
    public static RecyclerViewHolder getInstance(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_list,parent,false);
        return new RecyclerViewHolder(view);
    }
   public void onBindViewHolder(Object item){
       tv.setText((String)item);
   }
}
