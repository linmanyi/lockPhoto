package com.nmk.myapplication.work.ui.view.shape.drawable;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;

import com.nmk.myapplication.R;


/**
 * @author H-ray
 * @desc：
 * @create_time： 2020/1/2
 * @update_time： 2020/1/2
 */
public class SelectorTextColorDrawable implements IColorDrawable {

    private TypedArray mTypedArray;

    private int currPosition = 0;
    private int[][] states = new int[][]{};
    private int[] colors = new int[]{};

    public SelectorTextColorDrawable(TypedArray typedArray) {
        mTypedArray = typedArray;
    }

    @Override
    public ColorStateList createColorDrawable() {

        final int selectorCount = getSelectorCount();
        if (selectorCount <= 0) {
            return null;
        }
        states = new int[selectorCount][];
        colors = new int[selectorCount];

        if (mTypedArray.hasValue(R.styleable.HTextView_h_selector_pressed_text_color)) {
            //按压(Pressed)颜色
            states[currPosition] = new int[]{android.R.attr.state_pressed};
            colors[currPosition] = mTypedArray.getColor(
                    R.styleable.Background_h_selector_pressed_text_color, 0);
            currPosition++;

            setDefaultColor(-android.R.attr.state_pressed);
        }

        if (mTypedArray.hasValue(R.styleable.Background_h_selector_selected_text_color)) {
            //选择(Selected)颜色
            states[currPosition] = new int[]{android.R.attr.state_selected};
            colors[currPosition] = mTypedArray.getColor(
                    R.styleable.Background_h_selector_selected_text_color, 0);
            currPosition++;

            setDefaultColor(-android.R.attr.state_selected);
        }

        if (mTypedArray.hasValue(R.styleable.Background_h_selector_enable_text_color)) {
            //选择(Enable)颜色
            states[currPosition] = new int[]{android.R.attr.state_enabled};
            colors[currPosition] = mTypedArray.getColor(
                    R.styleable.Background_h_selector_enable_text_color, 0);
            currPosition++;

            setDefaultColor(-android.R.attr.state_enabled);
        }

        return new ColorStateList(states, colors);
    }

    /**
     * 获取文字selector条数
     */
    private int getSelectorCount() {
        int count = 0;

        if (mTypedArray == null || mTypedArray.getIndexCount() <= 0) {
            return count;
        }

        if (mTypedArray.hasValue(R.styleable.HTextView_h_selector_pressed_text_color)) {
            count++;
        }

        if (mTypedArray.hasValue(R.styleable.Background_h_selector_selected_text_color)) {
            count++;
        }

        if (mTypedArray.hasValue(R.styleable.Background_h_selector_enable_text_color)) {
            count++;
        }

        return count * 2;
    }

    /**
     * 设置默认颜色selector
     */
    private void setDefaultColor(int stateId) {
        if (currPosition > 0) {
            //设置默认颜色
            colors[currPosition] = mTypedArray.getColor(
                    R.styleable.Background_h_selector_default_text_color, 0);
            states[currPosition] = new int[]{stateId};
            currPosition++;
        }
    }


}
