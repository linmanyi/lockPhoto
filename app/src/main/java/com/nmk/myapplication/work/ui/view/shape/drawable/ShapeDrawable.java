package com.nmk.myapplication.work.ui.view.shape.drawable;

import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.nmk.myapplication.R;


/**
 * @author H-ray
 * @desc：生产Shape类型的Drawable
 * @create_time： 2019/12/31
 * @update_time： 2019/12/31
 */
public class ShapeDrawable implements IDrawable {

    private final TypedArray mTypedArray;

    public ShapeDrawable(TypedArray mTypedArray) {
        this.mTypedArray = mTypedArray;
    }

    @Override
    public Drawable createDrawable() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (mTypedArray == null) {
            return gradientDrawable;
        }

        setCommonDrawable(gradientDrawable);

        //填充色设置
        if (mTypedArray.hasValue(R.styleable.Background_h_shape_solid)) {
            gradientDrawable.setColor(
                    mTypedArray.getColor(R.styleable.Background_h_shape_solid, 0));
        }

        //边界线设置
        float strokeWidth = mTypedArray.getDimension(R.styleable.Background_h_shape_stroke_width,
                0);
        if (strokeWidth > 0) {
            gradientDrawable.setStroke((int) (strokeWidth+0.5f),
                    mTypedArray.getColor(R.styleable.Background_h_shape_stroke_color, 0));
        }

