package com.nmk.myapplication.work.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import com.nmk.myapplication.work.base.BaseActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import com.nmk.myapplication.databinding.ActivitySplashBinding
import com.nmk.myapplication.work.manager.UserInfoManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 闪屏
 */
@SuppressLint("CustomSplashScreen")
class MainSplashActivity: BaseActivity<BaseViewModel, ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.view.animate().alpha(0f).duration = 300L
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            val type = if (UserInfoManager.getInstance().isSettingPassword()) {
                LockActivity.VALIDATE_PASSWORD
            } else {
                LockActivity.SETTING_PASSWORD
            }
            LockActivity.startActivity(this@MainSplashActivity, type)
            finish()
        }
    }
}