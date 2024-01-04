package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.jeremyliao.liveeventbus.LiveEventBus
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.databinding.FolderActivityEditBinding
import com.nmk.myapplication.databinding.FolderDialogMoreBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.base.EventConstant
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.helper.picture.PictureSelectHelper
import com.nmk.myapplication.work.ui.dialog.AddFolderDialog
import com.nmk.myapplication.work.utils.common.CommonDateFormatUtil
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.MainVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 文件夹设置
 */
class MainEditActivity: BaseActivity<MainVM, FolderActivityEditBinding>() {

    private var id: Long = 0

    companion object{
        fun startActivity(context: Context,id: Long) {
            context.startActivity(Intent(context, MainEditActivity::class.java).putExtra("id",id))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        id = intent.getLongExtra("id",0)
        mViewModel.getFolder(id)
        mViewBind.deleteTv.setClickNotDoubleListener {
            mViewModel.deleteFolder(id,mViewBind.titleEditView.text.toString())
        }
        mViewBind.titleEditView.setClickNotDoubleListener{
            AddFolderDialog.showDialog(this@MainEditActivity,mViewBind.titleEditView.text.toString()) {
                mViewModel.editFolderName(id,it)
            }
        }
        mViewBind.selectTv.setClickNotDoubleListener{
            SelectFileActivity.startActivity(this@MainEditActivity,id,1)
        }
    }

    override fun createObserver() {
        mViewModel.getFolderED.observeInActivity(this) {
            if (it != null) {
                mViewBind.titleEditView.text = it.fileName
                if (it.cover.isNotEmpty()) {
                    ImageUtil.loadImg(this@MainEditActivity,mViewBind.coverImv,it.cover)
                }
            } else {
                ToastUtils.showToast(this@MainEditActivity,"没有找到该文件夹")
            }
        }

        mViewModel.deleteFolderED.observeInActivity(this) {
            if (it)
                finish()
            else
                ToastUtils.showToast(this@MainEditActivity,"删除失败")
        }

        mViewModel.editFolderED.observeInActivity(this@MainEditActivity) {
            if (it.contains("name"))
                mViewBind.titleEditView.text = it["name"]
            else if (it.contains("cover"))
                ImageUtil.loadImg(this,mViewBind.coverImv,it["cover"])
            else
                ToastUtils.showToast(this@MainEditActivity,"修改失败")
        }
        LiveEventBus.get<List<FileInfo>>(EventConstant.SELECT_FILE).observe(this) {
            mViewModel.editFolderCover(id,it[0].content)
        }
    }
}