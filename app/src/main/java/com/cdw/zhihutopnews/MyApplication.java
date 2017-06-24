package com.cdw.zhihutopnews;

import android.app.Application;


/**
 * 自定义Application 系统组件
 * 作用：当android程序启动时系统会创建一个 application对象，用来存储系统的一些信息
 */
public class MyApplication extends Application {
    public static MyApplication myApplication;

    public static Application getContext() {

        return myApplication;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

    }
}
