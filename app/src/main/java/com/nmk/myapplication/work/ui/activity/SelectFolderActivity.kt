package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.jeremyliao.liveeventbus.LiveEventBus
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FolderActivitySelectBinding
import com.nmk.myapplication.databinding.FolderItemSelectBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.base.EventConstant
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.ext.gone
import com.nmk.myapplication.work.ext.visibleOrGone
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.FileMV
import com.nmk.myapplication.work.vm.MainVM

/**
 * 选择文件夹
 */
class SelectFolderActivity: BaseActivity<MainVM, FolderActivitySelectBinding>() {

    private var folderId: Long = 0L
    private val fileMv: FileMV by viewModels()

    companion object {
        fun startActivity(context: Context, id: Long) {
            context.startActivity(Intent(context,SelectFolderActivity::class.java).putExtra("folderId",id))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        folderId = intent.getLongExtra("folderId",0L)
        mViewBind.sureTv.gone()
        mViewBind.content.setup {
            addType<FolderInfo>(R.layout.folder_item_select)
            onBind {
                val binding = getBinding<FolderItemSelectBinding>()
                val model = getModel<FolderInfo>()
                if (model.cover.isNotEmpty()) ImageUtil.loadFile(this@SelectFolderActivity,binding.coverImv,model.cover)
                binding.nameTv.text = model.fileName
            }
            onClick(R.id.rootView) {
                val model = getModel<FolderInfo>()
                LiveEventBus.get<FolderInfo>(EventConstant.SELECT_FOLDER).post(model)
                finish()
            }
        }
        mViewModel.getData()
    }

    override fun createObserver() {
        mViewModel.getDataED.observeInActivity(this) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.removeIf { it.id == folderId }
            } else {
                var file: FolderInfo? = null
                it.forEach {
                    if (it.id == folderId) file = it
                }
                it.remove(file)
            }
            mViewBind.content.models = it
            mViewBind.emptyTv.visibleOrGone(it.isEmpty())
        }
    }
}