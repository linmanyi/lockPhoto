package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.ActivityMainBinding
import com.nmk.myapplication.databinding.FolderItemBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.dialog.AddFolderDialog
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.MainVM
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
                if (model.cover.isNotEmpty()) {
                    ImageUtil.loadFile(this@MainActivity,binding.coverImg,model.cover)
                }
                binding.titleTv.text = model.fileName
            }
            onClick(R.id.rootView) {
                //进入文件夹
                val model = getModel<FolderInfo>()
                FolderDetailActivity.startActivity(this@MainActivity,model.id,model.fileName)
            }
            onClick(R.id.moreImv) {
                //更多
                val model = getModel<FolderInfo>()
                MainEditActivity.startActivity(this@MainActivity,model.id)
            }
        }
        mViewBind.titleBar.onClickRightListener = object : TitleBar.OnClickRightListener{
            override fun rightOnClick(v: View, position: Int) {
                AddFolderDialog.showDialog(this@MainActivity) {
                    mViewModel.addFolder(this@MainActivity,it)
                }
            }
        }
        mViewBind.settingImv.setClickNotDoubleListener {
            SettingActivity.startActivity(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getData()
    }

    override fun createObserver() {
        mViewModel.getDataED.observeInActivity(this) {
            mViewBind.content.models = it
            mViewBind.emptyTv.visibleOrGone(it.isEmpty())
        }
        mViewModel.addEd.observeInActivity(this) {
            ToastUtils.showToast(this,it)
        }
    }
}