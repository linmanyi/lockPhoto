package com.nmk.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.nmk.myapplication.base.BaseActivity
import com.nmk.myapplication.databinding.ActivityMainBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 主页
 */
class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {

    fun startActivity(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}