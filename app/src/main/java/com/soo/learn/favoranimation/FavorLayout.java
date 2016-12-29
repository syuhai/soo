package com.soo.learn.favoranimation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.soo.learn.R;

/**
 * Created by SongYuHai on 2016/12/23.
 */

public class FavorLayout extends RelativeLayout {

    private int dHeight;
    private int dWidth;
    private int mHeight;
    private int mWidth;
    public FavorLayout(Context context) {
        super(context);
    }

    public FavorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(getResources().getColor(R.color.color_00c2f2));
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
    }
}
