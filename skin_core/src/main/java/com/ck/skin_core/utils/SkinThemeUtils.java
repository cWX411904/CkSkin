package com.ck.skin_core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;

import com.ck.skin_core.R;

public class SkinThemeUtils {

    private static int[] TYPEFACE_ATTR = {
            R.attr.skinTypeface
    };

    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimaryDark
    };
    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr
            .navigationBarColor};

    public static int[] getResId(Context context, int[] attrs) {
        int[] resIds = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIds;
    }

    public static void updateStatusBar(Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //只有5.0以上的才有这个属性
            return;
        }

        //读取Activity在style.xml中定义的theme
        int[] resIds = getResId(activity, STATUSBAR_COLOR_ATTRS);

        /**
         * 修改顶部导航栏的颜色
         */
        if (resIds[0] == 0) {
            //如果再xml中没有配置该属性，就是为0
            //接下来再读取colorPrimaryDark这个属性值
            int[] colorPrimaryDarkResIds = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS);
            if (colorPrimaryDarkResIds[0] != 0) {
                //style.xml中定义了colorPrimaryDark的颜色
                activity.getWindow().setStatusBarColor(SkinResource.getInstance().getColor(colorPrimaryDarkResIds[0]));
            }
        } else {
            activity.getWindow().setStatusBarColor(SkinResource.getInstance().getColor(resIds[0]));
        }

        /**
         * 修改底部虚拟按键的颜色
         */
        if (resIds[1] != 0) {
            activity.getWindow().setNavigationBarColor(SkinResource.getInstance().getColor(resIds[1]));
        }
    }

    /**
     * 获得字体
     * @param activity
     */
    public static Typeface getSkinTypeface(Activity activity) {
        int[] resId = getResId(activity, TYPEFACE_ATTR);
        int skinTypefaceId = resId[0];
        return SkinResource.getInstance().getTypeface(skinTypefaceId);
    }
}
