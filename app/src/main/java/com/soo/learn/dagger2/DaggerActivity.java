package com.soo.learn.dagger2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.soo.learn.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by SongYuHai on 2017/2/10.
 */

public class DaggerActivity extends Activity {
    @Inject
    User user;
    @InjectView(R.id.tv)
     TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);
        ButterKnife.inject(this);
        //DaggerOnlyInjectComponent.builder().build().inject(this);
        DaggerUserComponent.builder().build().inject(this);
        tv.setText(user.getName());
    }
}
