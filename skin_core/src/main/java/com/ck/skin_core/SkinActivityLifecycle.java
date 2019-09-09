package com.ck.skin_core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.ArrayMap;
import android.view.LayoutInflater;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    ArrayMap<Activity, SkinLayoutFactory> mLayoutFactory = new ArrayMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        //获得Activity的布局加载器
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        try {
            /**
             * 为什么要先将mFactorySet变量设置为false呢？
             * 源码中在执行setFactory之前会先判断mFactorySet这个变量，如果为true会跑异常
             */
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SkinLayoutFactory skinLayoutFactory = new SkinLayoutFactory();
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutFactory);
        mLayoutFactory.put(activity, skinLayoutFactory);
        SkinManager.getInstance().addObserver(skinLayoutFactory);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinLayoutFactory skinLayoutFactory = mLayoutFactory.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);
    }
}
