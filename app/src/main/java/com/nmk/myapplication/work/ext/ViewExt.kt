package com.nmk.myapplication.work.ext

import android.view.View
import com.nmk.myapplication.work.utils.view.NotClickDoubleUtil

/**
 * 限制连击事件
 * @receiver View
 * @param listener OnClickListener
 */
fun View.setClickNotDoubleListener(listener: View.OnClickListener) {
    setOnClickListener {
        if (NotClickDoubleUtil.isFastDoubleClick()) return@setOnClickListener
//        LogUtil.click(it)
        listener.onClick(it)
    }
}