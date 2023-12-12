package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileItemBinding
import com.nmk.myapplication.databinding.FolderActivityFolderBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.FileMV
import me.hgj.jetpackmvvm.ext.view.visibleOrGone

class FolderDetailActivity : BaseActivity<FileMV, FolderActivityFolderBinding>() {
    private var id = 0L
    private var fileName = ""
    private lateinit var adapter: BindingAdapter

    companion object {
        fun startActivity(context: Context, id: Long, fileName: String) {
            context.startActivity(
                Intent(context, FolderDetailActivity::class.java)
                    .putExtra("id", id)
                    .putExtra("fileName", fileName)
            )
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        id = intent.getLongExtra("id", 0)
        fileName = intent.getStringExtra("fileName") ?: ""
        adapter = mViewBind.content.grid(3).setup {
            addType<FileInfo> { R.layout.file_item }
            onBind {
                val binding = getBinding<FileItemBinding>()
                val model = getModel<FileInfo>()
                if (ImageUtil.isImgLinkerUrl(model.content)) {
                    ImageUtil.loadFile(this@FolderDetailActivity, binding.contentImv, model.content)
                } else if (ImageUtil.isMp4AnimUrl(model.content)) {
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
        mViewBind.titleBar.onClickLeftListener = object : TitleBar.OnClickLeftListener {
            override fun leftOnClick(v: View, isBack: Boolean) {
                this@FolderDetailActivity.finish()
            }
        }
        mViewModel.getData(id)
        mViewBind.addImv.setClickNotDoubleListener {
            mViewModel.addFiles(this@FolderDetailActivity, id, fileName, null)
        }
    }

    override fun createObserver() {
        mViewModel.getDataDE.observeInActivity(this) {
            mViewBind.emptyLl.visibleOrGone(it.isEmpty())
            adapter.models = it
            adapter.notifyDataSetChanged()
        }
        mViewModel.addFilesED.observeInActivity(this) {
            mViewModel.getData(id)
        }
    }
}