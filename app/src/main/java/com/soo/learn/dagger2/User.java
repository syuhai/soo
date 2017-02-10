package com.soo.learn.dagger2;

import javax.inject.Inject;

/**
 * Created by SongYuHai on 2017/2/10.
 */

public class User {
    private String name;
    @Inject
    public User(){
        name="default";
    }

    public String getName() {
        return name;
    }

    public User(String name){
        this.name=name;
    }
}
