package com.nmk.myapplication.work.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nmk.myapplication.app.MyApplication
import com.nmk.myapplication.work.db.dao.FileDao
import com.nmk.myapplication.work.db.dao.FolderDao
import com.nmk.myapplication.work.db.data.FolderModel

@Database(entities = [
    FolderModel::class
], version = 1, exportSchema = false)
abstract class LockPhotoDB: RoomDatabase() {
    /**
     * 文件夹DAO
     */
    abstract fun folderDao(): FolderDao
    abstract fun fileDao(): FileDao
    companion object {
        private const val DATABASE_NAME = "lock_photo.db"

        @Volatile
        private var databaseInstance: LockPhotoDB? = null

//        private var MIGRATION1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE `chat_fast_count` (`db_id` INTEGER NOT NULL, `my_user_id` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `count` INTEGER NOT NULL, PRIMARY KEY(`db_id`))")
//                database.execSQL("CREATE TABLE `pay_order` (`db_id` INTEGER NOT NULL, `order_id` TEXT NOT NULL, `user_id` INTEGER NOT NULL, `time` INTEGER NOT NULL, `pay_state` INTEGER NOT NULL, PRIMARY KEY(`db_id`))")
//            }
//        }

        @Synchronized
        fun getInstance(): LockPhotoDB {
            if (databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(MyApplication.mContext, LockPhotoDB::class.java, DATABASE_NAME)
//                    .addMigrations(MIGRATION1_2)
                    .build()
            }
            return databaseInstance!!
        }

    }
}