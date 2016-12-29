package com.soo.learn.favoranimation;

import android.animation.TypeEvaluator;

import com.soo.learn.entity.Point;

/**
 * Created by SongYuHai on 2016/12/27.
 */

public class MyPointEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float v, Object o, Object t1) {
        Point pointStart= (Point) o;
        Point pointEnd= (Point) t1;
        float x=pointStart.getX()+v*(pointEnd.getX()-pointStart.getX());
        float y=pointStart.getY()+v*(pointEnd.getY()-pointStart.getY());
       /* if(y==1000){
            x=x-;
        }else if(y>1000){
            x=y-1000;
        }*/
        return new Point(x,y);
    }
}
