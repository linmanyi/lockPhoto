package com.nmk.myapplication.work.vm

import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
import com.luck.picture.lib.entity.LocalMedia
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.db.data.FileModel
import com.nmk.myapplication.work.helper.picture.PictureSelectHelper
import com.nmk.myapplication.work.network.http.data.DataUiState
import com.nmk.myapplication.work.ui.common.loading.LoadingManager
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import com.nmk.myapplication.work.utils.glide.ImageUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData
import java.io.File

class FileMV : BaseViewModel() {
    /**
     * 获取文件数据
     */
    val getDataDE = EventLiveData<ArrayList<FileInfo>>()
    fun getData(id: Long) {
        if (id == 0L) {
            getDataDE.postValue(arrayListOf())
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val list = arrayListOf<FileInfo>()
            kotlin.runCatching {
                val data = LockPhotoDB.getInstance().fileDao().queryDataByFolder(id)
                data.forEach {
                    list.add(
                        FileInfo(
                            id = it.db_id,
                            fileName = it.fileName,
                            folderId = it.folderId,
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

    /**
     * 新增文件
     */
    val addFilesED = EventLiveData<DataUiState<List<String>>>()
    fun addFiles(
        context: Context,
        folderId: Long,
        folderName: String,
        select: ArrayList<LocalMedia?>?
    ) {
        LoadingManager.getInstance().showDialog(context)
        val list = arrayListOf<FileModel>()
        PictureSelectHelper.getFiles(context, select) { localMedias ->
            kotlin.runCatching {
                viewModelScope.launch(Dispatchers.IO) {
                    //复制迁移文件
                    localMedias?.forEach {
                        if (File(it?.realPath.toString()).exists()) {
                            val timeMillis = System.currentTimeMillis()
                            val path =
                                FileConstance.getPrivateFilePath(folderName, it?.fileName ?: "")
                            FileUtil.saveFile(File(it?.realPath), path)
                            val fileSize =
                                FileUtil.getAutoFileOrFilesSize(path)
                            val strings = path.split(".")
                            list.add(FileModel().apply {
                                this.fileName = it?.fileName.toString()
                                this.folderId = folderId
                                cover = FileUtil.getSdCardPath() + path
                                createTime = timeMillis
                                width = it?.width ?: 0
                                height = it?.height ?: 0
                                type = strings[strings.size - 1].uppercase()
                                size = fileSize
                            })
                        }
                    }
                    LockPhotoDB.getInstance().fileDao().insert(list)
                    //更新文件夹封面为最后一张添加的图片
                    if (ImageUtil.isImgLinkerUrl(list.last().cover)) {
                        val folderModels = LockPhotoDB.getInstance().folderDao().queryDataById(folderId)
                        folderModels[0].cover = list.last().cover
                        LockPhotoDB.getInstance().folderDao().update(folderModels[0])
                    }
                }.invokeOnCompletion {
                    addFilesED.postValue(DataUiState(isSuccess = true, data = localMedias?.map { it?.realPath?: "" }))
                }
            }.onFailure {
                addFilesED.postValue(DataUiState(isSuccess = false))
            }
        }
    }

    val deleteFileED = EventLiveData<Boolean>()
    fun deleteFile(
        context: Context,
        infos: ArrayList<FileInfo>,
    ) {
        LoadingManager.getInstance().showDialog(context)
        kotlin.runCatching {
            viewModelScope.launch(Dispatchers.IO) {
                val models = arrayListOf<FileModel>()
                infos.forEach {
                    FileUtil.deleteFile(it.content)
                    models.add(it.toModel())
                }
                kotlin.runCatching {
                    LockPhotoDB.getInstance().fileDao().deleteList(models)
                }.onSuccess {
                    deleteFileED.postValue(true)
                }.onFailure {
                    deleteFileED.postValue(false)
                }
            }
        }.onFailure {
            deleteFileED.postValue(false)
        }
    }

    fun deleteLocalFile(files: List<File>) {
        kotlin.runCatching {
            viewModelScope.launch {
                files.forEach {
                    FileUtil.deleteFile(it)
                }
            }
        }
    }

    val moveEd = EventLiveData<Boolean>()
    fun moveFile(context: Context, newFolderId: Long, newFolderName: String, files: ArrayList<FileInfo>) {
        LoadingManager.getInstance().showDialog(context)
        kotlin.runCatching {
            viewModelScope.launch(Dispatchers.IO) {
                for (file in files) {
                    val oldPath = file.content
                    val model = file.apply {
                        folderId = newFolderId
                        content = FileUtil.getSdCardPath() + FileConstance.getPrivateFilePath(newFolderName, file.fileName)
                    }.toModel()
                    LockPhotoDB.getInstance().fileDao().updateFolder(model)
                    //迁移文件
                    if (File(oldPath).exists()) {
                        val newPath = FileConstance.getPrivateFilePath(newFolderName, file.fileName)
                        FileUtil.saveFile(File(oldPath), newPath)
                        FileUtil.deleteFile(File(oldPath))
                    }
                }
            }.invokeOnCompletion {
                if (it == null)
                    moveEd.postValue(true)
                else {
                    moveEd.postValue(false)
                }
            }
        }.onFailure {
            moveEd.postValue(false)
        }
    }

    /**
     * 下载文件-假
     */
    val downloadED = EventLiveData<Boolean>()
    fun downloadFile(context: Context, files: ArrayList<FileInfo>) {
        LoadingManager.getInstance().showDialog(context)
        kotlin.runCatching {
            viewModelScope.launch(Dispatchers.IO) {
                for (file in files) {
                    //将文件保持到本地相册
                    if (File(file.content).exists()) {
                        FileUtil.saveLoadFile(File(file.content), file.fileName)
                    }
                }
            }.invokeOnCompletion {
                if (it == null)
                    downloadED.postValue(true)
                else {
                    downloadED.postValue(false)
                }
            }
        }.onFailure {
            downloadED.postValue(false)
        }
    }
}