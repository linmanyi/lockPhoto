package com.nmk.myapplication.ui.view.verification

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.nmk.myapplication.databinding.LoginViewVerificationCodeBinding
import com.nmk.myapplication.ui.view.TextWatcherNew

/**
 * 验证码输入View
 * @author Created by H-ray on 2022/12/27.
 */
class VerificationCodeInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var numViewList: MutableList<VerificationCodeSingleInputView>
    var codeInputListener: ((String) -> Unit)? = null
    private val mViewBind: LoginViewVerificationCodeBinding

    init {
        mViewBind = LoginViewVerificationCodeBinding.inflate(LayoutInflater.from(context), this, true)
        numViewList = mutableListOf(mViewBind.codeView0, mViewBind.codeView1, mViewBind.codeView2, mViewBind.codeView3, mViewBind.codeView4, mViewBind.codeView5)
        mViewBind.inputEt.addTextChangedListener(object : TextWatcherNew() {
            override fun afterTextChanged(s: Editable?) {
                dealInputValue(s?.toString())
            }
        })

        //弹出软键盘，选中第一个
        mViewBind.inputEt.setText("")
    }

    fun getEtView() = mViewBind.inputEt

    /**
     * 处理输入的内容
     */
    private fun dealInputValue(s: String?) {
        if (s?.isNotEmpty() == true) {
            val sLength = s.length
            for ((position, view) in numViewList.withIndex()) {
                if (position > sLength - 1) {
                    //光标没有到达的位置，隐藏
                    view.setValue("")
                    view.isShowPoint(false)
                } else if (position == sLength - 1) {
                    //刚好到达这个
                    view.setValue(s[sLength - 1].toString())
                    view.isShowPoint(false)
                    if (numViewList.size == sLength) {
                        //最后一个
                        codeInputListener?.invoke(s)
                        return
                    }
                }
            }
            //把下一个光标显示
            numViewList[sLength].isShowPoint(true)
        } else {
            for (view in numViewList) {
                view.setValue("")
                view.isShowPoint(false)
            }
            numViewList[0].isShowPoint(true)
        }
    }

    /**
     * 清楚内容
     */
    fun clearValue() {
        mViewBind.inputEt.setText("")
    }


}