package com.soo.learn.favoranimation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.soo.learn.entity.Point;
import com.soo.learn.util.L;

/**
 * Created by SongYuHai on 2016/12/27.
 */

public class AnitView extends View {
    private final static float  RADIUS=50f;
    private Point currentPoint;
    private Paint mPaint;
    public AnitView(Context context) {
        super(context);
    }

    public AnitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(currentPoint==null){
            currentPoint=new Point(RADIUS,RADIUS);
            drawCircle(canvas);
            startAnimation();
        }else{
            drawCircle(canvas);
        }
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(currentPoint.getX(),currentPoint.getY(),RADIUS,mPaint);
    }

    private void startAnimation() {
        Point pointStart=new Point(RADIUS,RADIUS);
        Point pointEnd=new Point(getWidth()-RADIUS,getHeight()-RADIUS);
        MyPointEvaluator evaluator=new MyPointEvaluator();
        ValueAnimator anim= ValueAnimator.ofObject(evaluator,pointStart,pointEnd);
        anim.setDuration(5000);
        anim.setInterpolator(new BounceInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentPoint= (Point) valueAnimator.getAnimatedValue();
                L.d("currentPoint.X=="+currentPoint.getX()+"--y="+currentPoint.getY());
                invalidate();
            }
        });
        anim.start();
    }
}
