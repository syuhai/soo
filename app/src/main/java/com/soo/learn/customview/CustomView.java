package com.soo.learn.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.soo.learn.R;
import com.soo.learn.util.L;

/**
 * Created by SongYuHai on 2017/1/17.
 */

public class CustomView extends View {
    private String aa="";
    private int bb;
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.custom);
        aa=ta.getString(R.styleable.custom_test);
        bb=ta.getInteger(R.styleable.custom_testAttr,-1);
        L.d("CustomView: "+aa+bb);
    }
}
