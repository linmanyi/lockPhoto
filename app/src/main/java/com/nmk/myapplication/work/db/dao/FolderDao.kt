package com.nmk.myapplication.work.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nmk.myapplication.work.db.data.FolderModel

@Dao
abstract class FolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(model: FolderModel)

    @Query("select * from db_folder")
    abstract fun queryData(): ArrayList<FolderModel>

    @Query("delete from db_folder where db_id = :folderId")
    abstract fun deleteById(folderId: Long)

}