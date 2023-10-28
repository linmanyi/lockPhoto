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

/**
 * 文件对象
 */
data class FileInfo(
    var id: Long = 0,
    var fileName: String = "",
    var content: String = "",
    var createTime: Long = 0L,
    var width: Int = 0,
    var height: Int = 0,
    var type: String = "",//类型
    var size: Long = 0,//文件大小
)