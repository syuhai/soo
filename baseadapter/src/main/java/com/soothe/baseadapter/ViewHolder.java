package com.soothe.baseadapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SongYuHai on 2016/12/6.
 */

public class ViewHolder {
    private View convertView;
    private SparseArray<View> subViews;
    private ViewHolder(Context context, int resId){
       convertView= LayoutInflater.from(context).inflate(resId,null);
       convertView.setTag(this);
       subViews=new SparseArray<>();
    }
    public static ViewHolder getInstance(View convertView,int resId,Context context){
        ViewHolder vh;
        if(convertView==null){
          vh=new ViewHolder(context,resId);
        }else{
          vh= (ViewHolder) convertView.getTag();
        }
        return vh;
    }

    public void setText(int viewId,String txt){
        TextView tv=getViewByID(viewId);
        tv.setText(txt);
    }

    public void setImageResource(int viewId,int resId){
        ImageView iv=getViewByID(viewId);
        iv.setBackgroundResource(resId);
    }

    public View getView(){
        return convertView;
    }
    public <T extends  View> T getViewByID(int id){
        View subView=subViews.get(id);
       if(subView==null){
           subView=convertView.findViewById(id);
           subViews.append(id,subView);
       }
        return (T) subView;
    }
}
