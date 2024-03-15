package com.nmk.myapplication.work.utils.log

import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.Logger
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.PatternFlattener
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.FileNameGenerator
import com.nmk.myapplication.app.MyApplication
import com.nmk.myapplication.work.utils.common.CommonDateFormatUtil

/**
 * 磁盘日志管理工具，基于XLog
 * https://github.com/elvishew/xLog/blob/master/README_ZH.md
 *
 * @author Created by H-ray on 2021/11/23.
 */
class XLogDiskManager private constructor() {

    /**
     * 系统日志Logger，用于打印系统日志
     */
    private var sysLogger: Logger? = null

    /**
     * 文件路径
     */
    private var sdPath = ""

    /**
     * 是否初始化过
     */
    private var isInit = false

    companion object {
        const val logFilePath = "/Log"
        fun getInstance() = HolderInstance.instance
    }

    object HolderInstance {
        var instance = XLogDiskManager()
    }

    /**
     * 初始化磁盘日志
     */
    fun initXLogConfig() {
        XLog.init(LogLevel.ALL)
        sdPath = MyApplication.mContext.cacheDir.toString()
        if (sdPath.isEmpty()) return
        if (sysLogger != null) return

        //创建系统日志Logger
        val sysFilePrinter: FilePrinter = FilePrinter.Builder(sdPath + logFilePath) // 指定保存日志文件的路径
            .fileNameGenerator(MyFileNameGenerator()) // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
            .backupStrategy(NeverBackupStrategy()) // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)，当前为不进行日志备份
            .flattener(MyPattern())
            .cleanStrategy(FileLastModifiedCleanStrategy(7 * 24 * 3600 * 1000L))
            .build()
        sysLogger = XLog.printers(sysFilePrinter).build()

        isInit = true
    }

    /**
     * 写入系统日志
     */
    fun writeSys(value: String?) {
        if (value.isNullOrEmpty()) return
        sysLogger?.i(value)
        if (sysLogger == null) {
            nullToInit()
        }
    }

    /**
     * 中途如果出现被销毁了需要重新创建流程
     */
    private var isCheckToInit = false
    private fun nullToInit() {
        if (isInit && sysLogger == null && !isCheckToInit) {
            isCheckToInit = true
            initXLogConfig()
            isCheckToInit = false
        }
    }

    /**
     * 自定义日志文件命名
     *
     */
    private inner class MyFileNameGenerator : FileNameGenerator {

        override fun isFileNameChangeable(): Boolean {
            return true
        }

        override fun generateFileName(logLevel: Int, timestamp: Long): String {
            val lastYMD = CommonDateFormatUtil.getFormatYMD(timestamp).replace("/", "")
            return "log${lastYMD}.xlog"
        }
    }

    /**
     * 自定义Pattern，不需要横铺器
     */
    private class MyPattern(pattern: String = "{m}") : PatternFlattener(pattern)

}