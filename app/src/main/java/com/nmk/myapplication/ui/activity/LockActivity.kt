package com.nmk.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.base.BaseActivity
import com.nmk.myapplication.databinding.ActivityLockBinding
import com.nmk.myapplication.ui.view.numberKeyboard.NumberKeyboardView
import com.nmk.myapplication.utils.common.CacheUtil
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 操作密码页面
 */
class LockActivity: BaseActivity<BaseViewModel, ActivityLockBinding>() {

    companion object{

        /**验证密码**/
        const val VALIDATE_PASSWORD = 0
        /**设置密码**/
        const val SETTING_PASSWORD = 1

        fun startActivity(context: Context,type: Int = VALIDATE_PASSWORD) {
            context.startActivity(Intent(context, LockActivity::class.java).putExtra("type",type))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val type = intent.getIntExtra("type", VALIDATE_PASSWORD)
        mViewBind.title.text = when(type){
            VALIDATE_PASSWORD -> {
                "输入密码"
            }
            SETTING_PASSWORD -> {
                "设置密码"
            }
            else -> {
                "输入密码"
            }
        }
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

    private fun onNext(type: Int) {
        when(type) {
            VALIDATE_PASSWORD -> {

            }
        }
    }
}