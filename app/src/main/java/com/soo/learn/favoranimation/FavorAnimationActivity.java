package com.soo.learn.favoranimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.soo.learn.R;

import me.yifeiyuan.library.PeriscopeLayout;

/**
 * Created by SongYuHai on 2016/12/23.
 */

public class FavorAnimationActivity extends Activity {
    private ImageView heartIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor_animation);
        initView();
    }

    private void initView() {
        heartIv= (ImageView) findViewById(R.id.heart);
        final PeriscopeLayout periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
        periscopeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periscopeLayout.addHeart();
            }
        });
        heartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                periscopeLayout.addHeart();
            }
        });
    }
}
