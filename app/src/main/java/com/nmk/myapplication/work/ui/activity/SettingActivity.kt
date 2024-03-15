package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.ActivitySettingBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.activity.LockActivity.Companion.SETTING_PASSWORD
import com.nmk.myapplication.work.ui.common.loading.LoadingManager
import com.nmk.myapplication.work.vm.SettingMV

/**
 * 设置页
 */
class SettingActivity: BaseActivity<SettingMV, ActivitySettingBinding>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(
                Intent(context, SettingActivity::class.java)
            )
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.resetPasswordTv.setClickNotDoubleListener {
            LockActivity.startActivity(this,SETTING_PASSWORD)
        }
        mViewBind.clearTv.setClickNotDoubleListener {
            mViewModel.deleteAll(this)
        }
    }

    override fun createObserver() {
        mViewModel.deleteAllED.observeInActivity(this) {
            LoadingManager.getInstance().hideDialog()
            if (!it) ToastUtils.showToast(this,getString(R.string.delete_failure))
        }
    }
}