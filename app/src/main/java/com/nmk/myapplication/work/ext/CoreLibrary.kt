package com.nmk.myapplication.work.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.EditText
import android.widget.TextView
import com.nmk.myapplication.app.MyApplication
import java.io.Serializable

/**
 *
 * @author Created by H-ray on 2022/12/30
 *
 * @desc : 核心库扩展
 *
 */

/**
 * 判断context是否Destroy
 */
fun Context?.isDestroy(): Boolean {
    return if (this == null) {
        true
    } else if (this is Activity) {
        this.isDestroy()
    } else {
        false
    }
}

/**
 * 判断activity是否Destroy
 */
fun Activity?.isDestroy(): Boolean {
    return this == null || this.isFinishing || this.isDestroyed
}

/**
 * getSerializable废弃的替代方案
 */
fun <T : Serializable?> Intent.getSerializableModel(key: String, m_class: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this.getSerializableExtra(key, m_class)
    else
        this.getSerializableExtra(key) as? T
}

/**
 * getSerializable废弃的替代方案
 */
fun <T : Serializable?> Bundle.getSerializableModel(key: String, m_class: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this.getSerializable(key, m_class)
    else
        this.getSerializable(key) as? T
}

/**
 * 布尔值设置view的显示与隐藏
 */
fun View.setVisibility(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

/**
 * 监听EditText输入变化
 */
fun EditText.setAfterTextChanged(block: ((s: String) -> Unit)? = {}) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            block?.invoke(s?.trim().toString())
        }
    })
}

/**
 * 补间动画Listener简化
 */
fun Animation.setListenerAdapter(endBlock: (animation: Animation?) -> Unit = {}) {
    setAnimationListener(object : AnimationListener {
        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            endBlock(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) {

        }
    })
}

/**
 * 是否为空
 */
fun EditText.isEmpty() = text.trim().toString().isEmpty()

/**
 * 转PX
 */
fun Int.px(): Int {
    val scale: Float = MyApplication.mContext.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Int.sp2px(): Int {
    val scale = MyApplication.mContext.resources.displayMetrics.scaledDensity
    return (this * scale + 0.5f).toInt()
}

/**
 * 转dp
 */
fun Int.px2Dp(): Float {
    val density = MyApplication.mContext.resources.displayMetrics.density
    return this / density
}
