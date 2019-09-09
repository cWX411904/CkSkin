package com.ck.skin_core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 观察者
 */
public class SkinLayoutFactory implements LayoutInflater.Factory2, Observer {

    private static final Map<String, Constructor<? extends View>> mConstructorMap =
            new HashMap<>();

    /**
     * Android的view 的包的路径前缀
     */
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    private static final Class<?>[] mConstructorSignature = new Class[] {
            Context.class, AttributeSet.class
    };

    //属性处理类
    SkinAttribute skinAttribute = null;

    public SkinLayoutFactory() {
        skinAttribute = new SkinAttribute();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //跟系统一样，通过classLoader反射
        View view =  createViewFromTag(name, context, attrs);
        if (null == view) {
            //自定义view
            view = createView(name, context, attrs);
        }
        //栓选符合属性的view
        skinAttribute.load(view, attrs);
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {

        if (-1 != name.indexOf(".")) {
            //自定义控件
            return null;
        }
        View view = null;
        for (int i = 0; i < mClassPrefixList.length; i++) {
            view =  createView(mClassPrefixList[i] + name, context, attrs);
        }

        return view;

    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {}

        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if (null == constructor) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                mConstructorMap.put(name, constructor);
            } catch (Exception e) {

            }
        }
        return constructor;
    }


    /**
     * 当被观察者发生改变，并
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        //开始更换皮肤
        skinAttribute.applySkin();
    }
}
