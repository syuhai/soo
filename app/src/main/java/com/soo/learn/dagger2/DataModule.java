package com.soo.learn.dagger2;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SongYuHai on 2017/2/10.
 */

@Module
public class DataModule {
    @Provides
   User user(){
       return new User("通过module");
   }
}
