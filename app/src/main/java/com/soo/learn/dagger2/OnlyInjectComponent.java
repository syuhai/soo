package com.soo.learn.dagger2;

import dagger.Component;

/**
 * Created by SongYuHai on 2017/2/10.
 *
 * 没有modules和dependencies的情况下，纯粹用@Inject来提供依赖
 */
@Component
public interface OnlyInjectComponent {
    /**
     * 必须有个目标让Component知道需要往哪个类中注入
     * 这个方法名可以是其它的，但是推荐用inject
     * 目标类DaggerActivity必须精确，不能用它的父类
     * 这是Dagger2的机制决定的
     */
    void inject(DaggerActivity daggerActivity);
}
