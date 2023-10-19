package com.nmk.myapplication.work.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.viewbinding.ViewBinding
import com.nmk.myapplication.work.manager.ActivityManager
import com.nmk.myapplication.work.utils.system.StatusBarUtil
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding> : BaseVmVbActivity<VM, DB>() {
    override fun createObserver() {

    }

    override fun dismissLoading() {

    }

    open fun isImmersiveState(): Boolean {
        return true
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
        if (isImmersiveState()) {
            StatusBarUtil.setImmersiveStatusBar(this, isFontIconDark())
        }
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