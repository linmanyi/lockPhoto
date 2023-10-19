package com.nmk.myapplication.work.utils.view

import android.os.SystemClock
import kotlin.math.abs

/**
 * @desc： 重复点击事件判断
 * @author： Created by H-ray on 2023/7/21.
 */
object NotClickDoubleUtil {

    private var lastClickTime: Long = 0

    /**
     * 连续点击事件在是否在680毫秒以内
     */
    fun isFastDoubleClick(): Boolean {
        val time = SystemClock.elapsedRealtime()
        if (abs(time - lastClickTime) < 680) {
            return true
        }
        lastClickTime = time
        return false
    }

    /**
     * 连续点击事件在是否在680毫秒以内
     */
    fun isFastDoubleClick(fastTime: Int): Boolean {
        val time = SystemClock.elapsedRealtime()
        if (abs(time - lastClickTime) < fastTime) {
            return true
        }
        lastClickTime = time
        return false
    }

}