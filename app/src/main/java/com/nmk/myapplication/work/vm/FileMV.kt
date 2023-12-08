package com.nmk.myapplication.work.vm

import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
import com.luck.picture.lib.entity.LocalMedia
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.db.data.FileModel
import com.nmk.myapplication.work.helper.picture.PictureSelectHelper
import com.nmk.myapplication.work.utils.file.FileConstance
import com.nmk.myapplication.work.utils.file.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData
import java.io.File

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

    val addFilesED = EventLiveData<Boolean>()
    fun addFiles(context: Context, folderName: String, select: ArrayList<LocalMedia?>?) {
        PictureSelectHelper.getDynamicImg(context, select) {
            kotlin.runCatching {
                val list = arrayListOf<FileModel>()
                viewModelScope.launch(Dispatchers.IO) {
                    //复制迁移文件
                    it?.forEach {
                        if (File(it?.realPath.toString()).exists()) {
                            val timeMillis = System.currentTimeMillis()
                            val path = "${FileConstance.mainPath}${folderName}/$timeMillis${it?.fileName}"
                            FileUtil.saveFile(BitmapFactory.decodeFile(it?.realPath), path)
                            delay(500L)
                            val fileSize = FileUtil.getAutoFileOrFilesSize(path)
                            list.add(FileModel().apply {
                                this.fileName = it?.fileName.toString()
                                cover = path
                                createTime = timeMillis
                                width = it?.width?:0
                                height = it?.height?:0
                                type = "PNG"
                                size = fileSize
                            })
                        }
                    }
                    LockPhotoDB.getInstance().fileDao().insert(list)
                }
            }.onSuccess {
                addFilesED.postValue(true)
            }.onFailure {
                addFilesED.postValue(false)
            }
        }
    }
}