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
    var folderId: Long = 0,
    var content: String = "",
    var createTime: Long = 0L,
    var width: Int = 0,
    var height: Int = 0,
    var type: String = "",//类型
    var size: String = "0B",//文件大小

    var select: Boolean = false,//是否选择
)