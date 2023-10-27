package com.nmk.myapplication.work.vm

import androidx.lifecycle.viewModelScope
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData

class MainVM: BaseViewModel() {
    /**
     * 获取数据库的文件夹
     */
    val getDataED = EventLiveData<ArrayList<FolderInfo>>()
    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = arrayListOf<FolderInfo>()
            kotlin.runCatching {
                LockPhotoDB.getInstance().folderDao().queryData().forEach{
                    list.add(FolderInfo().apply {
                        id = it.db_id
                        fileName = it.fileName
                        cover = it.cover
                        createTime = it.createTime
                    })
                }
            }.onSuccess {
                getDataED.postValue(list)
            }.onFailure {
                getDataED.postValue(arrayListOf())
            }
        }
    }
}