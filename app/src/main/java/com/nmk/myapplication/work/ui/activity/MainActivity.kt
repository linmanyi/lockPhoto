package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.databinding.ActivityMainBinding
import com.nmk.myapplication.databinding.FolderItemBinding
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.MainVM
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.view.visibleOrGone

/**
 * 主页
 */
class MainActivity : BaseActivity<MainVM, ActivityMainBinding>() {

    companion object{
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.content.grid(2).setup {
            addType<FolderInfo> { R.layout.folder_item }
            onBind {
                val binding = getBinding<FolderItemBinding>()
                val model = getModel<FolderInfo>()
                ImageUtil.loadFile(this@MainActivity,binding.coverImg,model.cover)
                binding.titleTv.text = model.fileName
            }
        }
    }

    override fun createObserver() {
        mViewModel.getDataED.observeInActivity(this) {
            mViewBind.content.models = it
            mViewBind.emptyTv.visibleOrGone(it.isEmpty())
        }
    }
}