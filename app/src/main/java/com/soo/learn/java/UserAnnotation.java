package com.soo.learn.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SongYuHai on 2016/12/15.
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAnnotation {
     String value() default "";
     int id() default 0;
     String name() default "";
     int age() default 18;
     String gender() default "M";

}
