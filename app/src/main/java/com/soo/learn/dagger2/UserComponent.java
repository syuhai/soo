package com.soo.learn.dagger2;

import dagger.Component;

/**
 * Created by SongYuHai on 2017/2/10.
 */
@Component(modules = DataModule.class)
public interface UserComponent {
    void inject(DaggerActivity daggerActivity);
}
