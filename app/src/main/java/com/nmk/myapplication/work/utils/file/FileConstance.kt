package com.nmk.myapplication.work.utils.file

import android.annotation.SuppressLint

object FileConstance {
    private const val mainPath = "/private"

    /**
     * 获取文件路径
     */
    fun getPrivateFilePath(folder: String, name: String): String {
        return "$mainPath/$folder/$name"
    }

    /**
     * 获取文件夹路径
     */
    fun getPrivateFolderPath(folder: String): String {
        return "$mainPath/$folder"
    }

    /**
     * 总文件夹
     */
    fun getPrivatePath(): String {
        return "$mainPath/"
    }

    /**
     * 完整路径
     */
    fun getFullPrivatePath(path: String = ""): String {
        return "${FileUtil.getSdCardPath()}/$mainPath/$path"
    }
}