package com.soo.learn.floatview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.soo.learn.R;
import com.soo.learn.util.AppUtils;
import com.soo.learn.util.DensityUtils;
import com.soo.learn.util.L;

/**
 * Created by SongYuHai on 2016/12/20.
 */

public class FloatVewActivity extends Activity {
    private FloatView mFloatView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floatview);
        mFloatView=new FloatView(this);
       if( AppUtils.isMIUIRom()){
            L.d("miui------------");
        }else{
           L.d("not miui------------");
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFloatView.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFloatView.hide();
    }

    private void addFloatView() {
        View view= LayoutInflater.from(this).inflate(R.layout.floatview,null);
        int width= DensityUtils.dp2px(this,120);
        int height=DensityUtils.dp2px(this,200);
        WindowManager.LayoutParams paramsW=new WindowManager.LayoutParams();
        paramsW.type= WindowManager.LayoutParams.TYPE_TOAST;//TYPE_PHONE;
        paramsW.format=1;
        paramsW.width=width;
        paramsW.height=height;
        /**
         *这里的flags也很关键
         *代码实际是wmParams.flags |= FLAG_NOT_FOCUSABLE;
         *40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
         */
        paramsW.flags=40;
        this.getWindowManager().addView(view,paramsW);
    }
}
