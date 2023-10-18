package com.nmk.myapplication.ui.view.shape.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.nmk.myapplication.ui.view.shape.ViewAttrsHelper;

/**
 * 超级TextView
 * <li> layout文件中方便设置shape，drawable  </li>
 * <li> 代码控制html样式  </li>
 * @author H-ray
 * @desc：
 */
public class HTextView extends AppCompatTextView {

    private ViewAttrsHelper mViewAttrsHelper;

    public HTextView(Context context) {
        super(context);
    }

    public HTextView(Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HTextView(Context context,
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
