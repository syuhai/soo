package com.soo.learn.java;

import java.lang.reflect.Field;

/**
 * Created by SongYuHai on 2016/12/15.
 */
public class TestMain extends ParentC {
    @UserAnnotation("188")
    private Object obj;
    private int i=1;
    public static void main(String [] args) throws Exception{
        Field[] fields=TestMain.class.getDeclaredFields();
        Field[] fieldss=TestMain.class.getFields();
        //UserAnnotation ua=field.getAnnotation(UserAnnotation.class);
       // System.out.println(ua.age()+ua.name()+ua.id());
        TestMain tm = new TestMain();
        mProtected="c";
       // boolean b=false;
      /*  System.out.println(field.getInt(tm));
        testB(field.getType());*/
       // field.set(tm,new User(ua.age(),ua.gender(),ua.id(),ua.name())); /
    }

    private static void testB(Object obj) {
            System.out.println(obj.getClass().getSimpleName());
    }
}
