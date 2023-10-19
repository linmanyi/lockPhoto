package com.nmk.myapplication.work.ui.view.verification

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.nmk.myapplication.databinding.LoginViewSingleVerificationCodeBinding
import kotlinx.coroutines.*

/**
 * 验证码输入View
 * @author Created by H-ray on 2022/12/27.
 */
class VerificationCodeSingleInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mViewBind: LoginViewSingleVerificationCodeBinding
    private var isShow: Boolean = false

    init {
        mViewBind = LoginViewSingleVerificationCodeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    /**
     * 设置内容
     */
    fun setValue(s: String?) {
        mViewBind.numberTv.text = s ?: ""
    }

    /**
     * 显示光标
     */
    fun isShowPoint(show: Boolean) {
        if (isShow == show) return
        isShow = show
        mViewBind.pointView.visibility = if (isShow) View.VISIBLE else View.GONE
        pointAnim()
    }

    private var animJob: Job? = null
    private fun pointAnim() {
        animJob?.cancel()
        if (isShow) {
            val animJob = CoroutineScope(Dispatchers.IO).launch {
                var i = 0
                while (isActive){
                    i++
                    delay(500)
                    withContext(Dispatchers.Main) {
                        mViewBind.pointView.visibility = if (i % 2 == 0) View.VISIBLE else View.GONE
                    }
                }
            }
            this.animJob = animJob
        }
    }

    override fun onDetachedFromWindow() {
        animJob?.cancel()
        animJob = null
        super.onDetachedFromWindow()
    }

}