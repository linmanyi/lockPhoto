package com.nmk.myapplication.work.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 文件夹-表
 */
@Entity(tableName = "db_folder")
class FolderModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "db_id")
    var db_id: Long = 0

    @ColumnInfo(name = "cover")
    var cover: String = ""

    @ColumnInfo(name = "fileName")
    var fileName: String = ""

    @ColumnInfo(name = "createTime")
    var createTime: Long = 0L
}