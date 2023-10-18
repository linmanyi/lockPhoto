package com.nmk.myapplication.ui.view.shape.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.hray.library.widget.shape.ViewAttrsHelper;

/**
 * @author H-ray
 * @desc：
 * @create_time： 2020/1/2
 * @update_time： 2020/1/2
 */
public class HRelativeLayout extends RelativeLayout {

    private ViewAttrsHelper mViewAttrsHelper;

    public HRelativeLayout(Context context) {
        super(context);
    }

    public HRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
