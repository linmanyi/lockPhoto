package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileItemBinding
import com.nmk.myapplication.databinding.FolderActivityFolderBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.FileMV

class FolderDetailActivity: BaseActivity<FileMV, FolderActivityFolderBinding>() {
    private var id = 0L

    companion object{
        fun startActivity(context: Context,id: Long) {
            context.startActivity(Intent(context, FolderDetailActivity::class.java).putExtra("id",id))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        id = intent.getLongExtra("id", 0)
        mViewBind.content.grid(3).setup {
            addType<FileInfo> { R.layout.file_item }
            onBind {
                val binding = getBinding<FileItemBinding>()
                val model = getModel<FileInfo>()
                if (ImageUtil.isImgLinkerUrl(model.content)) {
                    ImageUtil.loadFile(this@FolderDetailActivity,binding.contentImv,model.content)
                } else if (ImageUtil.isMp4AnimUrl(model.content)){
                    binding.contentImv.setImageResource(R.mipmap.icon_mp4)
                }

            }
            onClick(R.id.rootView) {
                //进入文件

            }
            onClick(R.id.moreImv) {
                //更多
            }
        }
        mViewModel.getData(id)
    }

    override fun createObserver() {
        mViewModel.getDataDE.observeInActivity(this) {
            mViewBind.content
        }
    }
}