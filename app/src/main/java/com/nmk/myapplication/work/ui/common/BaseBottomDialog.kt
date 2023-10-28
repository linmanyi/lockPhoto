package com.nmk.myapplication.work.ui.common

import android.content.Context
import com.nmk.myapplication.work.network.http.HttpManager
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.nmk.myapplication.work.ext.isDestroy
import com.nmk.myapplication.work.network.http.MyAppException
import kotlinx.coroutines.Job
import me.hgj.jetpackmvvm.network.BaseResponse

/**
 * 全局底部弹窗父类
 * @author Created by H-ray on 2022/12/26.
 */
abstract class BaseBottomDialog(context: Context) : BottomPopupView(context), BaseDialog {

    private val httpManager by lazy {
        HttpManager()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    override fun dialogTag() = getLayoutId().toString()

    override fun contrastSameDialog() = false

    override fun getImplLayoutId() = getLayoutId()

    override fun onCreate() {
        super.onCreate()
        BaseDialogManager.getInstance().addDialog(dialogTag(), this)
        initView()
    }

    /**
     * 显示普通弹窗
     */
    protected fun showDialog(builder: Builder = Builder()) {
        if (context.isDestroy())
            return
        XPopup.Builder(context).hasShadowBg(builder.isShowBg).enableDrag(builder.enableDrag)
            .dismissOnTouchOutside(builder.dismissOnTouchOutside).dismissOnBackPressed(builder.dismissOnBackPressed)
            .autoOpenSoftInput(builder.autoOpenSoftInput).popupAnimation(builder.popupAnimation).asCustom(this).show()
    }

    data class Builder(
        var isShowBg: Boolean = true,
        var enableDrag: Boolean = false,
        var autoOpenSoftInput: Boolean = false,
        var dismissOnTouchOutside: Boolean = true,
        var dismissOnBackPressed: Boolean = true,
        var popupAnimation: PopupAnimation? = null
    )

    protected fun <T> sendHttp(block: suspend () -> BaseResponse<T>,
                               success: (T) -> Unit,
                               error: (MyAppException) -> Unit = {},
                               isShowDialog: Boolean = false,
                               loadingMessage: String = ""): Job{
        return httpManager.sendHttp(block, success, error, isShowDialog, loadingMessage)
    }

    /**
     * 销毁所有请求
     */
    private fun cancelAllHttp(){
        httpManager.cancelAllHttp()
    }

    override fun dismiss() {
        super.dismiss()
        BaseDialogManager.getInstance().removeDialog(dialogTag())
        cancelAllHttp()
    }

}