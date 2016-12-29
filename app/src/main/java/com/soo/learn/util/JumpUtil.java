package com.soo.learn.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by SongYuHai on 2016/12/7.
 */

public class JumpUtil {
    public static void JumpToActivity(Context context, Class cla){
        Intent intent =new Intent(context,cla);
        context.startActivity(intent);
    }
}
