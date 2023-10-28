package com.nmk.myapplication.work.base

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.StrictMode
import android.view.View
import androidx.viewbinding.ViewBinding
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.R
import com.nmk.myapplication.work.manager.ActivityManager
import com.nmk.myapplication.work.utils.system.StatusBarUtil
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding> : BaseVmVbActivity<VM, DB>() {

    protected var titleBar: TitleBar? = null
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
        initTitleBar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //解决FileUriExposedException异常问题
            val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            builder.detectFileUriExposure()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            disableAutoFill()
        }
    }

    /**
     * 禁用自动填充框架，android10版本部分用户偶现：SyncResultReceiver.waitResult
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun disableAutoFill() {
        window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
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

    open fun initTitleBar() {
        titleBar = findViewById(R.id.titleBar) as? TitleBar
        if (titleBar != null) {
            titleBar?.onClickLeftListener = (object : TitleBar.OnClickLeftListener {
                override fun leftOnClick(v: View, isBack: Boolean) {
                    if (isBack) {
                        finish()
                    }
                }
            })
        }
    }
}