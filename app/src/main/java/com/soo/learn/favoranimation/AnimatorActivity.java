package com.soo.learn.favoranimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.soo.learn.R;
import com.soo.learn.util.JumpUtil;

/**
 * Created by SongYuHai on 2016/12/26.
 * 要设置动画的目标对象；
 *  动画的属性类型；
 *  一个或多个属性值；
 *  当只指定一个属性值，系统默认此值为结束值；
 *  当指定两个属性值，系统默认分别为起始值和结束值；
 *  当指定三个或三个以上时，系统默认线性插值；
 *  属性名称：   translation rotation  scale  alpha  translationX
 */

public class AnimatorActivity extends Activity implements View.OnClickListener{
    //属性动画
    private ImageView animIv;
    private int mCurrentRed = -1;
    private int mCurrentGreen = -1;
    private int mCurrentBlue = -1;
    private View targetView;
    private ImageView pointIv;
    private LinearLayout pointLl1,pointLl2,pointLl3,pointLl4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        initView();
    }
    private void initView() {
        animIv= (ImageView) findViewById(R.id.animImg);
        pointIv= (ImageView) findViewById(R.id.point1);
        pointLl1= (LinearLayout) findViewById(R.id.pointView1);
        pointLl2= (LinearLayout) findViewById(R.id.pointView2);
        pointLl3= (LinearLayout) findViewById(R.id.pointView3);
        pointLl4= (LinearLayout) findViewById(R.id.pointView4);
        targetView=findViewById(R.id.parent);
        targetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayResult(targetView,"#0000ff", "#ff0000");
            }
        });
        findViewById(R.id.alpha).setOnClickListener(this);
        findViewById(R.id.translate).setOnClickListener(this);
        findViewById(R.id.rotate).setOnClickListener(this);
        findViewById(R.id.scale).setOnClickListener(this);
        findViewById(R.id.tremble).setOnClickListener(this);
        findViewById(R.id.pointRotate).setOnClickListener(this);
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
            case R.id.pointRotate:
                beginPointRotateAnimation();
                break;
        }
    }

    private void beginPointRotateAnimation() {
        ObjectAnimator animOne=ObjectAnimator.ofFloat(pointLl1,"rotation",0,360).setDuration(2000);
        animOne.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator animTwo=ObjectAnimator.ofFloat(pointLl2,"rotation",0,360);
        animTwo.setStartDelay(150);
        animTwo.setDuration(2000+150);
        animTwo.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator animThree=ObjectAnimator.ofFloat(pointLl3,"rotation",0,360);
        animThree.setStartDelay(2*150);
        animThree.setDuration(2000+2*150);
        animThree.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator animFour=ObjectAnimator.ofFloat(pointLl4,"rotation",0,360);
        animFour.setStartDelay(3*150);
        animFour.setDuration(2000+3*150);
        animFour.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet set=new AnimatorSet();
        //set.play(animOne).with(animTwo).with(animThree).with(animFour);
        set.playTogether(animOne,animTwo,animThree,animFour);
        set.start();
    }

    private void beginTrembleAnimation() {
        TrembleAnim tremble=new TrembleAnim();
        tremble.setDuration(1000);
        tremble.setRepeatCount(2);
        tremble.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                JumpUtil.JumpToActivity(AnimatorActivity.this,AnimatorEvalutorActivity.class);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animIv.startAnimation(tremble);
    }

    private void beginRotateAnimation() {
        //animIv.animate().rotation(180).setDuration(3000).start();
        animIv.animate().rotationBy(360).setDuration(3000);
        //RotateAnimation anim=new RotateAnimation(0,360);
       /* ObjectAnimator animO=new ObjectAnimator();
        animO.ofFloat(animIv,"rotation",0,360)
        .setDuration(3000).start();*/
    /*    RotateAnimation anim = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(3000);
        animIv.startAnimation(anim);*/
    }

    private void beginScaleAnimation() {

        ObjectAnimator animO=new ObjectAnimator();
        animO.ofFloat(animIv,"scaleX",1,2).setDuration(3000).start();
        ObjectAnimator animM=new ObjectAnimator();
        animM.ofFloat(animIv,"scaleY",1,2).setDuration(3000).start();
    /*    ScaleAnimation anim=new ScaleAnimation(1,2,1,2,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(3000);
        animIv.startAnimation(anim);*/
    }

    private void beginTranslateAnimation() {
        ObjectAnimator animO=new ObjectAnimator();
        animO.ofFloat(animIv,"translationX",1,100).setDuration(3000).start();
        ObjectAnimator animM=new ObjectAnimator();
        animM.ofFloat(animIv,"translationY",1,500).setDuration(3000).start();
       /* TranslateAnimation anim=new TranslateAnimation(1,100,1,500);
        anim.setDuration(3000);
        animIv.startAnimation(anim);*/
    }

    boolean isAlpha=false;
    private void beginAlphaAnimation() {
       /* if(!isAlpha){
            isAlpha=true;*/
            ObjectAnimator animO=new ObjectAnimator();
            animO.ofFloat(animIv,"alpha",1f,0f,1f).setDuration(6000).start();
       /* }else {
            isAlpha=false;
            ObjectAnimator animF= ObjectAnimator.ofFloat(animIv,"alpha",0f,1f);
            animF.setDuration(3000).start();
        }*/

    /*    AnimatorSet set=new AnimatorSet();
        set.play(animO).after(animF);
        set.start();*/
      /*  AlphaAnimation anim=new AlphaAnimation(1,0);
        anim.setDuration(3000);
        animIv.startAnimation(anim);*/
    }
