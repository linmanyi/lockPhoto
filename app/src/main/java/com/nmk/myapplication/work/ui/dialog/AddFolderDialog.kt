package com.nmk.myapplication.work.ui.dialog

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import androidx.core.widget.addTextChangedListener
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.DialogAddFolderBinding
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.common.BaseBottomDialog
import com.nmk.myapplication.work.ui.common.BaseCenterDialog

/**
 * 添加文件夹弹窗
 */
class AddFolderDialog(context: Context): BaseCenterDialog(context) {

    private lateinit var mViewBinding: DialogAddFolderBinding
    private val maxTextLength = 16
    private var block: ((String) -> Unit)? = null

    companion object{
        fun showDialog(context: Context,block: ((String) -> Unit)?) {
            AddFolderDialog(context).apply {
                this.block = block
            }.showDialog(BaseBottomDialog.Builder(dismissOnTouchOutside = false))
        }
    }

    override fun getLayoutId() = R.layout.dialog_add_folder

    override fun initView() {
        mViewBinding = DialogAddFolderBinding.bind(popupImplView)
        mViewBinding.closeImv.setClickNotDoubleListener{
            dismiss()
        }
        mViewBinding.edTv.addTextChangedListener {
            initBt(it)
        }
        mViewBinding.edTv.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxTextLength))
        mViewBinding.sureTv.setClickNotDoubleListener{
            block?.let { it1 -> it1(mViewBinding.edTv.text.toString()) }
            dismiss()
        }
        initBt(null)
    }

    private fun initBt(it: Editable?) {
        if (it?.isEmpty() == true) {
            mViewBinding.sureTv.isClickable = false
            mViewBinding.sureTv.isSelected = false
        } else {
            mViewBinding.sureTv.isClickable = true
            mViewBinding.sureTv.isSelected = true
        }
    }

}