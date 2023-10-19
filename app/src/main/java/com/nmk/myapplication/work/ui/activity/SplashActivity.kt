package com.nmk.myapplication.work.ui.activity

import android.os.Bundle
import com.nmk.myapplication.work.base.BaseActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import com.nmk.myapplication.databinding.ActivitySplashBinding
import com.nmk.myapplication.work.manager.UserInfoManager

/**
 * 闪屏
 */
class SplashActivity: BaseActivity<BaseViewModel, ActivitySplashBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        val type = if (UserInfoManager.getInstance().isSettingPassword()) {
            LockActivity.VALIDATE_PASSWORD
        } else {
            LockActivity.SETTING_PASSWORD
        }
        mViewBind.root.postDelayed({
            LockActivity.startActivity(this, type)
            finish()
        },3000)
    }
}