/*    private void combineAnimation(){
        ValueAnimator animator=ValueAnimator.ofFloat(1f,100f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

            }
        });
    }*/

    /**
     * displayResult()展示结果
     */
    private void displayResult(final View target, final String start, final String end) {
        // 创建ValueAnimator对象，实现颜色渐变
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 100f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取当前动画的进度值，1~100
                float currentValue = (float) animation.getAnimatedValue();
                Log.d("当前动画值", "current value : " + currentValue);

                // 获取动画当前时间流逝的百分比，范围在0~1之间
                float fraction = animation.getAnimatedFraction();
                // 直接调用evaluateForColor()方法，通过百分比计算出对应的颜色值
                String colorResult = evaluateForColor(fraction, start, end);

                /**
                 * 通过Color.parseColor(colorResult)解析字符串颜色值，传给ColorDrawable，创建ColorDrawable对象
                 */
                /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target.getLayoutParams();*/
                ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(colorResult));
                // 把ColorDrawable对象设置为target的背景
                target.setBackgroundDrawable(colorDrawable);
                //target.setBackground(colorDrawable);
                target.invalidate();
            }
        });
        valueAnimator.setDuration(6 * 1000);


        // 组装缩放动画
        ValueAnimator animator_1 = ObjectAnimator.ofFloat(target, "scaleX", 1f, 0.5f);
        ValueAnimator animator_2 = ObjectAnimator.ofFloat(target, "scaleY", 1f, 0.5f);
        ValueAnimator animator_3 = ObjectAnimator.ofFloat(target, "scaleX", 0.5f, 1f);
        ValueAnimator animator_4 = ObjectAnimator.ofFloat(target, "scaleY", 0.5f, 1f);
        AnimatorSet set_1 = new AnimatorSet();
        set_1.play(animator_1).with(animator_2);
        AnimatorSet set_2 = new AnimatorSet();
        set_2.play(animator_3).with(animator_4);
        AnimatorSet set_3 = new AnimatorSet();
        set_3.play(set_1).before(set_2);
        set_3.setDuration(3 * 1000);

        // 组装颜色动画和缩放动画，并启动动画
        AnimatorSet set_4 = new AnimatorSet();
        set_4.play(valueAnimator).with(set_3);
        set_4.start();
    }

    /**
     * evaluateForColor()计算颜色值并返回
     */
    private String evaluateForColor(float fraction, String startValue, String endValue) {

        String startColor = startValue;
        String endColor = endValue;
        int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
        int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
        int startBlue = Integer.parseInt(startColor.substring(5, 7), 16);
        int endRed = Integer.parseInt(endColor.substring(1, 3), 16);
        int endGreen = Integer.parseInt(endColor.substring(3, 5), 16);
        int endBlue = Integer.parseInt(endColor.substring(5, 7), 16);

        // 初始化颜色的值
        if (mCurrentRed == -1) {
            mCurrentRed = startRed;
        }
        if (mCurrentGreen == -1) {
            mCurrentGreen = startGreen;
        }
        if (mCurrentBlue == -1) {
            mCurrentBlue = startBlue;
        }

        // 计算初始颜色和结束颜色之间的差值
        int redDiff = Math.abs(startRed - endRed);
        int greenDiff = Math.abs(startGreen - endGreen);
        int blueDiff = Math.abs(startBlue - endBlue);
        int colorDiff = redDiff + greenDiff + blueDiff;
        if (mCurrentRed != endRed) {
            mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0, fraction);
        } else if (mCurrentGreen != endGreen) {
            mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff, redDiff, fraction);
        } else if (mCurrentBlue != endBlue) {
            mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff,
                    redDiff + greenDiff, fraction);
        }

        // 将计算出的当前颜色的值组装返回
        String currentColor = "#" + getHexString(mCurrentRed)
                + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);
        return currentColor;
    }

    /**
     * 根据fraction值来计算当前的颜色。
     */
    private int getCurrentColor(int startColor, int endColor, int colorDiff,
                                int offset, float fraction) {
        int currentColor;
        if (startColor > endColor) {
            currentColor = (int) (startColor - (fraction * colorDiff - offset));
            if (currentColor < endColor) {
                currentColor = endColor;
            }
        } else {
            currentColor = (int) (startColor + (fraction * colorDiff - offset));
            if (currentColor > endColor) {
                currentColor = endColor;
            }
        }
        return currentColor;
    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    private String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
