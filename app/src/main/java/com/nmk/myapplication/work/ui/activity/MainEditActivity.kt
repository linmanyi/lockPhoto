package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.nmk.myapplication.databinding.FolderActivityEditBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.vm.MainVM

/**
 * 文件夹设置
 */
class MainEditActivity: BaseActivity<MainVM, FolderActivityEditBinding>() {

    companion object{
        fun startActivity(context: Context,id: Long) {
            context.startActivity(Intent(context, MainEditActivity::class.java).putExtra("id",id))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewModel
    }
}