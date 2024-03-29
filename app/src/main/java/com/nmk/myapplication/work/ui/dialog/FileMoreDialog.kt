package com.nmk.myapplication.work.ui.dialog

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FolderDialogMoreBinding
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.ui.common.BaseBottomDialog
import com.nmk.myapplication.work.utils.common.CommonDateFormatUtil
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import com.nmk.myapplication.work.vm.FileMV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileMoreDialog(context: Context): BaseBottomDialog(context) {

    private lateinit var mViewBinding: FolderDialogMoreBinding
    private var id: Long = 0
    private val fileVM: FileMV by lazy {
        ViewModelProvider(context as ViewModelStoreOwner).get(FileMV::class.java)
    }

    companion object{
        fun showDialog(context: Context,id: Long) {
            val dialog = FileMoreDialog(context).apply {
                this.id = id
            }
            dialog.showDialog()
        }
    }

    override fun getLayoutId() = R.layout.folder_dialog_more

    override fun initView() {
        mViewBinding = FolderDialogMoreBinding.bind(popupImplView)
        fileVM.viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val info = LockPhotoDB.getInstance().fileDao().queryDataById(id).getOrNull(0)
                val size = FileUtil.getAutoFileOrFilesSize(info?.cover)
//                val size = info?.size.toString()
                withContext(Dispatchers.Main){
                    info?.let {
                        mViewBinding.nameContentTv.text = it.fileName
                        mViewBinding.timeContentTv.text = CommonDateFormatUtil.getFormatHMYMD(it.createTime)
                        mViewBinding.sizeContentTv.text = size
                    }
                }
            }.onFailure {
                ToastUtils.showToast(context,it.message)
            }
        }
    }
}