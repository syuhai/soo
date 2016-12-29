package com.soo.learn.floatview;

/**
 * Created by SongYuHai on 2016/12/20.
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.soo.learn.R;
import com.soo.learn.util.DensityUtils;
import com.soo.learn.util.L;
import com.soo.learn.util.ScreenUtils;

public class FloatView implements View.OnTouchListener,View.OnClickListener {
    private  Context mContext;
    private  WindowManager.LayoutParams params;
    private  WindowManager mWM;
    private  View mView;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private int mMiddle;
    private int mTotalWidth;
    private final int WIDTH_PX=120;
    private final int HEIGHT_PX=200;
    private ImageView mDelIv;
    private int tempParamX,tempParamY;
    public FloatView(Context context){
        this.mContext = context;
        mTotalWidth= ScreenUtils.getScreenWidth(context);
        mMiddle=mTotalWidth/2;
        initView();
    }

    private void initView() {
        params = new WindowManager.LayoutParams();
        params.height = DensityUtils.dp2px(mContext,HEIGHT_PX);
        params.width = DensityUtils.dp2px(mContext,WIDTH_PX);
        params.format = PixelFormat.TRANSLUCENT;
        // params.windowAnimations =  R.style.anim_view;
        // 悬浮窗类型
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.gravity = Gravity.RIGHT | Gravity.CLIP_VERTICAL;// Gravity.LEFT | Gravity.TOP;
        tempParamY=ScreenUtils.getScreenHeight(mContext)/2-params.height/2;
        tempParamX=mTotalWidth-params.width;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflate.inflate(R.layout.floatview, null);
        mDelIv= (ImageView) mView.findViewById(R.id.del);
        mDelIv.setOnClickListener(this);
        mView.setOnTouchListener(this);
        //mView.setOnClickListener(this);
        L.e("tempParamY="+tempParamY+"---tempParamX"+tempParamX);
    }

    public void show(){
        if(mView.getParent() != null) {
          mWM.removeView(mView);
         }
        mWM.addView(mView, params);
    }
    public void hide(){
        if(mView!=null){
            mWM.removeView(mView);
            mView=null;
        }
    }
    private void updateViewPosition(){
        measureNowXy();
        updateView();
    }
    private void updateView(){
        mWM.updateViewLayout(mView, params); //刷新显示
    }
    private void measureNowXy(){
        //更新浮动窗口位置参数
        params.x=(int) (x-mTouchStartX);
        params.y=(int) (y-mTouchStartY);
        params.x=tempParamX-params.x;
        params.y=params.y-tempParamY;
    }
    private void measureToSideX(){
        if(params.x+WIDTH_PX/2>mMiddle){
            params.x=mTotalWidth-WIDTH_PX;
        }else{
            params.x=0;
        }
    }
    private void updateViewPositionToSide(){
        measureNowXy();
        measureToSideX();
        updateView();
    }
         @Override
     public boolean onTouch(View v, MotionEvent event) {
             //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY();
             Log.i("currP", "currX"+x+"====currY"+y);
             switch (event.getAction()) {
                 case MotionEvent.ACTION_DOWN: //捕获手指触摸按下动作
                     // 获取相对View的坐标，即以此View左上角为原点
                     mTouchStartX = event.getX();
                     mTouchStartY = event.getY();
                     Log.i("startP","startX"+mTouchStartX+"====startY"+mTouchStartY);
                     break;
                 case MotionEvent.ACTION_MOVE: //捕获手指触摸移动动作
                     updateViewPosition();
                     break;
                 case MotionEvent.ACTION_UP: //捕获手指触摸离开动作
                     updateViewPositionToSide();
                     break;
             }
             return true;
         }

    @Override
    public void onClick(View v) {
        if(v==mDelIv){
            hide();
        }else if(v==mView){
            tempParamX+=100;
            tempParamY+=100;
            params.x=tempParamX;
            params.y=tempParamY;
            mWM.updateViewLayout(mView, params); //刷新显示
        }
    }
}

