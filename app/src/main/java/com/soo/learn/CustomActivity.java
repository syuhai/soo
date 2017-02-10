package com.soo.learn;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.soo.learn.customview.CustomTextView;
import com.soo.learn.util.StringUtil;

/**
 * Created by SongYuHai on 2017/1/17.
 */

public class CustomActivity extends Activity {
    Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        initView();
    }

    private void initView() {
        TextView stv= (TextView) findViewById(R.id.drawable);
        CustomTextView tv= (CustomTextView) findViewById(R.id.custom);
        drawable=getResources().getDrawable(R.mipmap.phone_micro);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tv.setCompoundDrawables(drawable,null,null,null);
        stv.setText(Html.fromHtml("<img  src=\""+R.mipmap.phone_micro+"\"/>&nbsp;"+ StringUtil.f("￥48.88"), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                return drawable;
            }
        }, null));
    }
}
