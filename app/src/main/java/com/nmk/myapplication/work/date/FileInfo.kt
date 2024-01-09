package com.nmk.myapplication.work.date

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.nmk.myapplication.work.db.data.FileModel
import java.io.Serializable

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
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()?:"",
        parcel.readLong(),
        parcel.readString()?:"",
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(fileName)
        parcel.writeLong(folderId)
        parcel.writeString(content)
        parcel.writeLong(createTime)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeString(type)
        parcel.writeString(size)
        parcel.writeByte(if (select) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileInfo> {
        override fun createFromParcel(parcel: Parcel): FileInfo {
            return FileInfo(parcel)
        }

        override fun newArray(size: Int): Array<FileInfo?> {
            return arrayOfNulls(size)
        }
    }

    fun toModel(): FileModel {
        return FileModel().apply {
            this.db_id = id
            this.folderId = this@FileInfo.folderId
            this.fileName = this@FileInfo.fileName
            this.cover = content
            this.createTime = this@FileInfo.createTime
            this.width = this@FileInfo.width
            this.height = this@FileInfo.height
            this.type = this@FileInfo.type
            this.size = this@FileInfo.size
        }
    }
}