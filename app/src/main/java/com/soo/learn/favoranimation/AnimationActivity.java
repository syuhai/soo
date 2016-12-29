package com.soo.learn.favoranimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.soo.learn.R;

/**
 * Created by SongYuHai on 2016/12/26.
 */

public class AnimationActivity extends Activity implements View.OnClickListener {
    //试图动画  anim        属性动画  animtor
    private ImageView animIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        initView();
    }

    private void initView() {
        animIv= (ImageView) findViewById(R.id.animImg);
        findViewById(R.id.alpha).setOnClickListener(this);
        findViewById(R.id.translate).setOnClickListener(this);
        findViewById(R.id.rotate).setOnClickListener(this);
        findViewById(R.id.scale).setOnClickListener(this);
        findViewById(R.id.tremble).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.alpha:
                beginAlphaAnimation();
                break;
            case R.id.translate:
                beginTranslateAnimation();
                break;
            case R.id.scale:
                beginScaleAnimation();
                break;
            case R.id.rotate:
                beginRotateAnimation();
                break;
            case R.id.tremble:
                beginTrembleAnimation();
                break;
        }
    }

    private void beginTrembleAnimation() {
        TrembleAnim tremble=new TrembleAnim();
        tremble.setDuration(1000);
        tremble.setRepeatCount(2);
        animIv.startAnimation(tremble);
    }

    private void beginRotateAnimation() {
        //RotateAnimation anim=new RotateAnimation(0,360);
        RotateAnimation anim = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(3000);
        animIv.startAnimation(anim);
    }

    private void beginScaleAnimation() {
        ScaleAnimation anim=new ScaleAnimation(1,2,1,2,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(3000);
        animIv.startAnimation(anim);
    }

    private void beginTranslateAnimation() {
        TranslateAnimation anim=new TranslateAnimation(1,100,1,500);
        anim.setDuration(3000);
        animIv.startAnimation(anim);
    }

    private void beginAlphaAnimation() {
        AlphaAnimation anim=new AlphaAnimation(1,0);
        anim.setDuration(3000);
        animIv.startAnimation(anim);
    }
}
