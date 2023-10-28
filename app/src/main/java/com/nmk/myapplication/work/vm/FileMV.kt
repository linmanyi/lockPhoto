package com.nmk.myapplication.work.vm

import androidx.lifecycle.viewModelScope
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData

class FileMV : BaseViewModel() {
    val getDataDE = EventLiveData<ArrayList<FileInfo>>()
    fun getData(id: Long) {
        if (id == 0L) {
            getDataDE.postValue(arrayListOf())
            return
        }
        viewModelScope.launch {
            val list = arrayListOf<FileInfo>()
            kotlin.runCatching {
                val data = LockPhotoDB.getInstance().fileDao().queryDataById(id)
                data.forEach {
                    list.add(
                        FileInfo(
                            id = it.db_id,
                            fileName = it.fileName,
                            content = it.cover,
                            createTime = it.createTime,
                            width = it.width,
                            height = it.height,
                            type = it.type,
                            size = it.size,
                        )
                    )
                }
            }.onSuccess {
                getDataDE.postValue(list)
            }.onFailure {
                getDataDE.postValue(arrayListOf())
            }
        }
    }
}