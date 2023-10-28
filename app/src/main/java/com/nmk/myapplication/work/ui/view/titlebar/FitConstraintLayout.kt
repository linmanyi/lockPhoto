package com.nmk.myapplication.work.ui.view.titlebar

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowInsetsCompat

/**
 * VoiceRoom
 * @author Created by H-ray on 2022/10/18.
 */
class FitConstraintLayout(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    /**
     * 修复 fitsSystemWindows 与软键盘的冲突问题
     * */
    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        val copyInsets = insets?.let {
            val origin = WindowInsetsCompat.toWindowInsetsCompat(it)
            origin.replaceSystemWindowInsets(0, origin.systemWindowInsetTop, 0, 0)
        }
        return super.onApplyWindowInsets(copyInsets?.toWindowInsets()?:insets)
    }
}