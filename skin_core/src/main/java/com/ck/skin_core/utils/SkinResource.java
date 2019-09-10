package com.ck.skin_core.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class SkinResource {

    private static SkinResource instance;

    private Resources mSkinResource;
    private String mSkinPkgName;
    private boolean isDefaultSkin = true;

    private Resources mAppResource;

    private SkinResource(Context context) {
        mAppResource= context.getResources();
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinResource.class) {
                if (instance == null) {
                    instance = new SkinResource(context);
                }
            }
        }
    }

    public static SkinResource getInstance() {
        return instance;
    }

    public void reset() {
        mSkinResource = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    public void applySkin(Resources resources, String pkgName) {
        mSkinResource = resources;
        mSkinPkgName = pkgName;
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    public int getIdentifier(int resId) {
        if (isDefaultSkin) return resId;

        //R.drawable.ic_launcher
        String resName = mAppResource.getResourceEntryName(resId);//ic_launcher
        String resType = mAppResource.getResourceTypeName(resId);//drawable
        int skinId = mSkinResource.getIdentifier(resName, resType, mSkinPkgName);
        return skinId;
    }

    public int getColor(int resId) {
        if (isDefaultSkin) return mAppResource.getColor(resId);

        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResource.getColor(resId);
        }
        return mSkinResource.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResource.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResource.getColorStateList(resId);
        }
        return mSkinResource.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResource.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResource.getDrawable(resId);
        }
        return mSkinResource.getDrawable(skinId);
    }


    /**
     * 可能是Color 也可能是drawable
     * background 可能是个图片也可能是个color
     *
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResource.getResourceTypeName(resId);

        if (resourceTypeName.equals("color")) {
            return getColor(resId);
        } else {
            // drawable
            return getDrawable(resId);
        }
    }

    public String getString(int resId) {
        try {
            if (isDefaultSkin) {
                return mAppResource.getString(resId);
            }
            int skinId = getIdentifier(resId);
            if (skinId == 0) {
                return mAppResource.getString(skinId);
            }
            return mSkinResource.getString(skinId);
        } catch (Resources.NotFoundException e) {

        }
        return null;
    }

    /**
     * 根据ID获得字体
     * @param resId
     * @return
     */
    public Typeface getTypeface(int resId) {
        String skinTypefacePath = getString(resId);
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }
        try {
            Typeface typeface;
            if (isDefaultSkin) {
                typeface = Typeface.createFromAsset(mAppResource.getAssets(), skinTypefacePath);
                return typeface;

            }
            typeface = Typeface.createFromAsset(mSkinResource.getAssets(), skinTypefacePath);
            return typeface;
        } catch (RuntimeException e) {
        }
        return Typeface.DEFAULT;
    }
}


















