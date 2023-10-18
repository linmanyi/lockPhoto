package com.nmk.myapplication.ui.view.numberKeyboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.nmk.myapplication.databinding.NumberKeyboardBinding

/**
 * 数字键盘
 */
class NumberKeyboardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val mViewBinding: NumberKeyboardBinding
    var listener: OnClickKeyListener? = null

    init {
        mViewBinding = NumberKeyboardBinding.inflate(LayoutInflater.from(context),this,true)
        val list = arrayListOf<TextView>()
        list.add(mViewBinding.number0)
        list.add(mViewBinding.number1)
        list.add(mViewBinding.number2)
        list.add(mViewBinding.number3)
        list.add(mViewBinding.number4)
        list.add(mViewBinding.number5)
        list.add(mViewBinding.number6)
        list.add(mViewBinding.number7)
        list.add(mViewBinding.number8)
        list.add(mViewBinding.number9)
        for (i in list) {
            i.setOnClickListener{
                //输出数字
                listener?.onClickNum(i.text.toString())
            }
        }
        mViewBinding.deleteImv.setOnClickListener{
            //减去数字
            listener?.onClickDelete()
        }
    }

    interface OnClickKeyListener{
        fun onClickNum(string: String)
        fun onClickDelete()
    }
}