package com.nmk.myapplication.work.ui.dialog

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import androidx.core.widget.addTextChangedListener
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.DialogAddFolderBinding
import com.nmk.myapplication.databinding.DialogDeleteSettingBinding
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.common.BaseBottomDialog
import com.nmk.myapplication.work.ui.common.BaseCenterDialog
import com.nmk.myapplication.work.utils.common.CacheUtil

/**
 * 添加文件夹弹窗
 */
class DeleteSettingDialog(context: Context): BaseCenterDialog(context) {

    private lateinit var mViewBinding: DialogDeleteSettingBinding
    private var block: ((Boolean) -> Unit)? = null
    private var isSelectNoPrompt = false

    companion object{
        fun showDialog(context: Context, block: ((Boolean) -> Unit)?) {
            DeleteSettingDialog(context).apply {
                this.block = block
            }.showDialog(BaseBottomDialog.Builder())
        }

        const val DELETE_FILE_NO_PROMPT_KEY = "delete_file_no_prompt_key"
    }

    override fun getLayoutId() = R.layout.dialog_delete_setting

    override fun initView() {
        mViewBinding = DialogDeleteSettingBinding.bind(popupImplView)
        mViewBinding.cancelTv.setClickNotDoubleListener{
            CacheUtil.putBoolean(DELETE_FILE_NO_PROMPT_KEY, isSelectNoPrompt)
            dismiss()
        }
        mViewBinding.checkboxImv.setClickNotDoubleListener {
            isSelectNoPrompt = !isSelectNoPrompt
            mViewBinding.checkboxImv.setImageResource(if (isSelectNoPrompt) R.mipmap.ic_select else R.mipmap.ic_un_select)
        }
        mViewBinding.sureTv.setClickNotDoubleListener {
            CacheUtil.putBoolean(DELETE_FILE_NO_PROMPT_KEY, isSelectNoPrompt)
            block?.invoke(isSelectNoPrompt)
            dismiss()
        }
    }
}