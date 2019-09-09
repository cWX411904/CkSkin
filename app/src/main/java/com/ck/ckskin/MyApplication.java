package com.ck.ckskin;

import android.app.Application;

import com.ck.skin_core.SkinManager;


/**
 * @author Lance
 * @date 2018/3/8
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
