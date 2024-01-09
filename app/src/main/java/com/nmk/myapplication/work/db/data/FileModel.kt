package com.nmk.myapplication.work.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 文件-表
 */
@Entity(tableName = "db_file")
class FileModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "db_id")
    var db_id: Long = 0

    @ColumnInfo(name = "folderId")
    var folderId: Long = 0

    @ColumnInfo(name = "fileName")
    var fileName: String = ""

    @ColumnInfo(name = "cover")
    var cover: String = ""

    @ColumnInfo(name = "createTime")
    var createTime: Long = 0L

    @ColumnInfo(name = "width")
    var width: Int = 0

    @ColumnInfo(name = "height")
    var height: Int = 0

    @ColumnInfo(name = "type")
    var type: String = ""

    @ColumnInfo(name = "size")
    var size: String = "0B"
}