package com.nmk.myapplication.work.vm

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.luck.picture.lib.entity.LocalMedia
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.db.LockPhotoDB
import com.nmk.myapplication.work.helper.picture.PictureSelectHelper
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

    val addFilesED = EventLiveData<FileInfo>()
    fun addFiles(context: Context,select: ArrayList<LocalMedia?>?) {
        PictureSelectHelper.getDynamicImg(context,select) {
            Toast.makeText(context,it?.size.toString(),Toast.LENGTH_LONG)
        }
    }
}