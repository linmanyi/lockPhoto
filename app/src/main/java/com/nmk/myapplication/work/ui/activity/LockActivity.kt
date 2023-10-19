package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.databinding.ActivityLockBinding
import com.nmk.myapplication.work.manager.UserInfoManager
import com.nmk.myapplication.work.ui.view.numberKeyboard.NumberKeyboardView
import com.nmk.myapplication.work.utils.common.CacheUtil
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
        /**确认密码**/
        const val CONFIRM_PASSWORD = 2

        fun startActivity(context: Context,type: Int = VALIDATE_PASSWORD) {
            context.startActivity(Intent(context, LockActivity::class.java).putExtra("type",type))
        }
    }

    private var type = 0
    private var teamPassword = ""
    override fun initView(savedInstanceState: Bundle?) {
        type = intent.getIntExtra("type", VALIDATE_PASSWORD)
        updateUI()
        mViewBind.verificationCodeInputView.codeInputListener = {
            onNext(type,it)
            mViewBind.verificationCodeInputView.clearValue()
        }
        mViewBind.keyboardView.listener = object : NumberKeyboardView.OnClickKeyListener{
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

    private fun updateUI() {
        mViewBind.verificationCodeInputView.clearValue()
        mViewBind.title.text = when (type) {
            VALIDATE_PASSWORD -> {
                "输入密码"
            }
            SETTING_PASSWORD -> {
                "设置密码"
            }
            CONFIRM_PASSWORD -> {
                "再次输入密码"
            }
            else -> {
                "输入密码"
            }
        }
    }

    private fun onNext(type: Int,string: String) {
        when(type) {
            VALIDATE_PASSWORD -> {
                if (UserInfoManager.getInstance().passwordIsRight(string)) {
                    MainActivity.startActivity(this)
                    finish()
                } else {
                    ToastUtils.showToast(this,"密码错误")
                }
            }
            SETTING_PASSWORD -> {
                teamPassword = string
                this.type = CONFIRM_PASSWORD
                updateUI()
            }
            CONFIRM_PASSWORD -> {
                if (teamPassword == string) {
                    UserInfoManager.getInstance().updatePassword(string)
                    MainActivity.startActivity(this)
                    finish()
                } else {
                    ToastUtils.showToast(this,"输入密码不一致")
                    updateUI()
                }
            }
        }
    }
}