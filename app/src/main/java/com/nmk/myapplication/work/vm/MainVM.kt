package com.nmk.myapplication.work.vm

import androidx.lifecycle.viewModelScope
import com.nmk.myapplication.work.base.BaseViewModel
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.db.data.FolderModel
import com.nmk.myapplication.work.utils.EventLiveData
import com.nmk.myapplication.work.utils.common.LogUtil
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import com.nmk.myapplication.work.utils.http.data.DataUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

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

    val addEd = EventLiveData<String>()
    fun addFolder(name: String) {
        //创建文件夹
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                if (FileUtil.isExitFile(FileConstance.getPrivateFolderPath(name))) {
                    addEd.postValue("该文件夹已存在")
                } else {
                    LockPhotoDB.getInstance().folderDao().insert(
                        FolderModel().apply {
                            cover = ""
                            fileName = name
                            createTime = System.currentTimeMillis()
                        }
                    )
                    FileUtil.createMoreFiles(FileConstance.getPrivateFolderPath(name))
                    LogUtil.i("fileFun","delete ${FileConstance.getPrivateFolderPath(name)}")
                }
            }.onFailure {
                addEd.postValue("创建失败")
            }.onSuccess {
                getData()
                addEd.postValue("创建成功")
            }
        }
    }

    val getFolderED = EventLiveData<DataUiState<FolderInfo?>>()
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
                getFolderED.postValue(DataUiState(isSuccess = true, data = list.getOrNull(0)))
            }.onFailure {
                getFolderED.postValue(DataUiState(isSuccess = false, data = null))
            }
        }
    }

    val deleteFolderED = EventLiveData<Boolean>()
    fun deleteFolder(id: Long,name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                FileUtil.deleteFile(File(FileConstance.getFullPrivatePath(name)))
                LockPhotoDB.getInstance().folderDao().deleteById(id)
                LogUtil.i("fileFun","delete ${FileConstance.getFullPrivatePath(name)}")
            }.onFailure {
                deleteFolderED.postValue(false)
                LogUtil.i("fileFun","delete ${FileConstance.getFullPrivatePath(name)} error")
            }
        }.invokeOnCompletion {
            deleteFolderED.postValue(it == null)
        }
    }

    val editFolderED = EventLiveData<Map<String,String>>()
    fun editFolderName(id: Long, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                if (File(name).exists()) {
                    editFolderED.postValue(mapOf())
                    return@launch
                } else {
                    val model = LockPhotoDB.getInstance().folderDao().queryDataById(id)[0]
                    model.fileName = name
                    LockPhotoDB.getInstance().folderDao().update(model)
                }
            }.onSuccess {
                editFolderED.postValue(mapOf(
                    Pair("name",name)
                ))
            }.onFailure {
                editFolderED.postValue(mapOf())
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
                editFolderED.postValue(mapOf(
                    Pair("cover",path)
                ))
            }.onFailure {
                editFolderED.postValue(mapOf())
            }
        }
    }
}