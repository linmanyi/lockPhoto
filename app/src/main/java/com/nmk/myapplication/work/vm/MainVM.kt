package com.nmk.myapplication.work.vm

import androidx.lifecycle.viewModelScope
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.db.data.FolderModel
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
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

    val getFolderED = EventLiveData<FolderInfo?>()
    fun getFolder(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = arrayListOf<FolderInfo>()
            kotlin.runCatching {
                LockPhotoDB.getInstance().folderDao().queryDataById(id).forEach{
                    list.add(FolderInfo().apply {
                        this.id = it.db_id
                        fileName = it.fileName
                        cover = it.cover
                        createTime = it.createTime
                    })
                }
            }.onSuccess {
                getFolderED.postValue(list.getOrNull(0))
            }.onFailure {
                getFolderED.postValue(null)
            }
        }
    }

    val deleteFolderED = EventLiveData<Boolean>()
    fun deleteFolder(id: Long,name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                if (FileUtil.isExitFile(FileConstance.getPrivateFolderPath(name))) {
                    FileUtil.deleteFile(FileConstance.getPrivateFolderPath(name))
                }
                LockPhotoDB.getInstance().folderDao().deleteById(id)
            }.onSuccess {
                deleteFolderED.postValue(true)
            }.onFailure {
                deleteFolderED.postValue(false)
            }
        }
    }

    val editFolderED = EventLiveData<Boolean>()
    fun editFolderName(id: Long, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val model = LockPhotoDB.getInstance().folderDao().queryDataById(id)[0]
                model.fileName = name
                LockPhotoDB.getInstance().folderDao().update(model)
            }.onSuccess {
                editFolderED.postValue(true)
            }.onFailure {
                editFolderED.postValue(false)
            }
        }
    }

    fun editFolderCover(id: Long, path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val model = LockPhotoDB.getInstance().folderDao().queryDataById(id)[0]
                model.cover = path
                LockPhotoDB.getInstance().folderDao().update(model)
            }.onSuccess {
                editFolderED.postValue(true)
            }.onFailure {
                editFolderED.postValue(false)
            }
        }
    }
}