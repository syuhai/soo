package com.soo.learn.BaseCommonAdapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SongYuHai on 2016/12/5.
 */

public interface IViewHolder {
    View getView(int position,View view ,ViewGroup viewGroup,Object object);
    void refreshView();
}
