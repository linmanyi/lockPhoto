package com.nmk.myapplication.work.ui.common

/**
 * @desc： 弹窗管理类
 * @author： Created by H-ray on 2023/7/17.
 */
class BaseDialogManager private constructor() {

    private val dialogMap = mutableMapOf<String, BaseDialog>()

    companion object {
        fun getInstance() = HolderInstance.instance
    }

    object HolderInstance {
        val instance = BaseDialogManager()
    }

    /**
     * 关闭某个弹窗
     */
    fun dismissDialogForTag(tag: String) {
        val dialog = dialogMap[tag] ?: return
        when (dialog) {
            is BaseCenterDialog -> {
                dialog.dismiss()
            }
            is BaseBottomDialog -> {
                dialog.dismiss()
            }
//            is BaseFullDialog -> {
//                dialog.dismiss()
//            }
//            is BaseDialogFragment<*> -> {
//                dialog.dismissAllowingStateLoss()
//            }
        }
    }

    fun isExitDialog(tag: String): Boolean {
        return dialogMap.containsKey(tag)
    }

    fun addDialog(tag: String, dialog: BaseDialog) {
        if (!dialogMap.containsKey(tag)) {
            dialogMap[tag] = dialog
        }
    }

    fun removeDialog(tag: String) {
        dialogMap.remove(tag)
    }

    fun removeAllDialog() {
        dialogMap.clear()
    }

}