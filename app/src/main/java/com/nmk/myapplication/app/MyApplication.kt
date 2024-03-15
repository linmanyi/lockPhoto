package com.nmk.myapplication.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.nmk.myapplication.work.utils.log.XLogDiskManager
import com.tencent.mmkv.MMKV

class MyApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        var isBackground: Boolean = false
    }
    override fun onCreate() {
        super.onCreate()
        mContext = this
        MMKV.initialize(mContext)
        XLogDiskManager.getInstance().initXLogConfig()
    }
}