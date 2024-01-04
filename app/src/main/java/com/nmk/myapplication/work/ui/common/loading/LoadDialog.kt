package com.nmk.myapplication.work.ui.common.loading

import android.content.Context
import com.nmk.myapplication.R
import com.nmk.myapplication.work.ui.common.BaseBottomDialog
import com.nmk.myapplication.work.ui.common.BaseCenterDialog

/**
 * 加载弹窗
 */
class LoadDialog(context: Context): BaseCenterDialog(context) {

    override fun getLayoutId() = R.layout.dialog_loading

    override fun initView() {

    }

    fun showDialog() {
        showDialog(BaseBottomDialog.Builder(dismissOnBackPressed = false, dismissOnTouchOutside = false, isShowBg = false))
    }
}