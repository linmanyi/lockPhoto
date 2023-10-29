package com.nmk.myapplication.work.ui.dialog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nmk.myapplication.R
import com.nmk.myapplication.work.ui.common.BaseBottomDialog
import com.nmk.myapplication.work.vm.MainVM

class FolderMoreDialog(context: Context): BaseBottomDialog(context) {
    override fun getLayoutId() = R.layout.folder_dialog_more

    override fun initView() {

    }
}