        return gradientDrawable;
    }

    /**
     * selector类型公共属性暂不支持修改
     * eg:形状、圆角...
     */
    private void setCommonDrawable(GradientDrawable gradientDrawable) {

        //形状设置
        gradientDrawable.setShape(mTypedArray.getInt(R.styleable.Background_h_shape_type, 0));
        //shape属性是否使用RTL镜像角度和颜色
        boolean isUseRtl = mTypedArray.getBoolean(R.styleable.Background_h_shape_use_rtl,true);

        //圆角设置
        float radius = mTypedArray.getDimension(R.styleable.Background_h_shape_corners_radius, 0);
        if (radius <= 0) {
            float leftTopRadius = mTypedArray.getDimension(
                    R.styleable.Background_h_shape_corners_radius_left_top, 0);
            float leftBottomRadius = mTypedArray.getDimension(
                    R.styleable.Background_h_shape_corners_radius_left_bottom, 0);
            float rightTopRadius = mTypedArray.getDimension(
                    R.styleable.Background_h_shape_corners_radius_right_top, 0);
            float rightBottomRadius = mTypedArray.getDimension(
                    R.styleable.Background_h_shape_corners_radius_right_bottom, 0);
            gradientDrawable.setCornerRadii(
                    new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius
                            , rightBottomRadius, rightBottomRadius, leftBottomRadius,
                            leftBottomRadius});
        } else {
            gradientDrawable.setCornerRadii(
                    new float[]{radius, radius, radius, radius, radius, radius, radius, radius,});
        }

        setGradientColor(gradientDrawable);
    }

    @TargetApi(16)
    private void setGradientColor(GradientDrawable gradientDrawable) {
        //渐变设置
        int startColor = mTypedArray.getColor(R.styleable.Background_h_shape_gradient_start_color,
                0);
        if (startColor != 0) {
            int centerColor = mTypedArray.getColor(R.styleable.Background_h_shape_gradient_center_color, 0);
            int[] colors;
            int endColor = mTypedArray.getColor(R.styleable.Background_h_shape_gradient_end_color, 0);

            int gradientAngle = mTypedArray.getInt(R.styleable.Background_h_shape_gradient_angle,
                    0);
            gradientAngle %= 360;

            //shape属性是否使用RTL镜像角度和颜色
            boolean isUseRtl = mTypedArray.getBoolean(R.styleable.Background_h_shape_use_rtl,true);

            //角度是否是水平方向
            boolean isHorizontal = gradientAngle == 0 || gradientAngle == 180;

            if (centerColor != 0) {
                colors = new int[3];
                colors[0] = startColor;
                colors[1] = centerColor;
                colors[2] = endColor;
            } else {
                colors = new int[2];
                colors[0] = startColor;
                colors[1] = endColor;
            }
            gradientDrawable.setColors(colors);


            GradientDrawable.Orientation mOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
            switch (gradientAngle) {
                case 0:
                    mOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
                    break;
                case 45:
                    mOrientation = GradientDrawable.Orientation.BL_TR;
                    break;
                case 90:
                    mOrientation = GradientDrawable.Orientation.BOTTOM_TOP;
                    break;
                case 135:
                    mOrientation = GradientDrawable.Orientation.BR_TL;
                    break;
                case 180:
                    mOrientation = GradientDrawable.Orientation.RIGHT_LEFT;
                    break;
                case 225:
                    mOrientation = GradientDrawable.Orientation.TR_BL;
                    break;
                case 270:
                    mOrientation = GradientDrawable.Orientation.TOP_BOTTOM;
                    break;
                case 315:
                    mOrientation = GradientDrawable.Orientation.TL_BR;
                    break;
            }
            gradientDrawable.setOrientation(mOrientation);
        }
    }

    public Drawable createSelectorPressedDrawable() {
        GradientDrawable pressedTrueDrawable = new GradientDrawable();
        if (mTypedArray == null) {
            return pressedTrueDrawable;
        }

        setCommonDrawable(pressedTrueDrawable);

        //填充色设置
        if (mTypedArray.hasValue(R.styleable.Background_h_selector_pressed_solid)) {
            pressedTrueDrawable.setColor(
                    mTypedArray.getColor(R.styleable.Background_h_selector_pressed_solid, 0));
        }

        //边界线设置
        float strokeWidth = mTypedArray.getDimension(
                R.styleable.Background_h_selector_pressed_stroke_width, 0);
        if (strokeWidth > 0) {
            pressedTrueDrawable.setStroke((int) (strokeWidth+0.5f),
                    mTypedArray.getColor(R.styleable.Background_h_selector_pressed_stroke_color,
                            0));
        }

        return pressedTrueDrawable;
    }

    public Drawable createSelectorSelectedDrawable() {
        GradientDrawable selectedTrueDrawable = new GradientDrawable();
        if (mTypedArray == null) {
            return selectedTrueDrawable;
        }

        setCommonDrawable(selectedTrueDrawable);

        //填充色设置
        if (mTypedArray.hasValue(R.styleable.Background_h_selector_selected_solid)) {
            selectedTrueDrawable.setColor(
                    mTypedArray.getColor(R.styleable.Background_h_selector_selected_solid, 0));
        }

        //边界线设置
        float strokeWidth = mTypedArray.getDimension(
                R.styleable.Background_h_selector_selected_stroke_width, 0);
        if (strokeWidth > 0) {
            selectedTrueDrawable.setStroke((int) (strokeWidth+0.5f),
                    mTypedArray.getColor(R.styleable.Background_h_selector_selected_stroke_color,
                            0));
        }

        return selectedTrueDrawable;
    }

    public Drawable createSelectorEnableDrawable() {
        GradientDrawable selectedTrueDrawable = new GradientDrawable();
        if (mTypedArray == null) {
            return selectedTrueDrawable;
        }

        setCommonDrawable(selectedTrueDrawable);

        //填充色设置
        if (mTypedArray.hasValue(R.styleable.Background_h_selector_enable_solid)) {
            selectedTrueDrawable.setColor(
                    mTypedArray.getColor(R.styleable.Background_h_selector_enable_solid, 0));
        }

        //边界线设置
        float strokeWidth = mTypedArray.getDimension(
                R.styleable.Background_h_selector_enable_stroke_width, 0);
        if (strokeWidth > 0) {
            selectedTrueDrawable.setStroke((int) (strokeWidth+0.5f),
                    mTypedArray.getColor(R.styleable.Background_h_selector_enable_stroke_color, 0));
        }

        return selectedTrueDrawable;
    }

    public Drawable createSelectorFocusedDrawable() {
        GradientDrawable focusedTrueDrawable = new GradientDrawable();
        if (mTypedArray == null) {
            return focusedTrueDrawable;
        }

        setCommonDrawable(focusedTrueDrawable);

        //填充色设置
        if (mTypedArray.hasValue(R.styleable.Background_h_selector_focused_solid)) {
            focusedTrueDrawable.setColor(
                    mTypedArray.getColor(R.styleable.Background_h_selector_focused_solid, 0));
        }

        //边界线设置
        float strokeWidth = mTypedArray.getDimension(
                R.styleable.Background_h_selector_focused_stroke_width, 0);
        if (strokeWidth > 0) {
            focusedTrueDrawable.setStroke((int) (strokeWidth+0.5f),
                    mTypedArray.getColor(R.styleable.Background_h_selector_focused_stroke_color,
                            0));
        }

        return focusedTrueDrawable;
    }


}
