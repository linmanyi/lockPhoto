package com.nmk.myapplication.work.ui.dialog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FolderDialogMoreBinding
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.ui.common.BaseBottomDialog
import com.nmk.myapplication.work.utils.common.CommonDateFormatUtil
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import com.nmk.myapplication.work.vm.MainVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FolderMoreDialog(context: Context): BaseBottomDialog(context) {

    private lateinit var mViewBinding: FolderDialogMoreBinding
    private var id: Long = 0
    private val folderVM: MainVM by lazy {
        ViewModelProvider(context as ViewModelStoreOwner).get(MainVM::class.java)
    }

    companion object{
        fun showDialog(context: Context,id: Long) {
            val dialog = FolderMoreDialog(context).apply {
                this.id = id
            }
            dialog.showDialog()
        }
    }

    override fun getLayoutId() = R.layout.folder_dialog_more

    override fun initView() {
        mViewBinding = FolderDialogMoreBinding.bind(popupImplView)
        folderVM.viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val info = LockPhotoDB.getInstance().folderDao().queryDataById(id).getOrNull(0)
                val size = FileUtil.getAutoFileOrFilesSize(FileUtil.getSdCardPath() + FileConstance.getPrivateFolderPath(info?.fileName?:""))
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