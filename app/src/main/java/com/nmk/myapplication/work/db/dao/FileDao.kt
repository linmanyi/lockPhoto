package com.nmk.myapplication.work.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nmk.myapplication.work.db.data.FileModel
import com.nmk.myapplication.work.db.data.FolderModel

/**
 * 问价-DAO
 */
@Dao
abstract class FileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(model: FileModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(models: List<FileModel>)

    @Query("select * from db_file where db_id = :id")
    abstract fun queryDataById(id: Long): List<FileModel>

    @Query("select * from db_file where folderId = :folderId")
    abstract fun queryDataByFolder(folderId: Long): List<FileModel>

    @Query("delete from db_file where db_id = :fileId")
    abstract fun deleteById(fileId: Long)

    @Delete
    abstract fun deleteList(models: List<FileModel>)

    @Query("select * from db_file where type = :type")
    abstract fun queryByType(type: String): List<FileModel>

    @Query("update db_file set folderId = :newId where db_id = :fileId")
    abstract fun moveFolder(fileId: Long, newId: Long)

}