package com.nmk.myapplication.work.vm

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.nmk.myapplication.work.base.BaseViewModel
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.ui.common.loading.LoadingManager
import com.nmk.myapplication.work.utils.EventLiveData
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SettingMV : BaseViewModel() {
    /**
     * 删除所有
     */
    val deleteAllED = EventLiveData<Boolean>()
    fun deleteAll(context: Context) {
        kotlin.runCatching {
            viewModelScope.launch(Dispatchers.IO) {
                LoadingManager.getInstance().showDialog(context)
                LockPhotoDB.getInstance().fileDao().deleteAll()
                LockPhotoDB.getInstance().folderDao().deleteAll()
                FileUtil.deleteFile(File(FileConstance.getFullPrivatePath()))
            }.invokeOnCompletion {
                deleteAllED.postValue(true)
            }
        }.onFailure {
            deleteAllED.postValue(false)
        }
    }
}