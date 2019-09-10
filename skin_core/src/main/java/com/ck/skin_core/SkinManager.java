package com.ck.skin_core;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.ck.skin_core.utils.SkinPreference;
import com.ck.skin_core.utils.SkinResource;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * 被观察者
 */
public class SkinManager extends Observable {

    private static volatile SkinManager instance;
    private Application application;

    private SkinActivityLifecycle skinActivityLifecycle;

    private SkinManager(Application application) {
        this.application = application;
        SkinPreference.init(application);
        SkinResource.init(application);
        skinActivityLifecycle = new SkinActivityLifecycle();
        application.registerActivityLifecycleCallbacks(skinActivityLifecycle);

        //加载皮肤
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public void loadSkin(String path) {
        if (TextUtils.isEmpty(path)) {
            //记录使用默认皮肤
            SkinPreference.getInstance().setSkin("");
            //清空资源管理器，皮肤资源属性
            SkinResource.getInstance().reset();
        } else {
            try {
                //反射创建AssetManager 与Resource
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, path);

                Resources appResource = application.getResources();
                //后面两个参数是当前app运行的显示与配置（横竖屏、语言等）
                Resources skinResource = new Resources(assetManager,
                        appResource.getDisplayMetrics(), appResource.getConfiguration());
                //记录
                SkinPreference.getInstance().setSkin(path);
                //获取外部Apk（皮肤包）包名
                PackageManager packageManager = application.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                String packageName = packageInfo.packageName;
                SkinResource.getInstance().applySkin(skinResource, packageName);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //通知到SkinAttribute属性发生改变
        setChanged();
        //通知观察者
        notifyObservers(null);
    }

    public static void init(Application application) {
        synchronized (SkinManager.class) {
            if (null == instance) {
                instance = new SkinManager(application);
            }
        }
    }

    public static SkinManager getInstance() {
        return instance;
    }

    public void updateSkin(Activity activity) {
        skinActivityLifecycle.updateSkin(activity);
    }
}
