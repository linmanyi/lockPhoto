package com.nmk.myapplication.ui.view.shape.drawable;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import com.nmk.myapplication.R;

/**
 * @author H-ray
 * @desc：
 * @create_time： 2020/1/2
 * @update_time： 2020/1/2
 */
public class SelectorDrawable implements IDrawable {

    private TypedArray mTypedArray;

    public SelectorDrawable(TypedArray mTypedArray) {
        this.mTypedArray = mTypedArray;
    }

    @Override
    public Drawable createDrawable() {
        StateListDrawable stateListDrawable = null;
        ShapeDrawable shapeDrawable = new ShapeDrawable(mTypedArray);
        if (mTypedArray.hasValue(R.styleable.Background_h_selector_pressed_solid)
                || mTypedArray.hasValue(R.styleable.Background_h_selector_pressed_stroke_width)) {

            if (stateListDrawable == null) {
                stateListDrawable = new StateListDrawable();
            }

            //默认效果
            GradientDrawable defaultDrawable = (GradientDrawable) shapeDrawable.createDrawable();
            stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, defaultDrawable);

            //（Pressed）按压效果
            GradientDrawable pressedTrueDrawable =
                    (GradientDrawable) shapeDrawable.createSelectorPressedDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                    pressedTrueDrawable);

        }

        if (mTypedArray.hasValue(R.styleable.Background_h_selector_selected_solid)
                || mTypedArray.hasValue(R.styleable.Background_h_selector_selected_stroke_width)) {

            if (stateListDrawable == null) {
                stateListDrawable = new StateListDrawable();
            }

            //默认效果
            GradientDrawable defaultDrawable = (GradientDrawable) shapeDrawable.createDrawable();
            stateListDrawable.addState(new int[]{-android.R.attr.state_selected}, defaultDrawable);

            //（Selected）选择效果
            GradientDrawable pressedTrueDrawable =
                    (GradientDrawable) shapeDrawable.createSelectorSelectedDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_selected},
                    pressedTrueDrawable);
        }

        if (mTypedArray.hasValue(R.styleable.Background_h_selector_enable_solid)
                || mTypedArray.hasValue(R.styleable.Background_h_selector_enable_stroke_width)) {

            if (stateListDrawable == null) {
                stateListDrawable = new StateListDrawable();
            }

            //默认效果
            GradientDrawable defaultDrawable = (GradientDrawable) shapeDrawable.createDrawable();
            stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, defaultDrawable);

            //（Enable）效果
            GradientDrawable enableTrueDrawable =
                    (GradientDrawable) shapeDrawable.createSelectorEnableDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_enabled},
                    enableTrueDrawable);
        }

        if (mTypedArray.hasValue(R.styleable.Background_h_selector_focused_solid)
                || mTypedArray.hasValue(R.styleable.Background_h_selector_focused_stroke_width)) {

            if (stateListDrawable == null) {
                stateListDrawable = new StateListDrawable();
            }

            //默认效果
            GradientDrawable defaultDrawable = (GradientDrawable) shapeDrawable.createDrawable();
            stateListDrawable.addState(new int[]{-android.R.attr.state_focused}, defaultDrawable);

            //（Focused）效果
            GradientDrawable focusedTrueDrawable =
                    (GradientDrawable) shapeDrawable.createSelectorFocusedDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_focused},
                    focusedTrueDrawable);
        }

        return stateListDrawable;
    }

}
