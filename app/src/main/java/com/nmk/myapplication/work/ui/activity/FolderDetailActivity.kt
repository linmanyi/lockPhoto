package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileItemBinding
import com.nmk.myapplication.databinding.FolderActivityFolderBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.common.loading.LoadingManager
import com.nmk.myapplication.work.ui.dialog.DeleteSettingDialog
import com.nmk.myapplication.work.ui.dialog.DeleteSettingDialog.Companion.DELETE_FILE_NO_PROMPT_KEY
import com.nmk.myapplication.work.ui.dialog.FileMoreDialog
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.work.utils.common.CacheUtil
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.FileMV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.ext.view.visibleOrGone
import java.io.File

/**
 * 文件夹内
 */
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
                val fileInfos = models as ArrayList<FileInfo>
                SecondActivity.startActivity(this@FolderDetailActivity,fileInfos,layoutPosition)
            }
        }
        mViewBind.titleBar.setTitle(fileName)
        mViewBind.titleBar.setLeftImgColor(getColor(R.color.white))
        mViewBind.titleBar.onClickLeftListener = object : TitleBar.OnClickLeftListener {
            override fun leftOnClick(v: View, isBack: Boolean) {
                this@FolderDetailActivity.finish()
            }
        }
        mViewBind.titleBar.onClickRightListener = object :TitleBar.OnClickRightListener {
            override fun rightOnClick(v: View, position: Int) {
                SelectFileActivity.startActivity(this@FolderDetailActivity,id,2)
            }

        }
        mViewBind.addImv.setClickNotDoubleListener {
            mViewModel.addFiles(this@FolderDetailActivity, id, fileName, null)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getData(id)
    }

    override fun createObserver() {
        mViewModel.getDataDE.observeInActivity(this) {
            mViewBind.emptyLl.visibleOrGone(it.isEmpty())
            adapter.models = it
            adapter.notifyDataSetChanged()
        }
        mViewModel.addFilesED.observeInActivity(this) {
            LoadingManager.getInstance().hideDialog()
            if (it.isSuccess) {
                val data = it.data
                mViewModel.getData(id)
                if (!CacheUtil.getBoolean(DELETE_FILE_NO_PROMPT_KEY,false)) {
                    DeleteSettingDialog.showDialog(this@FolderDetailActivity) {
                        //删除本地文件
                        val files = data?.map { File(it) }
                        files?.let { it1 -> mViewModel.deleteLocalFile(it1) }
                    }
                }
            } else {
                ToastUtils.showToast(this, getString(R.string.loading_error))
            }
        }
    }
}