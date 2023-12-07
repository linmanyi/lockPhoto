package com.nmk.myapplication.work.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nmk.myapplication.work.db.data.FileModel
import com.nmk.myapplication.work.db.data.FolderModel

/**
 * 问价-DAO
 */
@Dao
abstract class FileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(model: FileModel)

    @Query("select * from db_file where db_id = :id")
    abstract fun queryDataById(id: Long): List<FileModel>

    @Query("delete from db_file where db_id = :fileId")
    abstract fun deleteById(fileId: Long)

    @Query("select * from db_file where type = :type")
    abstract fun queryByType(type: String): List<FileModel>

}