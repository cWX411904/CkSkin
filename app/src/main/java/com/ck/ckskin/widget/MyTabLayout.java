package com.ck.ckskin.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.ck.ckskin.R;
import com.ck.skin_core.SkinViewSupport;
import com.ck.skin_core.utils.SkinResource;


/**
 * @author Lance
 * @date 2018/3/12
 */

public class MyTabLayout extends TabLayout implements SkinViewSupport {
    int tabIndicatorColorResId;
    int tabTextColorResId;

    public MyTabLayout(Context context) {
        this(context, null, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                defStyleAttr, 0);
        tabIndicatorColorResId = a.getResourceId(R.styleable.TabLayout_tabIndicatorColor, 0);
        tabTextColorResId = a.getResourceId(R.styleable.TabLayout_tabTextColor, 0);
        a.recycle();
    }

    @Override
    public void applySkin() {

        int tabIndicatorColor = SkinResource.getInstance().getColor(tabIndicatorColorResId);
        setSelectedTabIndicatorColor(tabIndicatorColor);

        ColorStateList colorStateList = SkinResource.getInstance().getColorStateList(tabTextColorResId);
        setTabTextColors(colorStateList);
    }

//    @Override
//    public void applySkin() {
//        if (tabIndicatorColorResId != 0) {
//            int tabIndicatorColor = SkinResources.getInstance().getColor(tabIndicatorColorResId);
//            setSelectedTabIndicatorColor(tabIndicatorColor);
//        }
//
//        if (tabTextColorResId != 0) {
//            ColorStateList tabTextColor = SkinResources.getInstance().getColorStateList
//                    (tabTextColorResId);
//            setTabTextColors(tabTextColor);
//        }
//    }
}
