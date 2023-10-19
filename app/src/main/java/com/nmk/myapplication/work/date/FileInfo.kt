package com.nmk.myapplication.work.date

/**
 * 相册对象
 */
data class FolderInfo(
    var id: Long = 0,
    var fileName: String = "",
    var cover: String = "",
    var createTime: Long = 0L
)