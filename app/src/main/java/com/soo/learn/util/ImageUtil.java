package com.soo.learn.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by SongYuHai on 2017/1/12.
 */

public class ImageUtil {
    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 改变bitmap的背景色
     *drawBg4Bitmap(0xffffffff,bitmap)
     * @param color
     * @param orginBitmap
     * @return
     */
    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }


    public static final Bitmap createRGBImage(Bitmap bitmap,int color)
    {
        int bitmap_w=bitmap.getWidth();
        int bitmap_h=bitmap.getHeight();
        int[] arrayColor=new int[bitmap_w*bitmap_h];
        int count=0;
        for(int i=0;i<bitmap_h;i++){
            for(int j=0;j<bitmap_w;j++){
                int color1=bitmap.getPixel(j,i);
                //这里也可以取出 R G B 可以扩展一下 做更多的处理，
                //暂时我只是要处理除了透明的颜色，改变其他的颜色
                if(color1==0){
                    color1=color;
                }
                arrayColor[count]=color1;
                count++;
            }
        }
        bitmap = Bitmap.createBitmap( arrayColor, bitmap_w, bitmap_h, Bitmap.Config.ARGB_8888 );
        return bitmap;
    }
}
