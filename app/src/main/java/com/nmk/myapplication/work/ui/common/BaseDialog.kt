package com.nmk.myapplication.work.ui.common

/**
 * @desc： Dialog管理
 * @author： Created by H-ray on 2023/7/17.
 */
interface BaseDialog {

    /**
     * 获取该Dialog的Tag
      */
    fun dialogTag(): String

    /**
     * 是否需要对比相同类型弹窗
     */
    fun contrastSameDialog(): Boolean

}