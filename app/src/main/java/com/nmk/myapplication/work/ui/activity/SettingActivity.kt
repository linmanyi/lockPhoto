package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.nmk.myapplication.databinding.ActivitySettingBinding
import com.nmk.myapplication.work.base.BaseActivity
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

    }
}