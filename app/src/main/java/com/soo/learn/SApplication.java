package com.soo.learn;

import android.app.Application;
import android.content.Context;

/**
 * Created by SongYuHai on 2016/12/8.
 */

public class SApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
    }
    public static Context getInstance(){
        return mContext;
    }
}
