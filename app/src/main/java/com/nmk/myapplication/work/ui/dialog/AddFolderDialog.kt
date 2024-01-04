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
    private var name: String? = null

    companion object{
        fun showDialog(context: Context, name: String? = null, block: ((String) -> Unit)?) {
            AddFolderDialog(context).apply {
                this.block = block
                this.name = name
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
            initBt(it.toString())
        }
        mViewBinding.edTv.setText(name)
        mViewBinding.edTv.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxTextLength))
        mViewBinding.sureTv.setClickNotDoubleListener{
            block?.let { it1 -> it1(mViewBinding.edTv.text.toString()) }
            dismiss()
        }
        initBt(name)
    }

    private fun initBt(it: String?) {
        if (it?.isEmpty() == true) {
            mViewBinding.sureTv.isClickable = false
            mViewBinding.sureTv.isSelected = false
        } else {
            mViewBinding.sureTv.isClickable = true
            mViewBinding.sureTv.isSelected = true
        }
    }

}