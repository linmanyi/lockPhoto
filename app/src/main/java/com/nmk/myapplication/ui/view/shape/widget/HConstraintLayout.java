package com.nmk.myapplication.ui.view.shape.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.nmk.myapplication.ui.view.shape.ViewAttrsHelper;


/**
 * @author H-ray
 * @desc：
 * @create_time： 2020/1/2
 * @update_time： 2020/1/2
 */
public class HConstraintLayout extends ConstraintLayout {

    private ViewAttrsHelper mViewAttrsHelper;

    public HConstraintLayout(Context context) {
        super(context);
    }

    public HConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HConstraintLayout(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mViewAttrsHelper = new ViewAttrsHelper();
        mViewAttrsHelper.initAttrsDrawable(this, context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewAttrsHelper.onMeasure(widthMeasureSpec, heightMeasureSpec,
                getPaddingStart() + getPaddingEnd(),
                getPaddingTop() + getPaddingBottom(),
                getLayoutParams());
        super.onMeasure(mViewAttrsHelper.spec.width, mViewAttrsHelper.spec.height);
    }

}
