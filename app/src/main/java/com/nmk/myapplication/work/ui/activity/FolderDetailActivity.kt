package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileItemListBinding
import com.nmk.myapplication.databinding.FileItemTableBinding
import com.nmk.myapplication.databinding.FolderActivityFolderBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.ext.invisible
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ext.visible
import com.nmk.myapplication.work.ext.visibleOrGone
import com.nmk.myapplication.work.ext.visibleOrInvisible
import com.nmk.myapplication.work.ui.common.loading.LoadingManager
import com.nmk.myapplication.work.ui.dialog.DeleteSettingDialog
import com.nmk.myapplication.work.ui.dialog.DeleteSettingDialog.Companion.DELETE_FILE_NO_PROMPT_KEY
import com.nmk.myapplication.work.ui.dialog.SelectSolidDialog
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.work.utils.common.CacheUtil
import com.nmk.myapplication.work.utils.common.CommonDateFormatUtil
import com.nmk.myapplication.work.utils.file.FileUtil
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.FileMV
import java.io.File

/**
 * 文件夹内
 */
class FolderDetailActivity : BaseActivity<FileMV, FolderActivityFolderBinding>() {
    private var id = 0L
    private var fileName = ""
    private lateinit var adapter: BindingAdapter
    private var type = TABLE
    private var checkVisibility: Boolean = false//是否显示选择
    private var solidId = 1
    private var solidIsAscending = true

    companion object {
        fun startActivity(context: Context, id: Long, fileName: String) {
            context.startActivity(
                Intent(context, FolderDetailActivity::class.java)
                    .putExtra("id", id)
                    .putExtra("fileName", fileName)
            )
        }

        const val LIST = 1
        const val TABLE = 2
    }

    override fun initView(savedInstanceState: Bundle?) {
        id = intent.getLongExtra("id", 0)
        fileName = intent.getStringExtra("fileName") ?: ""
        adapter = mViewBind.content.grid(
            when(this@FolderDetailActivity.type) {
                LIST -> 1
                TABLE -> 3
                else -> 3
            }).setup {
            addType<FileInfo> { when(this@FolderDetailActivity.type) {
                LIST -> R.layout.file_item_list
                TABLE -> R.layout.file_item_table
                else -> R.layout.file_item_table
            } }
            onBind {
                if (checkVisibility) singleMode = false
                val model = getModel<FileInfo>()
                when(type) {
                    LIST -> {
                        val binding = getBinding<FileItemListBinding>()
                        if (ImageUtil.isImgLinkerUrl(model.content)) {
                            ImageUtil.loadFile(this@FolderDetailActivity, binding.fileImv, model.content)
                        } else if (ImageUtil.isMp4AnimUrl(model.content)) {
                            binding.fileImv.setImageResource(R.mipmap.icon_mp4)
                        }
                        binding.timeTv.text = CommonDateFormatUtil.getFormatHMYMD(model.createTime)
                        binding.titleTv.text = model.fileName
                        binding.sizeTv.text = FileUtil.FormetFileSize(model.size)
                        binding.selectImv.visibleOrGone(checkVisibility)
                        binding.selectImv.setImageResource(if (model.select) R.mipmap.icon_select else R.mipmap.icon_un_select_trans)
                    }
                    TABLE -> {
                        val binding = getBinding<FileItemTableBinding>()
                        if (ImageUtil.isImgLinkerUrl(model.content)) {
                            ImageUtil.loadFile(this@FolderDetailActivity, binding.contentImv, model.content)
                        } else if (ImageUtil.isMp4AnimUrl(model.content)) {
                            binding.contentImv.setImageResource(R.mipmap.icon_mp4)
                        }
                        binding.selectImv.visibleOrGone(checkVisibility)
                        binding.selectImv.setImageResource(if (model.select) R.mipmap.icon_select else R.mipmap.icon_un_select_trans)
                    }
                }
            }
            onChecked { position, isChecked, isAllChecked ->
                val model = getModel<FileInfo>(position)
                model.select = isChecked
                notifyItemChanged(position)
            }
            onClick(R.id.rootView) {
                if (checkVisibility) {
                    //选中
                    val model = getModel<FileInfo>()
                    model.select = !model.select
                    setChecked(layoutPosition, model.select)
                } else {
                    //进入文件
                    val fileInfos = models as ArrayList<FileInfo>
                    SecondActivity.startActivity(this@FolderDetailActivity,fileInfos,layoutPosition)
                }
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
                updateTitleBar(true)
            }
        }
        mViewBind.cancelTv.setClickNotDoubleListener {
            updateTitleBar(false)
        }
        mViewBind.addImv.setClickNotDoubleListener {
            mViewModel.addFiles(this@FolderDetailActivity, id, fileName, null)
        }
        mViewBind.selectAllTv.setClickNotDoubleListener {
            if (!checkVisibility) return@setClickNotDoubleListener
            (adapter._data as? MutableList<FileInfo>)?.forEach {
                it.select = true
            }
            adapter.notifyDataSetChanged()
        }
        mViewBind.deleteTv.setClickNotDoubleListener {
            mViewModel.deleteFile(this@FolderDetailActivity,getSelect())
        }
        mViewBind.moveTv.setClickNotDoubleListener {mViewBind.moveTv.setClickNotDoubleListener {
            //移动
            SelectFolderActivity.startActivity(this,id)
        }}
        mViewBind.typeTv.setClickNotDoubleListener {
            type = if (type == TABLE) {
                LIST
            } else {
                TABLE
            }
            (mViewBind.content.layoutManager as GridLayoutManager).spanCount = when(this@FolderDetailActivity.type) {
                LIST -> 1
                TABLE -> 3
                else -> 3
            }
            adapter.notifyDataSetChanged()
        }
        mViewBind.solidTv.setClickNotDoubleListener {
            SelectSolidDialog.showDialog(this,solidId,solidIsAscending) { id, isAscending ->
                mViewModel.getData(this.id,id,isAscending)
                solidId = id
                solidIsAscending = isAscending
            }
        }
    }

    private fun updateTitleBar(boolean: Boolean) {
        checkVisibility = boolean
        adapter.notifyDataSetChanged()
        mViewBind.titleBar.visibleOrInvisible(!boolean)
        mViewBind.selectAllTv.visibleOrGone(boolean)
        mViewBind.cancelTv.visibleOrGone(boolean)
        mViewBind.addImv.visibleOrGone(!boolean)
        mViewBind.listFunLl.visibleOrGone(!boolean)
    }

    private fun getSelect(): ArrayList<FileInfo> {
        val arrayList = arrayListOf<FileInfo>()
        val filter = (adapter._data as? MutableList<FileInfo>)?.filter { it.select }
        filter?.toList()?.let { arrayList.addAll(it) }
        return arrayList
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getData(id,solidId,solidIsAscending)
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
                mViewModel.getData(id,solidId,solidIsAscending)
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
        mViewModel.deleteFileED.observeInActivity(this) {
            LoadingManager.getInstance().hideDialog()
            if (it)
                mViewModel.getData(id,solidId,solidIsAscending)
            else
                ToastUtils.showToast(this,getString(R.string.delete_failure))
        }
    }
}