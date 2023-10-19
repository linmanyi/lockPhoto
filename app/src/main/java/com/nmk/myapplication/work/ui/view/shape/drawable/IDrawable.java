package com.nmk.myapplication.work.ui.view.shape.drawable;

import android.graphics.drawable.Drawable;

/**
 * @author H-ray
 * @desc：创建Drawable
 * @create_time： 2019/12/31
 * @update_time： 2019/12/31
 */
public interface IDrawable {

    /**
     * 创建Drawable，填充background
     * 根据具体需求产生对应Drawable
     */
    Drawable createDrawable();

}
