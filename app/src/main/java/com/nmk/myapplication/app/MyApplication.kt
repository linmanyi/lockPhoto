package com.nmk.myapplication.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.nmk.myapplication.work.utils.log.XLogDiskManager
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSize

class MyApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        var isBackground: Boolean = false
    }
    override fun onCreate() {
        super.onCreate()
        mContext = this
        initAutoSize()
        MMKV.initialize(mContext)
        XLogDiskManager.getInstance().initXLogConfig()
    }

    private fun initAutoSize() {
        AutoSize.initCompatMultiProcess(this)
        AutoSize.checkAndInit(this)
    }
}