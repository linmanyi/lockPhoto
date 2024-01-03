package com.nmk.myapplication.work.ui.activity

import android.os.Bundle
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileItemBinding
import com.nmk.myapplication.databinding.FolderActivitySelectBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.dialog.FileMoreDialog
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.FileMV

/**
 * 选择图片
 */
class SelectFileActivity: BaseActivity<FileMV, FolderActivitySelectBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.content.grid(3).setup {
            addType<FileInfo> { R.layout.file_item }
            onBind {
                val binding = getBinding<FileItemBinding>()
                val model = getModel<FileInfo>()
                if (ImageUtil.isImgLinkerUrl(model.content)) {
                    ImageUtil.loadFile(this@SelectFileActivity, binding.contentImv, model.content)
                } else if (ImageUtil.isMp4AnimUrl(model.content)) {
                    binding.contentImv.setImageResource(R.mipmap.icon_mp4)
                }
                binding.selectImv.setImageResource(if (model.select) R.mipmap.icon_select else R.mipmap.icon_un_select_trans)
                binding.contentImv.setClickNotDoubleListener {
                    if (model.select) return@setClickNotDoubleListener
                    //选中
                    model.select = !model.select
                    setChecked(layoutPosition, model.select)
                }
            }
            onChecked { position, isChecked, isAllChecked ->


                val model = getModel<FileInfo>(position)
                model.select = isChecked
                notifyItemChanged(position)
            }
            onClick(R.id.moreImv) {
                //更多
                val model = getModel<FileInfo>()
                FileMoreDialog.showDialog(this@SelectFileActivity,model.id)
            }
        }
    }
}