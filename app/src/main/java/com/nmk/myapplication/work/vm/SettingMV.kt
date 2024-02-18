package com.nmk.myapplication.work.vm

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.ui.common.loading.LoadingManager
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData

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
                FileUtil.deleteFile(FileConstance.getPrivatePath())
            }.invokeOnCompletion {
                deleteAllED.postValue(true)
            }
        }.onFailure {
            deleteAllED.postValue(false)
        }
    }
}