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

//    @Query("select * from db_file where folderId = :folderId")
//    abstract fun queryDataByFolder(folderId: Long): List<FileModel>
    /**
     * 导入时间-升序
     */
    @Query("select * from db_file where folderId = :folderId ORDER BY createTime ASC")
    abstract fun queryDataByFolderTimeAsc(folderId: Long): List<FileModel>

    /**
     * 导入时间-降序
     */
    @Query("select * from db_file where folderId = :folderId ORDER BY createTime DESC")
    abstract fun queryDataByFolderTimeDESC(folderId: Long): List<FileModel>

    /**
     * 文件名-升序
     */
    @Query("select * from db_file where folderId = :folderId ORDER BY fileName Asc")
    abstract fun queryDataByFolderNameAsc(folderId: Long): List<FileModel>

    /**
     * 文件名-降序
     */
    @Query("select * from db_file where folderId = :folderId ORDER BY fileName DESC")
    abstract fun queryDataByFolderNameDESC(folderId: Long): List<FileModel>

    /**
     * 文件大小-升序
     */
    @Query("select * from db_file where folderId = :folderId ORDER BY size Asc")
    abstract fun queryDataByFolderSizeAsc(folderId: Long): List<FileModel>

    /**
     * 文件大小-降序
     */
    @Query("select * from db_file where folderId = :folderId ORDER BY size DESC")
    abstract fun queryDataByFolderSizeDESC(folderId: Long): List<FileModel>

    /**
     * 文件类型-升序
     */
    @Query("select * from db_file where folderId = :folderId ORDER BY type Asc")
    abstract fun queryDataByFolderTypeAsc(folderId: Long): List<FileModel>

    /**
     * 文件类型-降序
     */
    @Query("select * from db_file where folderId = :folderId ORDER BY type DESC")
    abstract fun queryDataByFolderTypeDESC(folderId: Long): List<FileModel>

    @Query("delete from db_file where db_id = :fileId")
    abstract fun deleteById(fileId: Long)

    @Query("delete from db_file")
    abstract fun deleteAll()

    @Delete
    abstract fun deleteList(models: List<FileModel>)

    @Query("select * from db_file where type = :type")
    abstract fun queryByType(type: String): List<FileModel>

    @Update
    abstract fun updateFolder(model: FileModel)

}