package com.nmk.myapplication.work.ui.view.shape.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.nmk.myapplication.work.ui.view.shape.ViewAttrsHelper;

/**
 * @author H-ray
 * @desc：
 * @create_time： 2019/12/31
 * @update_time： 2019/12/31
 */
public class HImageView extends AppCompatImageView {

    private ViewAttrsHelper mViewAttrsHelper;

    public HImageView(Context context) {
        super(context);
    }

    public HImageView(Context context,
                      @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HImageView(Context context,
                      @Nullable AttributeSet attrs, int defStyleAttr) {
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
