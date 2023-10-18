package com.nmk.myapplication.ui.activity

import android.os.Bundle
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.base.BaseActivity
import com.nmk.myapplication.databinding.ActivityLockBinding
import com.nmk.myapplication.ui.view.numberKeyboard.NumberKeyboardView
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 操作密码页面
 */
class LockActivity: BaseActivity<BaseViewModel, ActivityLockBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.verificationCodeInputView.codeInputListener = {
            ToastUtils.showToast(this,"密码是$it")
            mViewBind.verificationCodeInputView.clearValue()
        }
        mViewBind.keyboardView.listener = object :NumberKeyboardView.OnClickKeyListener{
            override fun onClickNum(string: String) {
                mViewBind.verificationCodeInputView.getEtView().text.insert(mViewBind.verificationCodeInputView.getEtView().text.length, string)
            }

            override fun onClickDelete() {
                var text = mViewBind.verificationCodeInputView.getEtView().text.toString()
                val newText: String = text.substring(0, text.length - 1)
                mViewBind.verificationCodeInputView.getEtView().setText(newText)
                mViewBind.verificationCodeInputView.getEtView().setSelection(newText.length)
            }
        }
    }
}