package com.nmk.myapplication.work.ui.dialog

import android.content.Context
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileDialogSelectSolidBinding
import com.nmk.myapplication.databinding.FileItemSelectSolidBinding
import com.nmk.myapplication.work.date.FileSolidBean
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ext.visibleOrGone
import com.nmk.myapplication.work.ui.common.BaseBottomDialog

/**
 * @desc: 选中更多排序弹窗
 * @author：Created by my on 2024/8/16
 */
class SelectSolidDialog(context: Context) : BaseBottomDialog(context) {

    private lateinit var mViewBinding: FileDialogSelectSolidBinding
    private var id: Int = 0
    private var isAscending: Boolean = true
    private var block: ((Int,Boolean) -> Unit)? = null

    override fun getLayoutId() = R.layout.file_dialog_select_solid

    companion object {
        fun showDialog(context: Context, id: Int, isAscending: Boolean, block: ((Int,Boolean) -> Unit)?) {
            val dialog = SelectSolidDialog(context).apply {
                this@apply.id = id
                this@apply.isAscending = isAscending
                this@apply.block = block
            }
            dialog.showDialog()
        }
    }

    override fun initView() {
        mViewBinding = FileDialogSelectSolidBinding.bind(popupImplView)
        mViewBinding.rv.setup {
            addType<FileSolidBean> { R.layout.file_item_select_solid }
            onBind {
                val model = getModel<FileSolidBean>()
                val binding = getBinding<FileItemSelectSolidBinding>()
                binding.rootView.setClickNotDoubleListener {
                    block?.invoke(model.id,!isAscending)
                    dismiss()
                }
                binding.nameTv.text = model.name
                binding.rootView.isSelected = model.isSelect
                binding.solidImv.visibleOrGone(model.isSelect)
                binding.solidImv.rotation = if (model.isAscending) 0f else 180f
                binding.stateTv.visibleOrGone(model.isSelect)
                binding.stateTv.text = if (model.isAscending) {
                    "升序"
                } else {
                    "降序"
                }
            }
        }
        val listOf = arrayListOf(
            FileSolidBean(0, "文件名称"),
            FileSolidBean(1, "创建日期"),
            FileSolidBean(2, "文件大小"),
            FileSolidBean(3, "文件类型"),
        )
        val first = listOf.first { it.id == id }
        first.isSelect = true
        first.isAscending = isAscending
        mViewBinding.rv.models = listOf
    }

}