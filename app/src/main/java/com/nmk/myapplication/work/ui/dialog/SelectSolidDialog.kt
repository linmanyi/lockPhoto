package com.nmk.myapplication.work.ui.dialog

import android.content.Context
import com.drake.brv.utils.setup
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileDialogSelectSolidBinding
import com.nmk.myapplication.databinding.FileItemSelectSolidBinding
import com.nmk.myapplication.work.date.FileSolidBean
import com.nmk.myapplication.work.ui.common.BaseBottomDialog

/**
 * @desc: 选中更多排序弹窗
 * @author：Created by my on 2024/8/16
 */
class SelectSolidDialog(context: Context): BaseBottomDialog(context) {

    private lateinit var mViewBinding: FileDialogSelectSolidBinding

    override fun getLayoutId() = R.layout.file_dialog_select_solid

    override fun initView() {
        mViewBinding = FileDialogSelectSolidBinding.bind(popupImplView)
        mViewBinding.rv.setup {
            addType<FileSolidBean> { R.layout.file_item_select_solid }
            onBind {
                val model = getModel<FileSolidBean>()
                val binding = getBinding<FileItemSelectSolidBinding>()
                binding
            }
        }
    }

}