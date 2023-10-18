package com.nmk.myapplication.ui.activity

import android.os.Bundle
import com.nmk.myapplication.base.BaseActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import com.nmk.myapplication.databinding.ActivitySplashBinding

/**
 * 闪屏
 */
class SplashActivity: BaseActivity<BaseViewModel,ActivitySplashBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.root.postDelayed({
            LockActivity.startActivity(this)
            finish()
        },3000)
    }
}