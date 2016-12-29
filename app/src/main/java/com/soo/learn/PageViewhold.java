package com.soo.learn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soo.learn.BaseCommonAdapter.IViewHolder;

/**
 * Created by SongYuHai on 2016/12/5.
 */

public class PageViewhold implements IViewHolder {
    private TextView titleTv;
    private Context mContext;
    private LayoutInflater inflater;

    public PageViewhold(Context mContext) {
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup, Object object) {
        ViewHolder vh;
        if(view==null){
            vh=new ViewHolder();
            view= inflater.inflate(R.layout.page_item,null);
            view.setTag(vh);
            vh.titleTv= (TextView) view.findViewById(R.id.title);
        }else{
            vh= (ViewHolder) view.getTag();
        }
            vh.titleTv.setText((CharSequence) object);
        return view;
    }

    @Override
    public void refreshView() {

    }

    public class ViewHolder{
        TextView titleTv;
    }
}
