package com.nmk.myapplication.work.utils.file

object FileConstance {
    private const val mainPath = "/private"

    /**
     * 获取文件夹路径
     */
    fun getFolderPath(folderPath: String): String {
        return FileUtil.getSdCardPath() + folderPath  //录音存储路径
    }
}