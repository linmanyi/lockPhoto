package com.nmk.myapplication.work.ui.view.shape;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.nmk.myapplication.R;
import com.nmk.myapplication.work.ui.view.shape.util.AspectRatioMeasure;


/**
 * @author H-ray
 * @desc：View自定义属性初始化帮助类
 * @create_time： 2019/12/31
 * @update_time： 2019/12/31
 */
public class ViewAttrsHelper {

    private float mAspectRatio;
    public AspectRatioMeasure.Spec spec;  //View宽高比计算辅助

    public ViewAttrsHelper() {
        spec = new AspectRatioMeasure.Spec(0, 0);
    }

    public void initAttrsDrawable(View view, Context context,
            @Nullable AttributeSet attrs) {
        TypedArray typeArray = null;
        try {
            if (view instanceof FrameLayout) {
                typeArray = context.obtainStyledAttributes(attrs, R.styleable.HFrameLayout);
            } else if (view instanceof LinearLayout) {
                typeArray = context.obtainStyledAttributes(attrs, R.styleable.HLinearLayout);
            } else if (view instanceof RelativeLayout) {
                typeArray = context.obtainStyledAttributes(attrs, R.styleable.HRelativeLayout);
            } else if (view instanceof ConstraintLayout) {
                typeArray = context.obtainStyledAttributes(attrs, R.styleable.HConstraintLayout);
            } else if (view instanceof Button) {
                typeArray = context.obtainStyledAttributes(attrs, R.styleable.HButton);
            } else if (view instanceof EditText) {
                typeArray = context.obtainStyledAttributes(attrs, R.styleable.HEditText);
            } else if (view instanceof TextView) {
                typeArray = context.obtainStyledAttributes(attrs, R.styleable.HTextView);
            } else if (view instanceof ImageView) {
                typeArray = context.obtainStyledAttributes(attrs, R.styleable.HImageView);
            } else {
                return;
            }

            if ((view instanceof TextView) && typeArray.getIndexCount() > 0) {
                ColorStateList colorStateList = DrawableFactory.getSelectorColor(typeArray);
                if (colorStateList != null) {
                    ((TextView) view).setTextColor(colorStateList);
                }
            }

            if (typeArray.hasValue(R.styleable.Background_h_aspect_ratio)) {
                mAspectRatio = typeArray.getFloat(R.styleable.Background_h_aspect_ratio, 0);
            } else {
                mAspectRatio = 0;
            }

            if (typeArray.getIndexCount() > 0) {
                //优先寻找Selector效果
                Drawable drawable = DrawableFactory.getSelectorDrawable(typeArray);
                if (drawable == null) {
                    //寻找Shape效果
                    drawable = DrawableFactory.getShapeDrawable(typeArray);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                } else {
                    view.setBackgroundDrawable(drawable);
                }

            }
        } catch (Exception e) {
            Log.e("shapeDraw", "请检查您使用的View背景的xml文件");
            e.printStackTrace();
        } finally {
            if (typeArray != null) {
                typeArray.recycle();
            }
        }
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec,
            int widthPadding, int heightPadding, @Nullable ViewGroup.LayoutParams layoutParams) {
        spec.width = widthMeasureSpec;
        spec.height = heightMeasureSpec;

        if (mAspectRatio <= 0f) {
            return;
        }

        AspectRatioMeasure.updateMeasureSpec(
                spec,
                mAspectRatio,
                layoutParams,
                widthPadding,
                heightPadding);
    }

}
