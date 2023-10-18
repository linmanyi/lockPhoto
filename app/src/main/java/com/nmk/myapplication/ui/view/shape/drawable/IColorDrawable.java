package com.nmk.myapplication.ui.view.shape.drawable;

import android.content.res.ColorStateList;

/**
 * @author H-ray
 * @desc：selector类型颜色选择器
 * @create_time： 2020/1/2
 * @update_time： 2020/1/2
 */
public interface IColorDrawable {

    /**
     * 生产selector类型的文字颜色选择器
     */
    ColorStateList createColorDrawable();

}
