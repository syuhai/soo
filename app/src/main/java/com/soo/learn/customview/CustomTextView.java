package com.soo.learn.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.soo.learn.R;

/**
 * Created by SongYuHai on 2017/1/17.
 */

public class CustomTextView extends TextView {
    private static final int LEFT=1,TOP=2,RIGHT=3,BOTTOM=4;
    private int mHeight,mWidth;
    private Drawable mDrawable;
    private int mLocation;
    public CustomTextView(Context context) {
        this(context,null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.custom);
        mHeight=ta.getDimensionPixelSize(R.styleable.custom_drawable_height,0);
        mWidth=ta.getDimensionPixelSize(R.styleable.custom_drawable_width,0);
        mDrawable=ta.getDrawable(R.styleable.custom_drawable_src);
        mLocation=ta.getInt(R.styleable.custom_drawable_location,LEFT);
        ta.recycle();
        drawDrawable();
    }

    private void drawDrawable() {
        if(mDrawable!=null){
            Bitmap mBitmap=((BitmapDrawable)mDrawable).getBitmap();
            Drawable drawable;
            if(mHeight!=0 && mWidth!=0){
                drawable=new BitmapDrawable(getResources(),getScaleBitmap(mBitmap,mWidth,mHeight));
            }else{
                drawable=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(mBitmap,mBitmap.getWidth(),mBitmap.getHeight(),true));
            }
            switch (mLocation){
                case LEFT:
                    setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                    break;
                case TOP:
                    setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
                    break;
                case RIGHT:
                    setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                    break;
                case BOTTOM:
                    setCompoundDrawablesWithIntrinsicBounds(null,null,null,drawable);
                    break;
            }
        }
    }

    private Bitmap getScaleBitmap(Bitmap bitmap, int mWidth, int mHeight) {
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        float rateWidth=(float) mWidth/width;
        float rateHeight=(float)mHeight/height;
        Matrix matrix=new Matrix();
        matrix.setScale(rateWidth,rateHeight);
        return Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
    }


}
