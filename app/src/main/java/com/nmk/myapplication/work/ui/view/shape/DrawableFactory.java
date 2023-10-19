package com.nmk.myapplication.work.ui.view.shape;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.nmk.myapplication.work.ui.view.shape.drawable.SelectorDrawable;
import com.nmk.myapplication.work.ui.view.shape.drawable.SelectorTextColorDrawable;
import com.nmk.myapplication.work.ui.view.shape.drawable.ShapeDrawable;


/**
 * @author H-ray
 * @desc：Drawable类型生产工厂
 * @create_time： 2019/12/31
 * @update_time： 2019/12/31
 */
public class DrawableFactory {

    /**
     * 生产Shape Drawable类型的Drawable
     */
    public static Drawable getShapeDrawable(TypedArray typedArray) {
        return new ShapeDrawable(typedArray).createDrawable();
    }

    /**
     * 生产selector Drawable类型的Drawable
     */
    public static Drawable getSelectorDrawable(TypedArray typedArray) {
        return new SelectorDrawable(typedArray).createDrawable();
    }

    /**
     * 生产selector类型的颜色选择器
     */
    public static ColorStateList getSelectorColor(TypedArray typedArray) {
        return new SelectorTextColorDrawable(typedArray).createColorDrawable();
    }

}
