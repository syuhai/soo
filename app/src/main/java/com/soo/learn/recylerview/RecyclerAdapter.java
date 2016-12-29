package com.soo.learn.recylerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by SongYuHai on 2016/12/7.
 */

public abstract class RecyclerAdapter<T,K extends BaseViewHolder> extends RecyclerView.Adapter<K> {
    private List<T> mDatas;
    private IOnItemClickListener onItemClickListener;
    private int mLayoutId=-1;
    public RecyclerAdapter(List<T> mDatas) {
        this.mDatas = mDatas;
    }
    public RecyclerAdapter(List<T> mDatas,int layoutId) {
        this.mDatas = mDatas;
        this.mLayoutId=layoutId;
    }

    public void setOnItemClickListener(IOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public K  onCreateViewHolder(ViewGroup parent, int viewType) {

        return (K)new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutId==-1?getLayoutId():mLayoutId,parent,false)); /*RecyclerViewHolder.getInstance(parent);*/
    }

    @Override
    public void onBindViewHolder(K holder,final int position) {
      convert(holder,mDatas.get(position));
      if(onItemClickListener!=null){
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  onItemClickListener.onItemClick(view ,position);
              }
          });
      }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }
    protected abstract void convert(K helper, T item);
    protected int getLayoutId(){
        return -1;
    }
    public interface  IOnItemClickListener{
        void onItemClick(View view, int position);
    }
}
