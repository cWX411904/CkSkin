package com.ck.skin_core;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ck.skin_core.utils.SkinResource;
import com.ck.skin_core.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {

    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    private Typeface typeface;

    List<SkinView> mSkinViews = new ArrayList<>();

    public SkinAttribute(Typeface skinTypeface) {
        this.typeface = skinTypeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    /**
     *
     * @param view
     * @param attrs 就是布局中的background / textColor 等等
     */
    public void load(View view, AttributeSet attrs) {

        List<SkinPair> skinPairs = new ArrayList<>();

        int attributeCount = attrs.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            //获得属性名
            String attributeName = attrs.getAttributeName(i);
            //是否符合需要栓选的属性
            if (mAttributes.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    //表示写死了属性，如#FFFFFF
                    continue;
                }
                //资源ID
                int resId;
                if (attributeValue.startsWith("?")) {
                    //
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    //获得主题 style 中的对应 attr的 资源id的值
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    //是以"@"开头
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                if (resId != 0) {
                    //可以被替换的属性
                    //attributeName 就是宽、高、bg等等
                    SkinPair skinPair = new SkinPair(attributeName, resId);
                    skinPairs.add(skinPair);
                }
            }
        }

        if (!skinPairs.isEmpty() || view instanceof TextView || view instanceof SkinViewSupport) {
            //将view与之对应的可以动态替换的属性集合 放入集合中
            SkinView skinView = new SkinView(view, skinPairs);
            skinView.applySkin(typeface);
            mSkinViews.add(skinView);
        }
    }

    /**
     * 换皮肤
     */
    public void applySkin() {
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin(typeface);
        }
    }

    public static class SkinView {
        View view;
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin(Typeface typeface) {
            applySkinTypeface(typeface);
            applySkinViewSupport();
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background":
                        Object background = SkinResource.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            //color
                            view.setBackgroundColor((Integer) background);
                        } else {
//                            ViewCompat.setBackground(view, (Drawable) background);

                        }
                        break;
                    case "src":
                        background = SkinResource.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            ((ImageView)view).setImageDrawable(new ColorDrawable((Integer) background));
                        } else {
                            ((ImageView)view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "testColor":
                        ((TextView)view).setTextColor(SkinResource.getInstance().getColorStateList(skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResource.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResource.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResource.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResource.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "skinTypeface":
                        Typeface typeface1 = SkinResource.getInstance().getTypeface(skinPair.resId);
                        applySkinTypeface(typeface1);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView)view).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
                }
            }
        }

        private void applySkinViewSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }

        private void applySkinTypeface(Typeface typeface) {
            if (view instanceof TextView) {
                ((TextView)view).setTypeface(typeface);
            }
        }
    }

    public static class SkinPair {
        String attributeName;
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
