package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.ActivityMainBinding
import com.nmk.myapplication.databinding.FolderItemBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.db.data.FolderModel
import com.nmk.myapplication.work.ui.dialog.FolderMoreDialog
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.MainVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                FolderDetailActivity.startActivity(this@MainActivity,model.id)
            }
            onClick(R.id.moreImv) {
                //更多
                val model = getModel<FolderInfo>()
                FolderMoreDialog.showDialog(this@MainActivity,model.id)
            }
        }
        mViewBind.titleBar.onClickRightListener = object : TitleBar.OnClickRightListener{
            override fun rightOnClick(v: View, position: Int) {
                AddFolderDialog.showDialog(this@MainActivity) {
                    //创建文件夹
                    mViewModel.viewModelScope.launch(Dispatchers.IO) {
                        kotlin.runCatching {
                            FileUtil.createMoreFiles("${FileConstance.mainPath}$it")
                            LockPhotoDB.getInstance().folderDao().insert(
                                FolderModel().apply {
                                    cover = ""
                                    fileName = it
                                    createTime = System.currentTimeMillis()
                                }
                            )
                        }.onFailure {
                            ToastUtils.showToast(this@MainActivity,"创建失败")
                        }.onSuccess {
                            mViewModel.getData()
                            ToastUtils.showToast(this@MainActivity,"创建成功")
                        }
                    }
                }
            }
        }
        mViewModel.getData()
    }

    override fun createObserver() {
        mViewModel.getDataED.observeInActivity(this) {
            mViewBind.content.models = it
            mViewBind.emptyTv.visibleOrGone(it.isEmpty())
        }
    }
}