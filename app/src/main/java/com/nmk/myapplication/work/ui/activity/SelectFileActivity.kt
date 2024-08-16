package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.jeremyliao.liveeventbus.LiveEventBus
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileItemTableBinding
import com.nmk.myapplication.databinding.FolderActivitySelectBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.base.EventConstant
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ext.visible
import com.nmk.myapplication.work.ext.visibleOrGone
import com.nmk.myapplication.work.ui.common.loading.LoadingManager
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.FileMV

/**
 * 选择图片
 */
class SelectFileActivity : BaseActivity<FileMV, FolderActivitySelectBinding>() {

    companion object {
        /**
         * 1-单选
         * 2-多选
         */
        fun startActivity(context: Context, id: Long, type: Int = 1) {
            context.startActivity(Intent(context, SelectFileActivity::class.java)
                .putExtra("id", id)
                .putExtra("type", type))
        }
    }

    private val list: ArrayList<FileInfo> = arrayListOf()
    private var id: Long = 0
    private var type: Int = 0

    override fun initView(savedInstanceState: Bundle?) {
        id = intent.getLongExtra("id",0)
        type = intent.getIntExtra("type",0)
        mViewBind.content.grid(3).setup {
            singleMode = type == 1
            addType<FileInfo> { R.layout.file_item_table }
            onBind {
                val binding = getBinding<FileItemTableBinding>()
                val model = getModel<FileInfo>()
                binding.selectImv.visible()
                if (ImageUtil.isImgLinkerUrl(model.content)) {
                    ImageUtil.loadFile(this@SelectFileActivity, binding.contentImv, model.content)
                } else if (ImageUtil.isMp4AnimUrl(model.content)) {
                    binding.contentImv.setImageResource(R.mipmap.icon_mp4)
                }
                binding.selectImv.setImageResource(if (model.select) R.mipmap.icon_select else R.mipmap.icon_un_select_trans)
                binding.contentImv.setClickNotDoubleListener {
                    //选中
                    model.select = !model.select
                    setChecked(layoutPosition, model.select)
                }
            }
            onChecked { position, isChecked, isAllChecked ->
                val model = getModel<FileInfo>(position)
                model.select = isChecked
                updateBt()
                notifyItemChanged(position)
            }
        }

        mViewBind.sureTv.visibleOrGone(type == 1)
        mViewBind.sureTv.setClickNotDoubleListener {
            if (type == 1) {
                LiveEventBus.get<List<FileInfo>>(EventConstant.SELECT_FILE).post(getSelect())
                finish()
            }
        }
        initEvent()
        mViewModel.getData(id)
        updateBt()
    }

    private fun initEvent() {
        mViewBind.deleteTv.setClickNotDoubleListener {
            //删除
            mViewModel.deleteFile(this, getSelect())
        }
        mViewBind.moveTv.setClickNotDoubleListener {
            //移动
            SelectFolderActivity.startActivity(this,id)
        }
    }

    override fun createObserver() {
        mViewModel.getDataDE.observeInActivity(this) {
            mViewBind.emptyLl.visibleOrGone(it.isEmpty())
            list.addAll(it)
            mViewBind.content.models = list
        }
        LiveEventBus.get<FolderInfo>(EventConstant.SELECT_FOLDER).observe(this) {
            mViewModel.moveFile(this,it.id,it.fileName, getSelect())
        }
        mViewModel.moveEd.observeInActivity(this) {
            LoadingManager.getInstance().hideDialog()
            if (!it)ToastUtils.showToast(this,getString(R.string.move_failure))
            finish()
        }
        mViewModel.deleteFileED.observeInActivity(this) {
            LoadingManager.getInstance().hideDialog()
            if (it)
                finish()
            else
                ToastUtils.showToast(this,getString(R.string.delete_failure))
        }
    }

    private fun updateBt() {
        if (getSelect().isNotEmpty()) {
            mViewBind.sureTv.alpha = 1f
            mViewBind.sureTv.isClickable = true
        } else {
            mViewBind.sureTv.alpha = 0.5f
            mViewBind.sureTv.isClickable = false
        }
        mViewBind.funLl.visibleOrGone(type == 2 && getSelect().isNotEmpty())
    }

    private fun getSelect(): ArrayList<FileInfo> {
        val arrayList = arrayListOf<FileInfo>()
        val filter = list.filter { it.select }
        arrayList.addAll(filter)
        return arrayList
    }
}