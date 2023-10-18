package com.hray.library.widget.shape.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Created by gan on 2022/5/13 11:13
 * 防止 setSpan (-1 ... -1) starts before 0 错误
 */
open class CustomEditText : AppCompatEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 防止 index 为 -1
     */
    override fun setSelection(index: Int) {
        if (index >= 0) {
            super.setSelection(index)
        }
    }

    /**
     * 防止 start stop 为 -1
     */
    override fun setSelection(start: Int, stop: Int) {
        if (start >= 0 && stop >= 0 && start <= stop) {
            super.setSelection(start, stop)
        }
    }


}