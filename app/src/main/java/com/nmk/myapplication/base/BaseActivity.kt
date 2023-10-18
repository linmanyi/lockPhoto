package com.nmk.myapplication.base

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.nmk.myapplication.manager.ActivityManager
import com.nmk.myapplication.utils.system.StatusBarUtil
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding> : BaseVmVbActivity<VM, DB>() {
    override fun createObserver() {

    }

    override fun dismissLoading() {

    }

    /**
     * 状态栏字体是否为黑色
     * @return Boolean
     */
    open fun isFontIconDark(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        StatusBarUtil.setImmersiveStatusBar(this, isFontIconDark())
        ActivityManager.getInstance().pushActivity(this)
    }

    override fun showLoading(message: String) {

    }

    override fun finish() {
        ActivityManager.getInstance().pupActivity(this)
        super.finish()
    }

    override fun onDestroy() {
        ActivityManager.getInstance().pupActivity(this)
        super.onDestroy()
    }
}