package com.nmk.myapplication.work.ui.common

import android.content.Context
import com.nmk.myapplication.work.ext.isDestroy
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import com.nmk.myapplication.work.manager.HttpManager
import com.nmk.myapplication.work.utils.http.MyAppException
import com.nmk.myapplication.work.utils.http.data.BaseResponse
import kotlinx.coroutines.Job

/**
 * 全局底部弹窗父类
 * @author Created by H-ray on 2022/12/26.
 */
abstract class BaseCenterDialog(context: Context) : CenterPopupView(context), BaseDialog {

    private val httpManager by lazy {
        HttpManager()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    override fun getImplLayoutId() = getLayoutId()

    override fun dialogTag() = getLayoutId().toString()

    override fun contrastSameDialog() = false

    override fun onCreate() {
        super.onCreate()
        BaseDialogManager.getInstance().addDialog(dialogTag(), this)
        initView()
    }

    /**
     * 显示普通弹窗
     */
    protected fun showDialog(builder: BaseBottomDialog.Builder = BaseBottomDialog.Builder()) {
        if (context.isDestroy())
            return
        if (contrastSameDialog()) {
            //存在相同的弹窗则不弹出
            if (BaseDialogManager.getInstance().isExitDialog(dialogTag())) {
                return
            }
        }
        XPopup.Builder(context).hasShadowBg(builder.isShowBg).enableDrag(builder.enableDrag)
            .dismissOnTouchOutside(builder.dismissOnTouchOutside).dismissOnBackPressed(builder.dismissOnBackPressed)
            .autoOpenSoftInput(builder.autoOpenSoftInput).popupAnimation(builder.popupAnimation).asCustom(this).show()
    }

    protected fun <T> sendHttp(
        block: suspend () -> BaseResponse<T>,
        success: (T) -> Unit,
        error: (MyAppException) -> Unit = {},
        isShowDialog: Boolean = false,
        loadingMessage: String = "",
    ): Job {
        return httpManager.sendHttp(block, success, error, isShowDialog, loadingMessage)
    }

    /**
     * 销毁所有请求
     */
    private fun cancelAllHttp() {
        httpManager.cancelAllHttp()
    }

    override fun dismiss() {
        super.dismiss()
        BaseDialogManager.getInstance().removeDialog(dialogTag())
        cancelAllHttp()
    }

}